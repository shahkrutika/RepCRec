package edu.nyu.adbms.repcrec;

class Transaction {
	
}

public class Variable {
	String name;
	int currentValue;
	int lastCommittedValue;
	boolean isLocked;
	Transaction owner;
	
	public Variable(String name, int initValue) {
		this.name = name;
		this.currentValue = initValue;
		this.lastCommittedValue = initValue;
		this.isLocked = false;
		this.owner = null;
	}
	
	public boolean lockVariable(Transaction t) {
		try {
			this.isLocked = true;
			this.owner = t;
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean isTransactionAuthorizedForWrite(Transaction t) {
		if (this.isLocked == true) {
			if (this.owner.hashCode() == t.hashCode())
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
	
	
}
