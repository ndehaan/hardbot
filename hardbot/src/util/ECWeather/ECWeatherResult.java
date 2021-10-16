package util.ECWeather;

import java.util.ArrayList;
import java.util.Date;


public class ECWeatherResult {

	private ECObservation myObservation = new ECObservation();
	private Date forecastDate;
	private ArrayList<ECForecast> myForecasts = new ArrayList<ECForecast>();
	private String location;
	private String city;
	private String province;
	
	public void setObservation(ECObservation observation) {
		this.myObservation = observation;
	}
	public ECObservation getObservation() {
		return myObservation;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity() {
		return city;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvince() {
		return province;
	}

	public boolean addForecast(ECForecast fc){
		return myForecasts.add(fc);
	}
	public ArrayList<ECForecast> getForecasts(){
		return myForecasts;
	}
	public void setForecastDate(Date forecastDate) {
		this.forecastDate = forecastDate;
	}
	public Date getForecastDate() {
		return forecastDate;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocation() {
		return location;
	}
}
