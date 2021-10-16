package util;

public class Observation extends ForecastResult {

	private int wind;
	private String windDirection;
	private int windGust;
	private int windBearing;
	private double visibility;
	private int humidity;
	private double pressure;
	private String pressureTendency;
	private String forecastDate;

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getWind() {
		return wind;
	}

	public void setWind(int wind) {
		this.wind = wind;
	}

	public String toString(){
		return getDay()+getCondition()+", "+getHigh()+"°C, Wind: "+this.getWindDirection()+" at "+getWind()+" km/h, Humidity: "+getHumidity()+"%.";
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public String getPressureTendency() {
		return pressureTendency;
	}

	public void setPressureTendency(String pressureTendency) {
		this.pressureTendency = pressureTendency;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getForecastDate() {
		return forecastDate;
	}

	public void setForecastDate(String forecastDate) {
		this.forecastDate = forecastDate;
	}

	public double getVisibility() {
		return visibility;
	}

	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}

	public int getWindBearing() {
		return windBearing;
	}

	public void setWindBearing(int windBearing) {
		this.windBearing = windBearing;
	}

	public int getWindGust() {
		return windGust;
	}

	public void setWindGust(int windGust) {
		this.windGust = windGust;
	}

}
