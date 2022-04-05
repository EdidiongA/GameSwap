package com.gatech.gameswap.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl implements ItemRepository{

	@Autowired
	private DataSource dataSource;
	
	@Override
	public JSONObject itemDetail(String userID, int itemID) throws SQLException {
		JSONObject jsonObject = new JSONObject();
	    JSONArray array = new JSONArray();
	    JSONObject record = new JSONObject();
	    try(Connection conn = dataSource.getConnection())  {
		String sql = "SELECT item.item_id, item.type, item.name, item.condition,item.email FROM item WHERE item.item_id ='"+itemID+"'";
		Statement statement = conn.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		if(resultSet.next()) {
			record.put("itemID", resultSet.getInt("item_id"));
			record.put("type", resultSet.getString("type"));
			record.put("name", resultSet.getString("name"));
			record.put("condition", resultSet.getString("condition"));
			String email = resultSet.getString("email");
			
			if(resultSet.getString("type").equals("VideoGame")) {
				String sql1 = "SELECT platform_name,media FROM videogame WHERE item_id ='"+itemID+"'";
				ResultSet result = statement.executeQuery(sql1);
				if(result.next()) {
					record.put("media", result.getString("media"));
					record.put("platform", result.getString("platform_name"));
				}
			}
			else if(resultSet.getString("type").equals("ComputerGame")) {
				String sql1 = "SELECT platform FROM computergame WHERE item_id ='"+itemID+"'";
				ResultSet result = statement.executeQuery(sql1);
				if(result.next()) 
					record.put("platform", result.getString("platform"));
			}
			else if(resultSet.getString("type").equals("JigsawPuzzle")) {
				String sql1 = "SELECT piece_count FROM JigsawPuzzle WHERE item_id ='"+itemID+"'";
				ResultSet result = statement.executeQuery(sql1);
				if(result.next()) 
					record.put("piece_count", resultSet.getString("piece_count"));
			}
			 String sql2 ="SELECT latitude, longitude FROM user JOIN Location on location.postal_code = user.postal_code where email ='"+userID+"'";
			 ResultSet rs = statement.executeQuery(sql2);
			 
			 
			 if(rs.next()) {
				 Double latitude = rs.getDouble("latitude");
				 Double longitude = rs.getDouble("longitude");
				 String sql3 = "SELECT user.nickname, AVG(ratedswap.rating) Rating, location.city, location.state, location.postal_code, ( 3958.75 * 2 * atan2(sqrt(sin((radians("+latitude+" - (location.latitude))) / 2) * sin((radians("+latitude+" - (location.latitude))) / 2) *\n"
							+ "cos(radians("+latitude+")) * cos(radians("+latitude+")) *\n"
							+ "sin((radians(("+longitude+")- (location.longitude))) / 2) * sin((radians(("+longitude+")- (location.longitude))) / 2)),\n"
							+ "sqrt((1-sin((radians("+latitude+" - (location.latitude))) / 2) * sin((radians("+latitude+" - (location.latitude))) / 2) *\n"
							+ "cos(radians("+latitude+")) * cos(radians("+latitude+")) *\n"
							+ "sin((radians(("+longitude+")- (location.longitude))) / 2) * sin((radians(("+longitude+")- (location.longitude))) / 2))))) AS distance\n"
							+ "from location\n"
							+ "JOIN user ON user.postal_code = location.postal_code\n"
							+ "LEFT OUTER JOIN\n"
							+ "Ratedswap  ON ratedswap.email = user.email WHERE user.email = '"+email+"'";
				 ResultSet res = statement.executeQuery(sql3);
				 if(res.next()) {
					record.put("nickname", res.getString("nickname"));
					record.put("Rating", res.getString("Rating"));
					record.put("city", res.getString("city"));
					record.put("state", res.getString("state"));
					record.put("postalcode", res.getString("postal_code"));
					record.put("distance", res.getString("distance"));
				 }
				 
			 }
		}
		conn.close();
		array.add(record);
		jsonObject.put("Item_details", array);
		
	    } catch (SQLException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
