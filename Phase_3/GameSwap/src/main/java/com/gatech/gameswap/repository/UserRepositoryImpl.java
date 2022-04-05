package com.gatech.gameswap.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.gatech.gameswap.model.User;


@Repository
public class UserRepositoryImpl implements UserRepository{



	@Autowired
	private DataSource dataSource;
	
	
	@Override
	 public Boolean createUser(User user) throws SQLException {
	 
	 String sql = "INSERT INTO User(email, postal_code, first_name, last_name, nickname, password) VALUES(?,?,?,?,?,?)";
	 try(Connection conn = dataSource.getConnection()) {
			PreparedStatement statement = conn.prepareStatement(sql);

			statement.setString(1, user.getEmail());
			statement.setString(2, user.getPostalCode());
			statement.setString(3, user.getFirstName());
			statement.setString(4, user.getLastName());
			statement.setString(5, user.getNickName());
			statement.setString(6, user.getPassword());
			
			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
			    System.out.println("A new user was inserted successfully!");
			    return true;
			}

	}catch(Exception e) {
		e.printStackTrace();
	}

	 return false;
	}
	
	@Override
	public Boolean updateUser(User user) throws SQLException {
		
		 
		//return jdbcTemplate.update(sql, user.getLocation().getPostalCode(), user.getFirstName(), user.getLastName(), user.getNickName(), user.getPassword(), user.getEmail()) > 0;
		 return false;
	}
	/*
	 * @Override public Boolean createUser(User user) { // TODO Auto-generated
	 * method stub return null; }
	 */

	@Override
	public Boolean login(String userId, String password) throws SQLException {
		Pattern ptrn = Pattern.compile("(?:\\d{3}_){2}\\d{4}");  
		Matcher match = ptrn.matcher(userId);
		boolean isPhone = (match.find() && match.group().equals(userId)); 
		String psd = null;

		try(Connection conn = dataSource.getConnection()) {
			if(!isPhone) {
				String sql = "SELECT password FROM User WHERE email ='"+ userId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if(result.next())
					psd = result.getString("password");
			}
			else {
				String sql = "SELECT email FROM phoneNumber WHERE phone_number ='"+userId+"'";
				Statement statement = conn.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if(result.next()) {
					String email = result.getString("email");
					String sql1 = "SELECT password FROM User WHERE email ='"+ email+"'";
					Statement stat = conn.createStatement();
					ResultSet res = stat.executeQuery(sql1);
					if(res.next())
						psd = res.getString("password");
				}
				
			}
			conn.close();
			if(psd.equals(password))
				return true;
			
		}
		catch (SQLException ex) {
		    ex.printStackTrace();
		    
		}
		return false;
		
	}

}
