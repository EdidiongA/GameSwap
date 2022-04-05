package com.gatech.gameswap.model;

public class Swap {
	String proposerID;
	String counterPartyID;
	int proposerItemID;
	int counterPartyItemID;
	
	public String getProposerID() {
		return proposerID;
	}
	public void setProposerID(String proposerID) {
		this.proposerID = proposerID;
	}
	public String getCounterPartyID() {
		return counterPartyID;
	}
	public void setCounterPartyID(String counterPartyID) {
		this.counterPartyID = counterPartyID;
	}
	public int getProposerItemID() {
		return proposerItemID;
	}
	public void setProposerItemID(int proposerItemID) {
		this.proposerItemID = proposerItemID;
	}
	public int getCounterPartyItemID() {
		return counterPartyItemID;
	}
	public void setCounterPartyItemID(int counterPartyItemID) {
		this.counterPartyItemID = counterPartyItemID;
	}
	
	
}
