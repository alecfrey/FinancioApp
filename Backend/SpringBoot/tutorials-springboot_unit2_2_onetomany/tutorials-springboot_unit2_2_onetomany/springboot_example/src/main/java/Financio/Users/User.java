package Financio.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import Financio.Expense.Expense;

@Entity
public class User {

    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @JoinColumn(name = "userId")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    private String username;
    private String password;
    private String address;
    private String city;
    private String state;
    private int zip;
    private Date dateCreated;
    private Date dateLocked;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String gender;
    private String picturePath;
    private String email;

    @OneToOne
    private Expense expense;

     // =============================== Constructors ================================== //


    public User(String username,
    		String password,
    		String address,
    		String city,
    		String state,
    		int zip,
    		String firstName,
    		String middleInitial,
    		String lastName,
    		String gender,
    		String picturePath,
    		String email) 
    {
        this.username = username;
        this.password = password;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.dateCreated = new Date(System.currentTimeMillis());
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.gender = gender;
        this.picturePath = picturePath;
        this.email = email;
    }
    
    public User() {}
    
    // =============================== Getters and Setters for each field ================================== //


    public int getUserId() {
		return id;
	}

	public void setUserId(int userId) {
		this.id = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public Date getDateLocked() {
		return dateLocked;
	}

	public void setDateLocked(Date dateLocked) {
		this.dateLocked = dateLocked;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDateCreated() {
		return dateCreated;
	}
    
}
