package com.gatech.gameswap.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class GameTypeMetadata {
	
	public enum VideoGameMedia {
		OpticalDisc, GameCard, Cartridge
	}
	
	public enum ComputerGamePlatform {
		Linux, Macos, Windows
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String videoGamePlatform;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private VideoGameMedia videoGameMedia;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ComputerGamePlatform computerGamePlatform;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer jigsawPuzzlePieceCount;
	
	public String getVideoGamePlatform() {
		return videoGamePlatform;
	}
	public void setVideoGamePlatform(String videoGamePlatform) {
		this.videoGamePlatform = videoGamePlatform;
	}
	public VideoGameMedia getVideoGameMedia() {
		return videoGameMedia;
	}
	public void setVideoGameMedia(VideoGameMedia videoGameMedia) {
		this.videoGameMedia = videoGameMedia;
	}
	public ComputerGamePlatform getComputerGamePlatform() {
		return computerGamePlatform;
	}
	public void setComputerGamePlatform(ComputerGamePlatform computerGamePlatform) {
		this.computerGamePlatform = computerGamePlatform;
	}
	public Integer getJigsawPuzzlePieceCount() {
		return jigsawPuzzlePieceCount;
	}
	public void setJigsawPuzzlePieceCount(Integer jigsawPuzzlePieceCount) {
		this.jigsawPuzzlePieceCount = jigsawPuzzlePieceCount;
	}
	
}
