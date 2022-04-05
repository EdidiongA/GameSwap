package com.gatech.gameswap.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gatech.gameswap.service.ItemService;

@RestController
@RequestMapping(path = "/item")
public class ItemController {
	
	@Autowired
	ItemService itemService;
	
	@GetMapping(path= "/viewitem")
	public JSONObject viewItem(@RequestParam String userID, @RequestParam int itemID){
	
		JSONObject itemDet = null;
		//Boolean success = false;
		try{
			itemDet = itemService.itemDetail(userID,itemID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return itemDet;
	}

}
