package com.gatech.gameswap.repository;

import java.sql.SQLException;

import com.gatech.gameswap.model.User;

public interface UserRepository {

	Boolean createUser(User user) throws SQLException;
	Boolean updateUser(User user) throws SQLException;
	Boolean login(String userId, String password) throws SQLException;
	
}
