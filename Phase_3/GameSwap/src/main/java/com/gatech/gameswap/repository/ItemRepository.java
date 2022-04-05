package com.gatech.gameswap.repository;

import java.sql.SQLException;

import org.json.simple.JSONObject;

public interface ItemRepository {

	JSONObject itemDetail(String userID,int itemID) throws SQLException;
}
