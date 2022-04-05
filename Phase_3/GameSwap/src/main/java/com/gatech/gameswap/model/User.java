package com.gatech.gameswap.model;

public class User {

	private String email;
	private String postalCode;
	private String firstName;
	private String lastName;
	private String nickName;
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public User() {
		super();
	}
	public User(String email, String postalCode, String firstName, String lastName, String nickName, String password) {
		super();
		this.email = email;
		this.postalCode = postalCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", postalCode=" + postalCode + ", firstName=" + firstName + ", lastName="
				+ lastName + ", nickName=" + nickName + ", password=" + password + "]";
	}

}
