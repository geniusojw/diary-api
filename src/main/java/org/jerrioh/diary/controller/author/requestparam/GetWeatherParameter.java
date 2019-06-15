package org.jerrioh.diary.controller.author.requestparam;

import javax.validation.constraints.NotNull;

public class GetWeatherParameter {
	@NotNull
	private String city;
	@NotNull
	private String country;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
