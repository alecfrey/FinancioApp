package Financio.Expense;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import Financio.ReportedExpense.ReportedExpense;
import Financio.Users.User;

@Entity
public class Expense { 
	
	@Id
	@JoinColumn(name = "expenseId")
	@GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
	
	private double resturant;
	private double subscriptions;
	private double essentials;
	private double grocery;
	private double gas;
	private double alcohol;
	private double other;
	private Date dateCreated;
	private Date dateModified;
	private Date dateRemoved;
    
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;
    
    @OneToMany
    private List<ReportedExpense> reportedExpenses;

     // =============================== Constructors ================================== //
    
   

	public Expense(double resturant,
	 double subscriptions,
	 double essentials,
	 double grocery,
	 double gas,
	 double alcohol,
	 double other,
	 User user) {
        this.resturant = resturant;
        this.subscriptions = subscriptions;
        this.essentials = essentials;
        this.grocery = grocery;
        this.gas = gas;
        this.alcohol = alcohol;
        this.other = other;
        this.dateCreated = new Date(System.currentTimeMillis());
        this.user = user;
    }
    
    public Expense() {}

    // =============================== Getters and Setters for each field ================================== //

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getResturant() {
		return resturant;
	}

	public void setResturant(double resturant) {
		this.resturant = resturant;
	}

	public double getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(double subscriptions) {
		this.subscriptions = subscriptions;
	}

	public double getEssentials() {
		return essentials;
	}

	public void setEssentials(double essentials) {
		this.essentials = essentials;
	}

	public double getGrocery() {
		return grocery;
	}

	public void setGrocery(double grocery) {
		this.grocery = grocery;
	}

	public double getGas() {
		return gas;
	}

	public void setGas(double gas) {
		this.gas = gas;
	}

	public double getAlcohol() {
		return alcohol;
	}

	public void setAlcohol(double alcohol) {
		this.alcohol = alcohol;
	}

	public double getOther() {
		return other;
	}

	public void setOther(double other) {
		this.other = other;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Date getDateRemoved() {
		return dateRemoved;
	}

	public void setDateRemoved(Date dateRemoved) {
		this.dateRemoved = dateRemoved;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User userId) {
		this.user = userId;
	}
}
