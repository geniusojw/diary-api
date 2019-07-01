package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class BuyPostItRequest {
	@NotNull
	private int price;

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
