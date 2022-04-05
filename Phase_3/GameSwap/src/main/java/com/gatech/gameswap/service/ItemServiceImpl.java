package com.gatech.gameswap.service;

import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gatech.gameswap.repository.ItemRepository;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	ItemRepository itemRepo;
	@Override
	public JSONObject itemDetail(String userID, int itemID) throws SQLException {
		return itemRepo.itemDetail(userID, itemID);
	}

}
