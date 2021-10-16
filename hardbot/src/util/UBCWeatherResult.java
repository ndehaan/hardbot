package util;

import java.util.Date;

public class UBCWeatherResult {
	private Date date;
	private double insideTemp;
	private double outsideTemp;
	private double outsideHumidity;
	private double windSpeed;
	private String windDir;
	private double barometer;
	private double rain;

	public UBCWeatherResult(Date date, double insideTemp, double outsideTemp,
			double outsideHumidity, double windSpeed, String windDir,
			double barometer, double rain) {
		setDate(date);
		this.setInsideTemp(insideTemp);
		this.setOutsideTemp(outsideTemp);
		this.setOutsideHumidity(outsideHumidity);
		this.setWindSpeed(windSpeed);
		this.setWindDir(windDir);
		this.setBarometer(barometer);
		this.setRain(rain);
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the insideTemp
	 */
	public double getInsideTemp() {
		return insideTemp;
	}

	/**
	 * @param insideTemp
	 *            the insideTemp to set
	 */
	public void setInsideTemp(double insideTemp) {
		this.insideTemp = insideTemp;
	}

	/**
	 * @return the outsideTemp
	 */
	public double getOutsideTemp() {
		return outsideTemp;
	}

	/**
	 * @param outsideTemp
	 *            the outsideTemp to set
	 */
	public void setOutsideTemp(double outsideTemp) {
		this.outsideTemp = outsideTemp;
	}

	/**
	 * @return the outsideHumidity
	 */
	public double getOutsideHumidity() {
		return outsideHumidity;
	}

	/**
	 * @param outsideHumidity
	 *            the outsideHumidity to set
	 */
	public void setOutsideHumidity(double outsideHumidity) {
		this.outsideHumidity = outsideHumidity;
	}

	/**
	 * @return the windSpeed
	 */
	public double getWindSpeed() {
		return windSpeed;
	}

	/**
	 * @param windSpeed
	 *            the windSpeed to set
	 */
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	/**
	 * @return the windDir
	 */
	public String getWindDir() {
		return windDir;
	}

	/**
	 * @param windDir
	 *            the windDir to set
	 */
	public void setWindDir(String windDir) {
		this.windDir = windDir;
	}

	/**
	 * @return the barometer
	 */
	public double getBarometer() {
		return barometer;
	}

	/**
	 * @param barometer
	 *            the barometer to set
	 */
	public void setBarometer(double barometer) {
		this.barometer = barometer;
	}

	/**
	 * @return the rain
	 */
	public double getRain() {
		return rain;
	}

	/**
	 * @param rain
	 *            the rain to set
	 */
	public void setRain(double rain) {
		this.rain = rain;
	}
	
	public String toString(){
		return Double.toString(getBarometer());
	}
}
