package util;

import java.util.ArrayList;


public class WeatherResult {

	private String city;
	private Observation current = new Observation();
	private ArrayList<ForecastResult> forecastList = new ArrayList<ForecastResult>();

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Observation getCurrent() {
		return current;
	}

	public ArrayList<ForecastResult> getForecastList() {
		return forecastList;
	}

	public void addForecast(ForecastResult forecastResult) {
		forecastList.add(forecastResult);
	}
}
