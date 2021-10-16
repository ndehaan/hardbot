package test;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import util.ECWeather.ECForecast;
import util.ECWeather.ECObservation;
import util.ECWeather.ECSitesHandler;
import util.ECWeather.ECWeatherHandler2;
import util.ECWeather.ECWeatherResult;

public class ECTester2 {

	public static void main(String[] args) {
		String city = "Vancouver";
		String province = "BC";
		String address = getECCode(city, province);
		if (address == null)
			System.out.println("City not found");
		else
			getWeather("http://dd.weatheroffice.gc.ca/EC_sites/xml/" + province
					+ "/" + address + "_e.xml");
	}

	private static String getECCode(String cityString, String provinceString) {
		String address = "http://dd.weatheroffice.gc.ca/EC_sites/xml/siteList.xml";
		String result = null;
		try {
			// URL to load data from
			URL url = new URL(address);
			// Get a SAXParser from the SAXParserFactory
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			// Get the XMLReader of the SAXParser we created
			XMLReader xr = sp.getXMLReader();
			ECSitesHandler myHandler = new ECSitesHandler(cityString,
					provinceString);
			xr.setContentHandler(myHandler);

			xr.parse(new InputSource(url.openStream()));

			result = myHandler.getCode();

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return result;
	}

	public static void getWeather(String address) {
		try {
			// URL to load data from
			URL url = new URL(address);
			// Get a SAXParser from the SAXParserFactory
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			// Get the XMLReader of the SAXParser we created
			XMLReader xr = sp.getXMLReader();
			ECWeatherHandler2 myHandler = new ECWeatherHandler2();
			xr.setContentHandler(myHandler);
			xr.parse(new InputSource(url.openStream()));

			ECWeatherResult weatherResult = myHandler.getWeatherResult();
			ECObservation observation = weatherResult.getObservation();
			System.out.println("Weather for " + weatherResult.getCity() + ", "
					+ weatherResult.getProvince()
					+ " provided by Environment Canada.");
			System.out.println("Observations at " + weatherResult.getLocation()
					+ " taken " + observation.getDate());
			String result="\t";
			if (observation.getCondition()!=null)
				result+=observation.getCondition()+", ";
			result += observation.getTemperature() + "ºC, Wind: "
					+ observation.getWindDirection() + " at "
					+ observation.getWindSpeed() + "km/h, Humidity: "
					+ observation.getHumidity() + "%.";
			System.out.println(result);

			System.out.println("Forecast issued at: "
					+ weatherResult.getForecastDate());
			for (ECForecast f : weatherResult.getForecasts()) {
				String output = f.getPeriod() + ":";
				if (output.length() <= 7)
					output += "\t";
				output = output + "\t" + f.getSummary();
				System.out.println(output);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
