package Financio.ReportedExpense;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import Financio.Expense.Expense;
import Financio.Users.User;

@Entity
public class ReportedExpense { 
	
	@Id
	@JoinColumn(name = "reported_expenseId")
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
    
    @ManyToOne
    @JoinColumn(name = "expenseId")
    private Expense expense;

     // =============================== Constructors ================================== //
    
   

	public ReportedExpense(double resturant,
	 double subscriptions,
	 double essentials,
	 double grocery,
	 double gas,
	 double alcohol,
	 double other,
	 Expense expense) {
        this.resturant = resturant;
        this.subscriptions = subscriptions;
        this.essentials = essentials;
        this.grocery = grocery;
        this.gas = gas;
        this.alcohol = alcohol;
        this.other = other;
        this.dateCreated = new Date(System.currentTimeMillis());
        this.expense = expense;
    }
    
    public ReportedExpense() {}

	

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

	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
	}
}
