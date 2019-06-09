package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class ChocolateDonationRequest {
	@NotNull
	private int chocolates;

	public int getChocolates() {
		return chocolates;
	}

	public void setChocolates(int chocolates) {
		this.chocolates = chocolates;
	}
}
