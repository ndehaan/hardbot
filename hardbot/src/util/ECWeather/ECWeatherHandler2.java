package util.ECWeather;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ECWeatherHandler2 extends DefaultHandler {

	private static SimpleDateFormat inputFormatter = new SimpleDateFormat(
			"yyyyMMddHHmmssz");

	private boolean provinceTag, locationTag, cityTag, obsTimeTag,
			forecastTimeTag, timeStamp, currentConditions, condition, high,
			low, pressure, visibility, humidity, wind, speed, gust, direction,
			bearing = false;

	private boolean forecast, period, textSummary = false;
	private int textSummaryCount = 0;

	private ECWeatherResult myWeatherResult = new ECWeatherResult();
	private ECForecast currentForecast;

	public ECWeatherHandler2() {
	}

	public ECWeatherResult getWeatherResult() {
		return myWeatherResult;
	}

	/**
	 * Gets called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		// CURRENT RESULT SECTION
		if (qName.equalsIgnoreCase("province"))
			provinceTag = true;
		else if (qName.equalsIgnoreCase("name")
				&& (atts.getQName(0) != null && atts.getQName(0)
						.equalsIgnoreCase("code")))
			cityTag = true;
		else if (qName.equalsIgnoreCase("station")
				&& (atts.getQName(0) != null && atts.getQName(0)
						.equalsIgnoreCase("code")))
			locationTag = true;
		else if (qName.equalsIgnoreCase("dateTime") && atts.getValue(0) != null
				&& atts.getValue(0).equalsIgnoreCase("observation")
				&& atts.getValue(2) != null
				&& atts.getValue(2).equalsIgnoreCase("0"))
			obsTimeTag = true;
		else if (qName.equalsIgnoreCase("timeStamp"))
			timeStamp = true;
		else if (qName.equalsIgnoreCase("dateTime") && atts.getValue(0) != null
				&& atts.getValue(0).equalsIgnoreCase("forecastIssue")
				&& atts.getValue(2) != null
				&& atts.getValue(2).equalsIgnoreCase("0"))
			forecastTimeTag = true;
		else if (qName.equalsIgnoreCase("currentConditions"))
			currentConditions = true;
		else if (qName.equalsIgnoreCase("condition"))
			condition = true;
		else if (qName.equalsIgnoreCase("temperature"))
			high = true;
		else if (qName.equalsIgnoreCase("pressure")) {
			pressure = true;
			if (currentConditions)
				myWeatherResult.getObservation().setPressureTendency(
						atts.getValue("tendency"));
		} else if (qName.equalsIgnoreCase("visibility"))
			visibility = true;
		else if (qName.equalsIgnoreCase("relativeHumidity"))
			humidity = true;
		else if (qName.equalsIgnoreCase("wind"))
			wind = true;
		else if (qName.equalsIgnoreCase("speed"))
			speed = true;
		else if (qName.equalsIgnoreCase("gust"))
			gust = true;
		else if (qName.equalsIgnoreCase("direction"))
			direction = true;
		else if (qName.equalsIgnoreCase("bearing"))
			bearing = true;
		// FORECAST SECTION
		else if (qName.equalsIgnoreCase("forecast")) {
			forecast = true;
			currentForecast = new ECForecast();
			textSummaryCount = 0;
		} else if (qName.equalsIgnoreCase("period"))
			period = true;
		else if (qName.equalsIgnoreCase("textSummary"))
			textSummary = true;

	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		// CURRENT RESULT SECTION
		if (qName.equalsIgnoreCase("province"))
			provinceTag = false;
		else if (qName.equalsIgnoreCase("name"))
			cityTag = false;
		else if (qName.equalsIgnoreCase("station"))
			locationTag = false;
		else if (qName.equalsIgnoreCase("dateTime"))
			obsTimeTag = false;
		else if (qName.equalsIgnoreCase("timeStamp")) {
			timeStamp = false;
			obsTimeTag = false;
			forecastTimeTag = false;
		} else if (qName.equalsIgnoreCase("currentConditions"))
			currentConditions = false;
		else if (qName.equalsIgnoreCase("condition"))
			condition = false;
		else if (qName.equalsIgnoreCase("temperature"))
			high = false;
		else if (qName.equalsIgnoreCase("pressure"))
			pressure = false;
		else if (qName.equalsIgnoreCase("visibility"))
			visibility = false;
		else if (qName.equalsIgnoreCase("relativeHumidity"))
			humidity = false;
		else if (qName.equalsIgnoreCase("wind"))
			wind = false;
		else if (qName.equalsIgnoreCase("speed"))
			speed = false;
		else if (qName.equalsIgnoreCase("gust"))
			gust = false;
		else if (qName.equalsIgnoreCase("direction"))
			direction = false;
		else if (qName.equalsIgnoreCase("bearing"))
			bearing = false;
		// FORECAST SECTION
		else if (qName.equalsIgnoreCase("forecast")) {
			forecast = false;
			myWeatherResult.addForecast(currentForecast);
			currentForecast = null;
		} else if (qName.equalsIgnoreCase("period"))
			period = false;
		else if (qName.equalsIgnoreCase("textSummary")) {
			textSummaryCount++;
			textSummary = false;
			
		}
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		String newString = new String(ch, start, length);
		if (provinceTag)
			myWeatherResult.setProvince(newString);
		if (cityTag)
			myWeatherResult.setCity(newString);
		if (locationTag)
			myWeatherResult.setLocation(newString);
		if (timeStamp && obsTimeTag)
			try {
				myWeatherResult.getObservation().setDate(
						inputFormatter.parse((new String(ch, start, length))
								+ "UTC"));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		if (timeStamp && forecastTimeTag)
			try {
				myWeatherResult.setForecastDate(inputFormatter
						.parse(new String(ch, start, length) + "UTC"));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		if (currentConditions && condition) {
			// currentResult.setDay("Currently:");
			myWeatherResult.getObservation().setCondition(newString);
		}
		if (currentConditions && high)
			myWeatherResult.getObservation().setTemperature(
					Double.parseDouble(newString));
		if (currentConditions && pressure)
			myWeatherResult.getObservation().setPressure(
					Double.parseDouble(newString));
		if (currentConditions && visibility)
			myWeatherResult.getObservation().setVisibility(
					Double.parseDouble(newString));
		if (currentConditions && humidity)
			myWeatherResult.getObservation().setHumidity(
					Integer.parseInt(newString));
		if (wind && speed)
			myWeatherResult.getObservation().setWindSpeed(
					Integer.parseInt(newString));
		if (wind && direction)
			myWeatherResult.getObservation().setWindDirection(newString);
		if (wind && gust)
			myWeatherResult.getObservation().setWindGust(
					Integer.parseInt(newString));
		if (wind && bearing)
			myWeatherResult.getObservation().setWindBearing(
					Integer.parseInt(newString));

		// FORECAST SECTION

		// forecast, period, textsummary
		if (forecast && period)
			currentForecast.setPeriod(newString);
		if (forecast && textSummary && (textSummaryCount == 0))
			currentForecast.setSummary(newString);

	}
}