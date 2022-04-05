package com.gatech.gameswap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gatech.gameswap.model.User;
import com.gatech.gameswap.service.UserService;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping(path= "/login")
	public ResponseEntity<Integer> loginUser(@RequestParam String userId, @RequestParam String password) {

		try{
			Boolean loginStatus = userService.login(userId,password);
			if(loginStatus)
				return new ResponseEntity<Integer>(HttpStatus.ACCEPTED);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(HttpStatus.FORBIDDEN);
	}
	
	@PostMapping(path= "/register")
	public ResponseEntity<Integer> create(@RequestBody User user) {

		try{
			userService.createUser(user);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<String> update(User user) {
		
		try{
			userService.updateUser(user);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
		
}
