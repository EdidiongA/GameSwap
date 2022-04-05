package com.gatech.gameswap.service;

import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gatech.gameswap.model.Swap;
import com.gatech.gameswap.repository.SwapRepository;

@Service
public class SwapServiceImpl implements SwapService{
	
	@Autowired
	SwapRepository swapRepository;
	
	@Override
	public Boolean swapRequest(Swap swap) throws SQLException{
		return swapRepository.swapRequest(swap);
		
	}

	@Override
	public JSONObject proposeSwap(String userID) throws SQLException {
		return swapRepository.proposeSwap(userID);
	}

	@Override
	public JSONObject ackPage(String userID) throws SQLException {
		return swapRepository.ackPage(userID);
	}

	@Override
	public Boolean swapAccept(int proposerItemID, int counterPartyItemID) throws SQLException {
		return swapRepository.swapAccept(proposerItemID, counterPartyItemID);
	}

	@Override
	public Boolean swapReject(int proposerItemID, int counterPartyItemID) throws SQLException {
		return swapRepository.swapReject(proposerItemID, counterPartyItemID);
	}

	@Override
	public JSONObject unRatedSwap(String userID) throws SQLException {
		return swapRepository.unRatedSwap(userID);
	}

	@Override
	public Boolean upadteSwapRating(int swapID, String userID, int rating) throws SQLException {
		return swapRepository.upadteSwapRating(swapID, userID, rating);
	}

	@Override
	public JSONObject swapHistory(String userID) throws SQLException {
		return swapRepository.swapHistory(userID);
	}

	@Override
	public JSONObject swapDetail(String userID,int swapID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
