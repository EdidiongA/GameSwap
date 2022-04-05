package com.gatech.gameswap.service;

import java.sql.SQLException;

import org.json.simple.JSONObject;

public interface ItemService {

	JSONObject itemDetail(String userID,int itemID) throws SQLException;
}
