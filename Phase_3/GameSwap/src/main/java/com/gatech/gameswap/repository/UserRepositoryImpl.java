package com.gatech.gameswap.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gatech.gameswap.model.Location;
import com.gatech.gameswap.model.Phone;
import com.gatech.gameswap.model.Phone.PhoneNumberType;
import com.gatech.gameswap.model.User;
import com.gatech.gameswap.model.UserStats;

@Repository
public class UserRepositoryImpl implements UserRepository{

	@Autowired
	private DataSource dataSource;

	Logger logger = Logger.getLogger(UserRepositoryImpl.class);

	@Override
	public Boolean updateUser(User user) {
		Boolean success = false;
		try(Connection conn = dataSource.getConnection()) {
			try(PreparedStatement pstmt = conn.prepareStatement("UPDATE User SET postal_code = ?, first_name = ?, last_name = ?, nickname = ?, password = ?, phone_number=?, type=?, share_phone_number=? WHERE email = ?")){
				pstmt.setString(1, user.getLocation().getPostalCode());
				pstmt.setString(2, user.getFirstName());
				pstmt.setString(3, user.getLastName());
				pstmt.setString(4, user.getNickName());
				pstmt.setString(5, user.getPassword());
				pstmt.setString(6, user.getPhone().getNumber());
				pstmt.setString(7, user.getPhone().getType().toString());
				pstmt.setBoolean(8, user.getPhone().isShare());
				pstmt.setString(9, user.getEmail());
				success = pstmt.executeUpdate() > 0;
				if (success) {
					logger.info("User record was updated successfully!");
				}
				success = pstmt.executeUpdate() > 0;
			}

		}catch(Exception e) {
			success = null;
			logger.error("Error in updateUser()", e);
		}
		return success;
	}

	@Override
	public Boolean createUser(User user) {
		logger.info("Entered createUser()");
		Boolean success = false;
		try(Connection conn = dataSource.getConnection()) {
			try(PreparedStatement pstmt = conn.prepareStatement("insert into user(email, postal_code, first_name, last_name, nickname, password, phone_number, type, share_phone_number) values (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
				pstmt.setString(1, user.getEmail());
				pstmt.setString(2, user.getLocation().getPostalCode()); 
				pstmt.setString(3, user.getFirstName());
				pstmt.setString(4, user.getLastName());
				pstmt.setString(5, user.getNickName());
				pstmt.setString(6, user.getPassword());
				pstmt.setString(7, user.getPhone().getNumber());
				pstmt.setString(8, user.getPhone().getType().toString());
				pstmt.setBoolean(9, user.getPhone().isShare());

				success = pstmt.executeUpdate() > 0;
				if (success) {
					logger.info("A new user was inserted successfully!");
				}
			}
		}catch (Exception e) {
			success = null;
			logger.error("Error in createUser()", e);
		}
		logger.info("Completed createUser()");
		return success;
	}

	@Override
	public Boolean authenticateUser(String email, String password) {
		logger.info("Entered authenticateUser()");
		Boolean success = null;
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select u.first_name, u.last_name, u.nickname, u.phone_number, u.type, u.share_phone_number, l.postal_code, l.city, l.state from User u, Location l where u.email = ? and u.password = ? and u.postal_code=l.postal_code")){
				pstmt.setString(1, email);
				pstmt.setString(2, password);
				try(ResultSet rs = pstmt.executeQuery()){
					success = rs.next();
				}
			}
		} catch (Exception e) {
			success = null;
			logger.error("Error in authenticateUser()", e);
		}
		logger.info("Completed authenticateUser()");
		return success;
	}

	@Override
	public User getUser(String email) {
		logger.info("Entered getUser()");
		User user = null;
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select u.first_name, u.last_name, u.nickname, u.phone_number, u.type, u.share_phone_number, l.postal_code, l.city, l.state from User u, Location l where u.email = ? and u.postal_code=l.postal_code")){
				pstmt.setString(1, email);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						user = new User();
						user.setEmail(email);
						user.setFirstName(rs.getString("first_name"));
						user.setLastName(rs.getString("last_name"));
						user.setNickName(rs.getString("nickname"));

						Phone phone = new Phone();
						phone.setNumber(rs.getString("phone_number"));
						phone.setType(PhoneNumberType.valueOf(rs.getString("type")));
						phone.setShare(rs.getBoolean("share_phone_number"));
						user.setPhone(phone);

						Location location = new Location();
						location.setPostalCode(rs.getString("postal_code"));
						location.setCity(rs.getString("city"));
						location.setState(rs.getString("state"));
						user.setLocation(location);
					}
				}
			}

			if(user!=null) {

				UserStats userStats = new UserStats();
				try(PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(1) unaccepted_swap_count FROM Swap s WHERE s.proposer_email = ? and s.swap_id not in (select swap_id from AcknowledgedSwap)")){
					pstmt.setString(1, email);
					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							userStats.setUnacceptedSwapCount(rs.getInt("unaccepted_swap_count"));
						}
					}
				}
				try(PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(1) unrated_swap_count from Swap s JOIN AcknowledgedSwap a ON s.swap_id = a.swap_id WHERE a.status = 'ACCEPTED' AND (s.proposer_email = ? || s.counterparty_email = ?) && a.swap_id NOT IN (SELECT swap_id from RatedSwap where email = ?)")){
					pstmt.setString(1, email);
					pstmt.setString(2, email);
					pstmt.setString(3, email);
					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							userStats.setUnratedSwapCount(rs.getInt("unrated_swap_count"));
						}
					}
				}
				try(PreparedStatement pstmt = conn.prepareStatement("select case when (select 1 from RatedSwap where email = ?) is null then null else (SELECT avg(rating) rating from RatedSwap where email = ?) end rating")){
					pstmt.setString(1, email);
					pstmt.setString(2, email);
					try(ResultSet rs = pstmt.executeQuery()){
						while(rs.next()) {
							if(rs.getObject("rating")!=null) {
								userStats.setRating(Math.round(rs.getDouble("rating")*100)/100d);
							}
						}
					}
				}

				user.setUserStats(userStats);
			}
		} catch (Exception e) {
			user = null;
			logger.error("Error in getUser()", e);
		}
		logger.info("Completed getUser()");
		return user;
	}

	@Override
	public Boolean checkEmailExists(String email) {
		Boolean success = false;
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select 1 from User where email = ?")){
				pstmt.setString(1, email);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						success = true;
					}
				}
			}
		}catch (Exception e) {
			logger.error("Error in checkEmailExists()", e);
		}
		return success;
	}

	@Override
	public Boolean checkPhoneExists(String phone) {
		Boolean success = false;
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select 1 from User where phone_number = ?")){
				pstmt.setString(1, phone);
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						success = true;
					}
				}
			}
		}catch (Exception e) {
			success = null;
			logger.error("Error in checkPhoneExists()", e);
		}
		return success;
	}

	@Override
	public List<Location> getLocations() {
		List<Location> locations = new ArrayList<>();
		try(Connection conn = dataSource.getConnection()){
			try(PreparedStatement pstmt = conn.prepareStatement("select city, state, postal_code from location")){
				try(ResultSet rs = pstmt.executeQuery()){
					while(rs.next()) {
						Location location = new Location();
						location.setPostalCode(rs.getString("postal_code"));
						location.setCity(rs.getString("city"));
						location.setState(rs.getString("state"));
						locations.add(location);
					}
				}
			}
		}catch (Exception e) {
			locations = null;
			logger.error("Error in getLocations()", e);
		}
		return locations;
	}

	@Override
	public Double getRating(String email) {
		return null;
	}
}
