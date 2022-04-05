package com.gatech.gameswap.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gatech.gameswap.model.User;
import com.gatech.gameswap.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public Boolean createUser(User user) throws SQLException {
		return userRepository.createUser(user);	
	
	}

	@Override
	public String updateUser(User user) throws SQLException {
		
		if(validateUnacceptedSwaps(user)) {
			return "PENDING_UNACCEPTED_SWAPS";
		}else {
			Boolean success = userRepository.updateUser(user);
			return success ? "SUCCESS" : "FAILURE" ;
		}
		
	}
	
	private Boolean validateUnacceptedSwaps(User user) {
		//call repo method to validate
		return false;
	}

	@Override
	public Boolean login(String userId, String password) throws SQLException {
		return userRepository.login(userId, password);
	}
}
