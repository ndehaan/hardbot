package util.ECWeather;

import java.util.Date;

public class ECObservation {
	
	private Date date;
	private String condition;
	private double temperature;
	private double visibility;
	private double pressure;
	private String pressureTendency;
	private int humidity;
	private int windSpeed;
	private String windDirection;
	private int windGust;
	private int windBearing;
	
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public int getHumidity() {
		return humidity;
	}
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	public int getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}
	public String getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	public double getVisibility() {
		return visibility;
	}
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	public double getPressure() {
		return pressure;
	}
	public void setWindGust(int windGust) {
		this.windGust = windGust;
	}
	public int getWindGust() {
		return windGust;
	}
	public void setWindBearing(int windBearing) {
		this.windBearing = windBearing;
	}
	public int getWindBearing() {
		return windBearing;
	}
	public void setPressureTendency(String pressureTendency) {
		this.pressureTendency = pressureTendency;
	}
	public String getPressureTendency() {
		return pressureTendency;
	}

}
