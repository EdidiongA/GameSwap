package com.gatech.gameswap.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gatech.gameswap.model.Swap;
import com.gatech.gameswap.service.SwapService;

@RestController
@RequestMapping(path = "/swap")
public class SwapController {
	
	@Autowired
	SwapService swapService;
	
	
	
	@GetMapping(path= "/proposeswap")
	public JSONObject proposeSwap(@RequestParam String userID){
	
		JSONObject userItemList = null;
		//Boolean success = false;
		try{
			userItemList = swapService.proposeSwap(userID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return userItemList;
	}
	
	@PostMapping(path= "/confirmswap")
	public ResponseEntity<Integer> confirmSwap(@RequestBody Swap swap){
	
		
		//Boolean success = false;
		try{
			Boolean swapStatus = swapService.swapRequest(swap);
			
			if(swapStatus)
				return new ResponseEntity<Integer>(HttpStatus.CREATED);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
	}

	
	@GetMapping(path= "/acceptrejectswap")
	public JSONObject swapAck(@RequestParam String userID){
	
		JSONObject aclList = null;
		//Boolean success = false;
		try{
			aclList = swapService.ackPage(userID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return aclList;
	}
	
	
	@PostMapping(path= "/accept")
	public ResponseEntity<Integer> acceptSwap(@RequestParam int proposerItemID, @RequestParam int counterPartyItemID){
	
		try{
			Boolean acceptStatus = swapService.swapAccept(proposerItemID,counterPartyItemID);
			
			if(acceptStatus)
				return new ResponseEntity<Integer>(HttpStatus.ACCEPTED);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(path= "/reject")
	public ResponseEntity<Integer> rejectSwap(@RequestParam int proposerItemID, @RequestParam int counterPartyItemID){
	
		try{
			Boolean acceptStatus = swapService.swapReject(proposerItemID,counterPartyItemID);
			
			if(acceptStatus)
				return new ResponseEntity<Integer>(HttpStatus.ACCEPTED);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping(path= "/unratedswap")
	public JSONObject unRatedSwapList(@RequestParam String userID){
	
		JSONObject unRatedList = null;
		//Boolean success = false;
		try{
			unRatedList = swapService.unRatedSwap(userID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return unRatedList;
	}
	
	@PostMapping(path= "/ratingupdate")
	public ResponseEntity<Integer> updateRting(@RequestParam int swapID, @RequestParam String userID, @RequestParam int rating){
	
		try{
			Boolean acceptStatus = swapService.upadteSwapRating(swapID,userID,rating);
			
			if(acceptStatus)
				return new ResponseEntity<Integer>(HttpStatus.ACCEPTED);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping(path= "/swaphistory")
	public JSONObject viewSwapHistory(@RequestParam String userID){
	
		JSONObject swapHistoryList = null;
		//Boolean success = false;
		try{
			swapHistoryList = swapService.swapHistory(userID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return swapHistoryList;
	}
	
	@GetMapping(path= "/swapdetails")
	public JSONObject viewSwapDetails(@RequestParam String userID,@RequestParam int swapID){
	
		JSONObject swapdetail = null;
		//Boolean success = false;
		try{
			swapdetail = swapService.swapDetail(userID,swapID);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return swapdetail;
	}
}
