/**
 * The Package class is an object which contains the name of the recipient,
 * the date of arrival of the package, and its weight in pounds.
 * @author Benny Feng
 * 	email: benny.feng@stonybrook.edu
 * 	SBU ID: 111053634
 * 	HW#3 CSE-214 R04 Michael Alvin
 */
public class Package {
	private String recipient;
	private int arrivalDate;
	private double weight;
	/**
	 * Constructor for Package Object
	 * @param recipient
	 * 	The name of the recipient
	 * @param arrivalDate
	 * 	The day of arrival 
	 * @param weight
	 * 	The weight of the package in pounds
	 */
	public Package(String recipient,int arrivalDate, double weight){
		this.recipient = recipient;
		this.arrivalDate = arrivalDate;
		this.weight = weight;
	}
	/**
	 * Default Constructor for Package Object
	 */
	public Package(){
		recipient = "Benny";
		arrivalDate = 1;
		weight = 10;
	}
	/**
	 * Gets the name of the recipient
	 * @return
	 * 	Returns the name of the recipient
	 */
	public String getRecipient() {
		return recipient;
	}
	/**
	 * Changes the name of the recipient
	 * @param recipient
	 * 	What name to change recipient to
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	/**
	 * Gets the arrival day
	 * @return
	 * 	Returns the day of arrival
	 */
	public int getArrivalDate() {
		return arrivalDate;
	}
	/**
	 * Changes day of arrival
	 * @param arrivalDate
	 * 	What day arrivalDate should be
	 */
	public void setArrivalDate(int arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	/**
	 * Gets the weight of the Package
	 * @return
	 * 	Returns the weight
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * Changes the weight of the Package
	 * @param weight
	 * 	What the new weight of the Package should be
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String toString() {
		return "[" + recipient + " " + arrivalDate + "]";
	}
}
