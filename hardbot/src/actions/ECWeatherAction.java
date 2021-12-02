package actions;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeSet;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import main.MyBot;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import util.Configuration;
import util.FileDownload;
import util.WeatherResult;
import util.ECWeather.ECForecast;
import util.ECWeather.ECObservation;
import util.ECWeather.ECSitesHandler;
import util.ECWeather.ECWeatherHandler2;
import util.ECWeather.ECWeatherResult;

public class ECWeatherAction extends Action {
	private MyBot bot = null;

	public ECWeatherAction() {

	}

	public ECWeatherAction(MyBot bot) {
		this.bot = bot;
	}

	@Override
	public String getName() {
		return "ecweather";
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns a weather summary from Environment Canada for select Canadian cities.  Current conditions will be reported in the current window; forecasts will be sent via private message.",
				"Usage: Type \"" + command + this.getName()
						+ "\" followed the city name." };
	}

	@Override
	public String[] perform(String request, String sender) {
		String forecastURL = getECAddress(request);
		if (forecastURL == null)
			return null;
		ECWeatherResult weatherResult = null;
		try {
			// URL to load data from
			URL url = new URL(forecastURL);
			// Get a SAXParser from the SAXParserFactory
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			// Get the XMLReader of the SAXParser we created
			XMLReader xr = sp.getXMLReader();
			ECWeatherHandler2 myHandler = new ECWeatherHandler2();
			xr.setContentHandler(myHandler);
			xr.parse(new InputSource(url.openStream()));
			weatherResult = myHandler.getWeatherResult();
			if (weatherResult.getCity() == null)
				weatherResult = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (weatherResult == null)
			return new String[] { "No results found" };
		ArrayList<String> outputList = new ArrayList<String>();
		ECObservation observation = weatherResult.getObservation();
		outputList
				.add("Observations at " + weatherResult.getLocation()
						+ " taken " + observation.getDate()
						+ " by Environment Canada.");
		String result = "       ";
		if (observation.getCondition() != null)
			result += observation.getCondition() + ", ";
		result += observation.getTemperature() + "ºC, Wind: "
				+ observation.getWindDirection() + " at "
				+ observation.getWindSpeed() + "km/h, Humidity: "
				+ observation.getHumidity() + "%.";
		outputList.add(result);
		outputList.add("Forecast issued for " +weatherResult.getLocation()+ " "+ weatherResult.getForecastDate());
		for (ECForecast f : weatherResult.getForecasts()) {
			String output = f.getPeriod() + ":";
			for (int i = output.length(); i < 17; i++)
				output += " ";
			output += f.getSummary();
			outputList.add(output);
		}
		if (bot != null) {
			for (int i = 2; i < outputList.size(); i++) {
				bot.sendMessage(sender, outputList.get(i));
				System.out.println(outputList.get(i));
			}
			return new String[] { outputList.get(0), outputList.get(1) };
		} else
			return outputList.toArray(new String[outputList.size()]);
	}

	// Method to lookup the Environment Canada XML URL for Canadian cities
	// eg: http://dd.weatheroffice.gc.ca/EC_sites/xml/BC/s0000141_e.xml
	private String getECAddress(String request) {
		String forecastURL = "http://dd.weatheroffice.gc.ca/citypage_weather/xml/";
		if (request.equalsIgnoreCase("hartland")
				|| request.equalsIgnoreCase("wvv")) {
			return forecastURL + "BC" + "/" + "s0000792_e.xml";
		}
		if (request.toLowerCase().contains("fort mcmurray")) {
			return forecastURL + "AB" + "/" + "s0000595_e.xml";
		}
		String city, province = null;
		WeatherAction wa = new WeatherAction();
		// Use Google's weather API to convert the user input into a city/state
		WeatherResult wr = wa.getWeatherResult(request);
		if (wr.getCity() == null)
			return null;
		String temp = wr.getCity();
		String[] cityProvince = temp.split(",");
		if (isProvince(cityProvince[1].trim())) {
			city = cityProvince[0];
			province = cityProvince[1];
		} else
			return null;
		String ECCode = this.getECCode(city, province);

		if (ECCode == null)
			return null;

		// EC and Google use different codes for Newfoundland and Yukon
		if (province.trim().equalsIgnoreCase("NL"))
			province = "NF";
		if (province.trim().equalsIgnoreCase("YT"))
			province = "YK";
		return forecastURL + province.trim() + "/"
				+ this.getECCode(city, province) + "_e.xml";
	}

	// Method to check a string to see if the two letter code is a Canadian
	// province
	private boolean isProvince(String province) {
		TreeSet<String> provinces = new TreeSet<String>();
		provinces.add("BC");
		provinces.add("AB");
		provinces.add("SK");
		provinces.add("MB");
		provinces.add("ON");
		provinces.add("QC");
		provinces.add("NL");
		provinces.add("NS");
		provinces.add("NB");
		provinces.add("PE");
		provinces.add("NU");
		provinces.add("NT");
		provinces.add("YT");
		return provinces.contains(province);
	}

	// Method to get the Environment Canada code for a given Canadian city
	private String getECCode(String cityString, String provinceString) {
		String address = "http://dd.weatheroffice.ec.gc.ca/citypage_weather/xml/siteList.xml";
		File siteList = new File("siteList.xml");
		if (!siteList.exists())
			FileDownload.download(address, "siteList.xml");
		// Find out if we need to update our file
		Date mod = new Date(new File("siteList.xml").lastModified());
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(mod);
		cal.add(Calendar.DAY_OF_YEAR, 90);
		Date expiry = new Date(cal.getTimeInMillis());
		if (expiry.before(now)) {
			// send(channel, "Source is more than 90 days old.");
			// send(channel, "Downloading update from " + url);
			FileDownload.download(address, "siteList.xml");
		}
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
			xr.parse(new InputSource(new FileInputStream(siteList)));
			result = myHandler.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
