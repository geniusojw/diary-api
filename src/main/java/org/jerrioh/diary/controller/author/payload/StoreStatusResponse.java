package org.jerrioh.diary.controller.author.payload;

import java.util.Map;

public class StoreStatusResponse {
	private int chocolates;
	private Map<String, Integer> priceMap;

	public int getChocolates() {
		return chocolates;
	}
	public void setChocolates(int chocolates) {
		this.chocolates = chocolates;
	}
	public Map<String, Integer> getPriceMap() {
		return priceMap;
	}
	public void setPriceMap(Map<String, Integer> priceMap) {
		this.priceMap = priceMap;
	}
}
