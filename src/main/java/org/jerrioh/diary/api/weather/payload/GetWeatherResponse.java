package org.jerrioh.diary.api.weather.payload;

import java.util.List;

public class GetWeatherResponse {

	public static class Weather {
		private int id;
		private String main;
		private String description;
		private String icon;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getMain() {
			return main;
		}
		public void setMain(String main) {
			this.main = main;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
	}
	
	private List<Weather> weather;

	public List<Weather> getWeather() {
		return weather;
	}

	public void setWeather(List<Weather> weather) {
		this.weather = weather;
	}
}
