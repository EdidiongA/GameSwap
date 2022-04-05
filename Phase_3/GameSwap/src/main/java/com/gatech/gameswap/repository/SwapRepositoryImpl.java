package com.gatech.gameswap.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gatech.gameswap.model.Swap;

@Repository
public class SwapRepositoryImpl implements SwapRepository{

	@Autowired
	private DataSource dataSource;
	
	@Override
	public Boolean swapRequest(Swap swap) throws SQLException {
		
		String sql = "INSERT into Swap (proposer_email, counterparty_email, proposed_item_id, counterparty_item_id, proposed_date)\n"
				+ " VALUES ('"+swap.getProposerID()+"','"+swap.getCounterPartyID()+"','"+ swap.getProposerItemID()+"','"+swap.getCounterPartyItemID()+"',CAST(now() As Date))";
		 try(Connection conn = dataSource.getConnection()) {
				PreparedStatement statement = conn.prepareStatement(sql);

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
			    System.out.println("swap requested!");
			    return true;
			}
			conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
	}

	@Override
	public JSONObject proposeSwap(String userID) throws SQLException {
		JSONObject jsonObject = new JSONObject();
	    JSONArray array = new JSONArray();
	    JSONObject record = new JSONObject();
	    try(Connection conn = dataSource.getConnection())  {
			String sql = "((SELECT COUNT(1) unrated_swap_count from Swap s\n"
					+ "JOIN AcknowledgedSwap a\n"
					+ "ON s.swap_id = a.swap_id\n"
					+ "WHERE a.status = 'ACCEPTED'\n"
					+ "AND (s.proposer_email ='"+userID+"' OR s.counterparty_email ='"+userID+"')\n"
					+ "AND a.swap_id NOT IN\n"
					+ "(SELECT swap_id from Ratedswap where email ='"+userID+"')))";
			
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if(resultSet.next()) {
				record.put("unrated_swap_count", resultSet.getInt("unrated_swap_count"));
				array.add(record);
				int swap_unrated = resultSet.getInt("unrated_swap_count");
				if(swap_unrated < 2) {
					String sql1 = "SELECT item.item_id, item.type, item.name, item.condition FROM\n"
							+ "Item\n" 
							+ "LEFT OUTER JOIN\n"
							+ "swap ON (counterparty_item_id = item_id)\n"
							+ "LEFT OUTER JOIN Acknowledgedswap ON (swap.swap_id = Acknowledgedswap.swap_id)\n"
							+ "WHERE (counterparty_email ='"+userID+"' AND Acknowledgedswap.status = 'REJECTED')\n" 
							+ "OR (item.email ='"+userID+"' AND swap.swap_id IS NULL)";
				
					ResultSet result = statement.executeQuery(sql1);
					while(result.next()) {
					record.put("item_id", result.getString("item_id"));
					record.put("type", result.getString("type"));
					record.put("name", result.getString("name"));
					record.put("condition", result.getString("condition"));
					array.add(record);
					}
				}
			}
			jsonObject.put("Propose_item", array);
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
	}

	@Override
	public JSONObject ackPage(String userID) throws SQLException {
		JSONObject jsonObject = new JSONObject();
	    JSONArray array = new JSONArray();
	    JSONObject record = new JSONObject();
	    try(Connection conn = dataSource.getConnection()) {
		 String sql ="SELECT latitude, longitude FROM user JOIN Location on location.postal_code = user.postal_code where email ='"+userID+"'";
		 Statement statement = conn.createStatement();
		 ResultSet rs = statement.executeQuery(sql);
			if(rs.next()) {
				Double latitude = rs.getDouble("latitude");
				Double longitude = rs.getDouble("longitude");
				
				String sql1 = "SELECT Swap.proposed_date, User.nickname proposer, item1.name proposed_item ,item2.name desired_item,\n"
						+ "(SELECT AVG(rating) Rating FROM ratedswap WHERE email=proposer_email GROUP BY email) Rating,( 3958.75 * 2 * atan2(sqrt(sin((radians("+latitude+" - (location.latitude))) / 2) * sin((radians("+latitude+" - (location.latitude))) / 2) *\n"
						+ "cos(radians("+latitude+")) * cos(radians("+latitude+")) *\n"
						+ "sin((radians(("+longitude+")- (location.longitude))) / 2) * sin((radians(("+longitude+")- (location.longitude))) / 2)),\n"
						+ "sqrt((1-sin((radians("+latitude+" - (location.latitude))) / 2) * sin((radians("+latitude+" - (location.latitude))) / 2) *\n"
						+ "cos(radians("+latitude+")) * cos(radians("+latitude+")) *\n"
						+ "sin((radians(("+longitude+")- (location.longitude))) / 2) * sin((radians(("+longitude+")- (location.longitude))) / 2))))) AS distance\n"
						+ "FROM User\n"
						+ "JOIN Swap ON (Swap.proposer_email = User.email)\n"
						+ "LEFT OUTER JOIN Item AS item1 ON (swap.proposed_item_id = item1.item_id)\n"
						+ "LEFT OUTER JOIN Item AS item2 ON (swap.counterparty_item_id = item2.item_id)\n"
						+ "LEFT OUTER JOIN Location ON (User.postal_code = Location.postal_code)\n"
						+ "WHERE swap.swap_id IN\n"
						+ "(SELECT swap_id from swap where counterparty_email = '"+userID+"' AND swap_id NOT IN (SELECT Acknowledgedswap.swap_id from swap JOIN AcknowledgedSwap \n"
						+ "ON swap.swap_id = Acknowledgedswap.swap_id WHERE counterparty_email = '"+userID+"'))";
				ResultSet resultSet = statement.executeQuery(sql1);
				while(resultSet.next()) {
					record.put("proposed_date", resultSet.getString("proposed_date"));
					record.put("proposer", resultSet.getString("proposer"));
					record.put("proposed_item", resultSet.getString("proposed_item"));
					record.put("desired_item", resultSet.getString("desired_item"));
					//record.put("distance", resultSet.getString("distance"));
					record.put("Rating", resultSet.getString("Rating"));
					array.add(record);
				}
				conn.close();
			}
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		jsonObject.put("accept_rejecy_swap_list", array);
		return jsonObject;
	}

	@Override
	public Boolean swapAccept(int proposerItemID, int counterPartyItemID) throws SQLException {
		try(Connection conn = dataSource.getConnection()) {
			String sql = "SELECT swap_id FROM swap\n"
					+ "WHERE proposed_item_id = '"+proposerItemID+"' and counterparty_item_id='"+counterPartyItemID+"'";
			 Statement statement = conn.createStatement();
			 ResultSet rs = statement.executeQuery(sql);
			 if(rs.next()) {
				 String swapID= rs.getString("swap_id");
			 
				 String sql1 = "INSERT INTO acknowledgedswap \n"
						 + "VALUES ( '"+swapID+"', 'ACCEPTED', CAST(now() As Date))";
				 PreparedStatement stat = conn.prepareStatement(sql1);
					int rowsInserted = stat.executeUpdate();
					conn.close();
					if (rowsInserted > 0) {
					    System.out.println("swap accepted!");
					    return true;
					}
			 }
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean swapReject(int proposerItemID, int counterPartyItemID) throws SQLException {
		try(Connection conn = dataSource.getConnection()) {
			String sql = "SELECT swap_id FROM swap\n"
					+ "WHERE proposed_item_id = '"+proposerItemID+"' and counterparty_item_id='"+counterPartyItemID+"'";
			 Statement statement = conn.createStatement();
			 ResultSet rs = statement.executeQuery(sql);
			 if(rs.next()) {
				 String swapID= rs.getString("swap_id");
			 
				 String sql1 = "INSERT INTO acknowledgedswap \n"
						 + "VALUES ( '"+swapID+"', '‘REJECTED’', CAST(now() As Date))";
				 PreparedStatement stat = conn.prepareStatement(sql1);
					int rowsInserted = stat.executeUpdate();
					conn.close();
					if (rowsInserted > 0) {
					    System.out.println("swap rejected!");
					    return true;
					}
			 }
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public JSONObject unRatedSwap(String userID) throws SQLException {
		JSONObject jsonObject = new JSONObject();
	    JSONArray array = new JSONArray();
	    JSONObject record = new JSONObject();
	    try(Connection conn = dataSource.getConnection()) {
		 String sql ="SELECT A.swap_id, A.acknowledged_date AS Acceptance_Date, \n"
		 		+ "(SELECT COUNT(*) FROM Swap where swap_id = A.swap_id AND proposer_email = '"+userID+"') My_Role,\n"
		 		+ "(SELECT name FROM Item where Item_id = S.proposed_item_id) AS Proposed_Item,\n"
		 		+ "(SELECT name FROM Item where Item_id = S.counterparty_item_id) AS Desired_Item,\n"
		 		+ "(SELECT nickname FROM User where (email != '"+userID+"' \n"
		 		+ "AND (email = S.counterparty_email OR email = S.proposer_email))) AS Other_User,\n"
		 		+ "R.rating \n"
		 		+ " FROM Swap AS S \n"
		 		+ " JOIN Acknowledgedswap AS A ON S.swap_id = A.swap_id\n"
		 		+ " LEFT OUTER JOIN Ratedswap AS R ON (R.swap_id = A.swap_id AND R.email = '"+userID+"')\n"
		 		+ " WHERE S.Swap_id\n"
		 		+ "IN\n"
		 		+ "((SELECT Swap.swap_id from Swap\n"
		 		+ "JOIN Acknowledgedswap\n"
		 		+ "ON Swap.swap_id = Acknowledgedswap.swap_id \n"
		 		+ "WHERE Acknowledgedswap.status = 'ACCEPTED' \n"
		 		+ "AND (Swap.counterparty_email = '"+userID+"' OR  Swap.proposer_email = '"+userID+"') \n"
		 		+ "AND Acknowledgedswap.swap_id NOT IN\n"
		 		+ "(SELECT swap_id from Ratedswap where email = '"+userID+"')))\n"
		 		+ "ORDER BY acknowledged_date DESC;\n"
		 		+ "";
		 Statement statement = conn.createStatement();
		 ResultSet resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()) {
				record.put("Acceptance_Date", resultSet.getString("Acceptance_Date"));
				if(resultSet.getInt("My_Role") == 1)
					record.put("My_Role", "Counterparty");
				else if(resultSet.getInt("My_Role") == 0)
					record.put("My_Role", "Prpopser");
				record.put("proposed_item", resultSet.getString("Proposed_Item"));
				record.put("desired_item", resultSet.getString("Desired_Item"));
				//record.put("distance", resultSet.getString("distance"));
				record.put("Other_User", resultSet.getString("Other_User"));
				array.add(record);
			}
			conn.close();
	    
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		jsonObject.put("unrated_swap_list", array);
		return jsonObject;
	}

	@Override
	public Boolean upadteSwapRating(int swapID, String userID, int rating) throws SQLException {
		try(Connection conn = dataSource.getConnection()) {
			String sql = "INSERT into RatedSwap (swap_id,email,rating)\n"
					+ "VALUES ('"+swapID+"','"+userID+"','"+rating+"');";
			
			PreparedStatement stat = conn.prepareStatement(sql);
			int rowsInserted = stat.executeUpdate();
			conn.close();
			if (rowsInserted > 0) {
			    System.out.println("swap rated!");
			    return true;
			}
			 
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		}

	@Override
	public JSONObject swapHistory(String userID) throws SQLException {
		JSONObject jsonObject = new JSONObject();
	    JSONArray array = new JSONArray();
	    JSONObject record = new JSONObject();
	    try(Connection conn = dataSource.getConnection()) {
	    	record.put("My_Role", "Prpopser");
	    	 String sql ="SELECT COUNT(*) Total from AcknowledgedSwap \n"
	    	 		+ "WHERE swap_id\n"
	    	 		+ "IN (SELECT swap_id from Swap WHERE Swap.proposer_email = '"+userID+"')";
	    	 Statement statement = conn.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql);
				
			if(resultSet.next()) {
				record.put("Total", resultSet.getString("Total"));
			}
			
			String sql1 ="SELECT COUNT(*) Accepted from AcknowledgedSwap WHERE swap_id\n"
					+ "IN (SELECT swap_id from SWAP WHERE Swap.proposer_email = '"+userID+"')\n"
					+ "AND status = 'ACCEPTED'";

			 ResultSet rs = statement.executeQuery(sql1);
			if(rs.next()) {
				record.put("Accepted", rs.getInt("Accepted"));
			}
			
			String sql2 ="SELECT COUNT(*) Rejected from AcknowledgedSwap WHERE swap_id\n"
					+ "IN (SELECT swap_id from SWAP WHERE Swap.proposer_email = '"+userID+"')\n"
					+ "AND status = 'REJECTED';\n";

			 ResultSet res = statement.executeQuery(sql2);
			if(res.next()) {
				record.put("Rejected", res.getString("Rejected"));
			}
			
			String sql3 ="SELECT count(*) * 100.0 / (SELECT COUNT(*) Rejected from Swap WHERE Swap.proposer_email = '"+userID+"') Rejected_percentage\n"
					+ "FROM AcknowledgedSwap \n"
					+ "WHERE swap_id\n"
					+ "IN (SELECT swap_id from Swap WHERE Swap.proposer_email = '"+userID+"')\n"
					+ "AND status = 'REJECTED';\n";

			 ResultSet result = statement.executeQuery(sql3);
			if(result.next()) {
				record.put("Rejected_percentage", result.getString("Rejected_percentage"));
			}
			array.add(record);
	    	jsonObject.put("As_Proposer_count", array);
	    	
	    	JSONArray array1 = new JSONArray();
		    JSONObject record1 = new JSONObject();
		    
		    record1.put("My_Role", "Counterparty");
	    	 String sql4 ="SELECT COUNT(*) Total from AcknowledgedSwap \n"
	    	 		+ "WHERE swap_id\n"
	    	 		+ "IN (SELECT swap_id from Swap WHERE Swap.counterparty_email = '"+userID+"')";
			 ResultSet re = statement.executeQuery(sql4);
				
			if(re.next()) {
				record1.put("Total", re.getString("Total"));
			}
			
			String sql5 ="SELECT COUNT(*) Accepted from AcknowledgedSwap WHERE swap_id\n"
					+ "IN (SELECT swap_id from SWAP WHERE Swap.counterparty_email = '"+userID+"')\n"
					+ "AND status = 'ACCEPTED'";

			 ResultSet resul = statement.executeQuery(sql5);
			if(resul.next()) {
				record1.put("Accepted", resul.getString("Accepted"));
			}
			
			String sql6 ="SELECT COUNT(*) Rejected from AcknowledgedSwap WHERE swap_id\n"
					+ "IN (SELECT swap_id from SWAP WHERE Swap.counterparty_email = '"+userID+"')\n"
					+ "AND status = 'REJECTED';\n";

			 ResultSet resu = statement.executeQuery(sql6);
			if(resu.next()) {
				record1.put("Rejected", resu.getString("Rejected"));
			}
			
			String sql7 ="SELECT count(*) * 100.0 / (SELECT COUNT(*)  Rejected from Swap WHERE Swap.counterparty_email = '"+userID+"') Rejected_percentage\n"
					+ "FROM AcknowledgedSwap \n"
					+ "WHERE swap_id\n"
					+ "IN (SELECT swap_id from Swap WHERE Swap.counterparty_email = '"+userID+"')\n"
					+ "AND status = 'REJECTED';\n";

			 ResultSet results = statement.executeQuery(sql7);
			if(results.next()) {
				record1.put("Rejected_percentage", results.getString("Rejected_percentage"));
			}
			array1.add(record1);
	    	jsonObject.put("As_Counterparty_count", array1);
	    	
	    	JSONArray array2 = new JSONArray();
		    JSONObject record2 = new JSONObject();
		 String sql8 ="SELECT S.proposed_date, A.acknowledged_date AS Accept_Reject_Date, A.status,\n"
		 		+ "(SELECT name FROM Item WHERE Item_id = S.proposed_item_id) AS Proposed_Item,\n"
		 		+ "(SELECT name FROM Item WHERE Item_id = S.counterparty_item_id) AS Desired_Item,\n"
		 		+ "(SELECT COUNT(*) FROM Swap where swap_id = A.swap_id AND proposer_email = '"+userID+"') My_Role,\n"
		 		+ "(SELECT nickname FROM User WHERE (email != '"+userID+"' \n"
		 		+ "AND (email = S.counterparty_email OR email = S.proposer_email))) AS Other_User,\n"
		 		+ "R.rating FROM Swap AS S\n"
		 		+ "JOIN AcknowledgedSwap AS A ON S.swap_id = A.swap_id\n"
		 		+ "LEFT OUTER JOIN RatedSwap AS R ON A.swap_id = R.swap_id AND R.email='"+userID+"'\n"
		 		+ "WHERE (S.counterparty_email = '"+userID+"' OR  S.proposer_email = '"+userID+"')\n"
		 		+ "ORDER BY A.acknowledged_date DESC, S.proposed_date ASC;\n";

		 ResultSet resultSe = statement.executeQuery(sql8);
			
			while(resultSe.next()) {
				record2.put("proposed_date", resultSe.getDate("proposed_date"));
				record2.put("acknowledged_date", resultSe.getDate("Accept_Reject_Date"));
				record2.put("status", resultSe.getString("status"));
				if(resultSe.getInt("My_Role") == 1)
					record2.put("My_Role", "Counterparty");
				else if(resultSe.getInt("My_Role") == 0)
					record2.put("My_Role", "Prpopser");
				record2.put("proposed_item", resultSe.getString("Proposed_Item"));
				record2.put("desired_item", resultSe.getString("Desired_Item"));
				//record.put("distance", resultSet.getString("distance"));
				record2.put("Other_User", resultSe.getString("Other_User"));
				array2.add(record2);
			}
			jsonObject.put("Swap_history", array2);
			conn.close();
	    
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		//jsonObject.put("unrated_swap_list", array2);
		return jsonObject;
	}

	@Override
	public JSONObject swapDetail(String userID,int swapID) throws SQLException {
		JSONObject jsonObject = new JSONObject();
	    JSONArray array = new JSONArray();
	    JSONObject record = new JSONObject();
	    try(Connection conn = dataSource.getConnection()) {
	    	
	    //swap details
		String sql = "SELECT S.proposed_date, A.acknowledged_date, A.status, R.rating,\n"
				+ "(SELECT COUNT(*) FROM Swap where swap_id = A.swap_id AND proposer_email = '"+userID+"') My_Role \n"
				+ "FROM Swap AS S\n"
				+ "JOIN AcknowledgedSwap AS A ON S.swap_id = A.swap_id\n"
				+ "LEFT OUTER JOIN RatedSwap AS R ON (R.swap_id = A.swap_id AND R.email ='"+userID+"')\n"
				+ "WHERE s.swap_id = '"+swapID+"'";
		Statement statement = conn.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		if(resultSet.next()) {
			record.put("proposed_date", resultSet.getDate("proposed_date"));
			record.put("acknowledged_date", resultSet.getDate("acknowledged_date"));
			record.put("status", resultSet.getString("status"));
			record.put("rating", resultSet.getString("rating"));
			if(resultSet.getInt("My_Role") == 1)
				record.put("My_Role", "Proposer");
			else if(resultSet.getInt("My_Role") == 0)
				record.put("My_Role", "Counterparty");
			
			jsonObject.put("swap_details", record);
		}
			//user details

		    JSONObject record1 = new JSONObject();
		    
		    String sql2 ="SELECT latitude, longitude FROM user JOIN Location on location.postal_code = user.postal_code where email ='"+userID+"'";
			 ResultSet rs = statement.executeQuery(sql2);
			 
			 
			 if(rs.next()) {
				 Double latitude = rs.getDouble("latitude");
				 Double longitude = rs.getDouble("longitude");
				 String sql1 = "SELECT User.first_name, User.nickname, User.email email, P.phone_number, P.type, P.share_phone_number, ( 3958.75 * 2 * atan2(sqrt(sin((radians("+latitude+" - (location.latitude))) / 2) * sin((radians("+latitude+" - (location.latitude))) / 2) *\n"
							+ "cos(radians("+latitude+")) * cos(radians("+latitude+")) *\n"
							+ "sin((radians(("+longitude+")- (location.longitude))) / 2) * sin((radians(("+longitude+")- (location.longitude))) / 2)),\n"
							+ "sqrt((1-sin((radians("+latitude+" - (location.latitude))) / 2) * sin((radians("+latitude+" - (location.latitude))) / 2) *\n"
							+ "cos(radians("+latitude+")) * cos(radians("+latitude+")) *\n"
							+ "sin((radians(("+longitude+")- (location.longitude))) / 2) * sin((radians(("+longitude+")- (location.longitude))) / 2))))) AS distance FROM User\n"
							+ "JOIN Swap ON (Swap.counterparty_email = User.email \n"
							+ "OR Swap.proposer_email = User.email)\n"
							+ "JOIN PhoneNumber AS P ON P.email = User.email\n"
							+ "JOIN Location ON (User.postal_code = Location.postal_code)\n"
							+ "WHERE user.email NOT IN ('"+userID+"') and Swap.swap_id = '"+swapID+"'";

					ResultSet rset = statement.executeQuery(sql1);
					if(rset.next()) {
						record1.put("first_name", rset.getString("first_name"));
						record1.put("nickname", rset.getString("nickname"));
						record1.put("email", rset.getString("email"));
						record1.put("phone_number", rset.getString("phone_number"));
						record1.put("type", rset.getString("type"));
						record1.put("distance", rset.getString("distance"));
						if(rset.getInt("share_phone_number") == 1)
							record1.put("share_phone_number", "Yes");
						else if(rset.getInt("share_phone_number") == 0)
							record1.put("share_phone_number", "No");

				 }
				jsonObject.put("User_details", record1);
			 }
		
			 
			//proposed Item

			    JSONObject record2 = new JSONObject();
			String sql1 = "SELECT Item.item_id,Item.name,Item.type,Item.condition,Item.description from Item\n"
					+ "JOIN Swap ON Swap.proposed_item_id = Item.item_id \n"
					+ "WHERE Swap.swap_id = '"+swapID+"'";

			ResultSet rslt = statement.executeQuery(sql1);
			if(rslt.next()) {
				record2.put("item_id", rslt.getInt("item_id"));
				record2.put("name", rslt.getString("name"));
				record2.put("type", rslt.getString("type"));
				record2.put("condition", rslt.getString("condition"));
			
				if(rslt.getString("description") != null)
					record2.put("description", rslt.getString("description"));
				

				jsonObject.put("Proposed_Item", record2);
			
		}
			
			//DesiredItem Item
			
		    JSONObject record3 = new JSONObject();
		String sql3 = "SELECT Item.item_id,Item.name,Item.type,Item.condition,Item.description from Item\n"
				+ "JOIN Swap ON Swap.counterparty_item_id = Item.item_id \n"
				+ "WHERE Swap.swap_id = '"+swapID+"'";

		ResultSet rsltSt = statement.executeQuery(sql3);
		if(rsltSt.next()) {
			record3.put("item_id", rsltSt.getInt("item_id"));
			record3.put("name", rsltSt.getString("name"));
			record3.put("type", rsltSt.getString("type"));
			record3.put("condition", rsltSt.getString("condition"));
		
			if(rsltSt.getString("description") != null)
				record3.put("description", rsltSt.getString("description"));
			
			jsonObject.put("Desired_item", record3);
		
		}
		conn.close();
		
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
