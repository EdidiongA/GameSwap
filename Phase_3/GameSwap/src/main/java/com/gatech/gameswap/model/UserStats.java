package com.gatech.gameswap.model;

public class UserStats {
	private Double rating;
	private Integer unacceptedSwapCount;
	private Integer unratedSwapCount;
	@Override
	public String toString() {
		return "UserStats [rating=" + rating + ", unacceptedSwapCount=" + unacceptedSwapCount + ", unratedSwapCount="
				+ unratedSwapCount + "]";
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public Integer getUnacceptedSwapCount() {
		return unacceptedSwapCount;
	}
	public void setUnacceptedSwapCount(Integer unacceptedSwapCount) {
		this.unacceptedSwapCount = unacceptedSwapCount;
	}
	public Integer getUnratedSwapCount() {
		return unratedSwapCount;
	}
	public void setUnratedSwapCount(Integer unratedSwapCount) {
		this.unratedSwapCount = unratedSwapCount;
	}
}
