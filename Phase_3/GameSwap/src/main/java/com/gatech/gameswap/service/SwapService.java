package com.gatech.gameswap.service;

import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import com.gatech.gameswap.model.Swap;

public interface SwapService {
	Boolean swapRequest(Swap swap) throws SQLException;
	JSONObject proposeSwap(String userID) throws SQLException;
	JSONObject ackPage(String userID) throws SQLException;
	Boolean swapAccept(int proposerItemID,int counterPartyItemID) throws SQLException;
	Boolean swapReject(int proposerItemID,int counterPartyItemID) throws SQLException;
	JSONObject unRatedSwap(String userID) throws SQLException;
	Boolean upadteSwapRating(int swapID,String userID, int rating) throws SQLException;
	JSONObject swapHistory(String userID) throws SQLException;
	JSONObject swapDetail(String userID,int swapID) throws SQLException;
}
