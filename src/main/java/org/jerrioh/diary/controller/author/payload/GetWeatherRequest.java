package org.jerrioh.diary.controller.author.payload;

import javax.validation.constraints.NotNull;

public class GetWeatherRequest {
	@NotNull
	private String country;
	@NotNull
	private String city;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
