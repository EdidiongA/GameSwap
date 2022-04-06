package com.gatech.gameswap.model;

public class SwapAck {
	private String proposed_date;
	private String proposer;
	private String proposed_item;
	private String desired_item;
	private Double distance;
	private int Rating;
	
	
	public String getProposed_date() {
		return proposed_date;
	}
	public void setProposed_date(String proposed_date) {
		this.proposed_date = proposed_date;
	}
	public String getProposer() {
		return proposer;
	}
	public void setProposer(String proposer) {
		this.proposer = proposer;
	}
	public String getProposed_item() {
		return proposed_item;
	}
	public void setProposed_item(String proposed_item) {
		this.proposed_item = proposed_item;
	}
	public String getDesired_item() {
		return desired_item;
	}
	public void setDesired_item(String desired_item) {
		this.desired_item = desired_item;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public int getRating() {
		return Rating;
	}
	public void setRating(int rating) {
		Rating = rating;
	}
	
	
}
