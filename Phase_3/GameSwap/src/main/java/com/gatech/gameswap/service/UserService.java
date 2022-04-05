package com.gatech.gameswap.service;

import java.sql.SQLException;

import com.gatech.gameswap.model.User;

public interface UserService {

	Boolean createUser(User user) throws SQLException;
	String updateUser(User user) throws SQLException;
	Boolean login(String userId,String password) throws SQLException;
	//List<User> displayAllUsers();

}
