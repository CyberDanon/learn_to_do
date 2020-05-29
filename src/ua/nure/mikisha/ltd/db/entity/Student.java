package ua.nure.mikisha.ltd.db.entity;

public class Student extends User {
	protected int balance;
	protected int bonuses;
	@Override
	public int getBalance() {
		return balance;
	}
	@Override
	public void setBalance(int balance) {
		this.balance = balance;
	}
	@Override
	public int getBonuses() {
		return bonuses;
	}
	@Override
	public void setBonuses(int bonuses) {
		this.bonuses = bonuses;
	}
}
