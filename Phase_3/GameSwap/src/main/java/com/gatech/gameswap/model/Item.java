package com.gatech.gameswap.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Item {

	public enum Condition {
		Mint, LikeNew, LightlyUsed, ModeratelyUsed, HeavilyUsed, Damaged
	}
	
	public enum GameType {
		BoardGame, CardGame, VideoGame, ComputerGame, JigsawPuzzle
	}
	
	public enum ItemSearchKey {
		  Keyword, MyPostalCode, Miles, PostalCode
	}
	
	
	private Long id;
	private String name;
	private String description;
	private Condition condition;
	private GameType gameType;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private GameTypeMetadata gameTypeMetadata;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ItemOwner itemOwner;
	
	public ItemOwner getItemOwner() {
		return itemOwner;
	}

	public void setItemOwner(ItemOwner itemOwner) {
		this.itemOwner = itemOwner;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", description=" + description + ", condition=" + condition
				+ ", gameType=" + gameType + ", gameTypeMetadata=" + gameTypeMetadata + "]";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Condition getCondition() {
		return condition;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	public GameType getGameType() {
		return gameType;
	}
	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
	public GameTypeMetadata getGameTypeMetadata() {
		return gameTypeMetadata;
	}
	public void setGameTypeMetadata(GameTypeMetadata gameTypeMetadata) {
		this.gameTypeMetadata = gameTypeMetadata;
	}
		

}
