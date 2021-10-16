package util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GoogleWeatherHandler extends DefaultHandler {

	private boolean weather_tag = false;

	private boolean problem_cause_tag = false;

	private boolean forecast_info_tag = false;

	private boolean current_cond_tag = false;

	private boolean forecast_cond_tag = false;

	private boolean degreesF = false;

	private WeatherResult result = new WeatherResult();

	public GoogleWeatherHandler() {

	}

	public WeatherResult getResult() {
		return result;
	}

	public void setResult(WeatherResult result) {
		this.result = result;
	}

	/**
	 * Gets called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (qName.equals("problem_cause"))
			problem_cause_tag = true;
		else if (qName.equals("weather"))
			weather_tag = true;
		else if (qName.equals("current_conditions"))
			current_cond_tag = true;
		else if (qName.equals("forecast_conditions")) {
			forecast_cond_tag = true;
			result.addForecast(new ForecastResult());
		} else if (qName.equals("unit_system")
				&& atts.getValue("data").equals("US"))
			degreesF = true;
		else if (qName.equals("city")) {
			result.setCity(atts.getValue("data"));
		}

		if (current_cond_tag == true) {
			Observation current = result.getCurrent();
			if (qName.equals("condition")) {
				current.setDay("Currently: ");
				current.setCondition(atts.getValue("data"));
			}
			if (qName.equals("temp_c"))
				current.setHigh(Integer.parseInt(atts.getValue("data")));
			if (qName.equals("humidity")) {
				String temp = atts.getValue("data");
				temp = (String) temp.subSequence(temp.length() - 3, temp
						.length() - 1);
				current.setHumidity(Integer.parseInt(temp.trim()));
			}
			if (qName.equals("wind_condition")) {
				String temp = atts.getValue("data");
				String[] array = temp.split(" ");
				temp = array[array.length - 2];
				int result = Integer.parseInt(temp);
				if (degreesF) {
					result = mToK(result);
				}
				current.setWind(result);
				current.setWindDirection(array[1]);
			}
		}
		if (forecast_cond_tag == true) {
			ArrayList<ForecastResult> resultList = result.getForecastList();
			if (qName.equals("day_of_week"))
				resultList.get(resultList.size() - 1).setDay(
						atts.getValue("data"));
			if (qName.equals("condition"))
				resultList.get(resultList.size() - 1).setCondition(
						atts.getValue("data"));
			if (qName.equals("high")) {
				int temp = Integer.parseInt(atts.getValue("data"));
				if (degreesF)
					temp = FtoC(temp);
				resultList.get(resultList.size() - 1).setHigh(temp);
			}

			if (qName.equals("low")) {
				int temp = Integer.parseInt(atts.getValue("data"));
				if (degreesF)
					temp = FtoC(temp);
				resultList.get(resultList.size() - 1).setLow(temp);
			}
		}
	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (qName.equals("problem_cause"))
			problem_cause_tag = false;
		else if (qName.equals("weather"))
			weather_tag = false;
		else if (qName.equals("current_conditions"))
			current_cond_tag = false;
		else if (qName.equals("forecast_conditions"))
			forecast_cond_tag = false;
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {
	}

	private int FtoC(double val) {
		// Convert to celcius
		val = (val - 32) / 1.8;
		// Round up/down & convert to int
		DecimalFormat myFormat = new DecimalFormat("0");
		return Integer.parseInt(myFormat.format(val));
	}

	private int mToK(int val) {
		float result = (float) (1.609344 * val);
		return Math.round(result);
	}
}