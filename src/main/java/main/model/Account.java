package main.model;

/**
 * Class {@code Account} is a class for user account.
 */
public class Account extends Model {

	private long amount;

	public Account(long id, long amount) {
		super(id);
		this.amount = amount;
	}

	/**
	 * Withdraws funds from the account
	 * @param amount the amount to be written off
	 */
	public void withdraw(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Invalid amount");
		}
		if (amount > this.amount) {
			throw new IllegalArgumentException("Insufficient funds");
		}
		this.amount -= amount;
	}

	/**
	 * Deposit funds into the account
	 * @param amount the amount to be deposited
	 */
	public void deposit(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Invalid amount");
		}
		this.amount += amount;
	}

}
