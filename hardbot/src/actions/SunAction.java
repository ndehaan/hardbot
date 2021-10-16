package actions;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import util.Configuration;
import util.WeatherResult;
import util.ECWeather.ECSitesHandler;

public class SunAction extends Action {

	private static SimpleDateFormat inputDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static SimpleDateFormat outputDateFormat = new SimpleDateFormat(
			"HH:mm:ss zzz");

	@Override
	public String getName() {
		return "sun";
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns sunrise and sunset information from Environment Canada for select Canadian cities.",
				"Usage: Type \""
						+ command
						+ this.getName()
						+ "\" followed the city name. (default city is Vancouver)" };
	}

	@Override
	public String[] perform(String request, String sender) {
		ArrayList<String> resultList = new ArrayList<String>();
		if (request.equalsIgnoreCase(""))
			request = "Vancouver";
		resultList.add("Sunrise and Sunset times for " + request);
		String forecastURL = getECAddress(request);
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(forecastURL);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("riseSet");

			// Now we have the riseSet section
			Node riseSetNode = nodeLst.item(0);

			// Now we want to get dateTimes
			Element dateTimeElmt = (Element) riseSetNode;
			NodeList fstNmElmntLst = dateTimeElmt
					.getElementsByTagName("dateTime");

			// Now we need to go through each dateTime to find the UTC dates
			for (int i = 0; i < fstNmElmntLst.getLength(); i++) {
				Element dateTimeElmnt = (Element) fstNmElmntLst.item(i);
				if (dateTimeElmnt.getAttribute("name").equalsIgnoreCase(
						"sunrise")
						&& dateTimeElmnt.getAttribute("zone").equalsIgnoreCase(
								"UTC")) {
					String dateString = dateTimeElmnt.getElementsByTagName(
							"timeStamp").item(0).getTextContent();
					inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
					Date riseDate = inputDateFormat.parse(dateString);
					resultList.add("Sunrise: "
							+ outputDateFormat.format(riseDate));

				}
				if (dateTimeElmnt.getAttribute("name").equalsIgnoreCase(
						"sunset")
						&& dateTimeElmnt.getAttribute("zone").equalsIgnoreCase(
								"UTC")) {
					String dateString = dateTimeElmnt.getElementsByTagName(
							"timeStamp").item(0).getTextContent();
					inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
					Date setDate = inputDateFormat.parse(dateString);
					resultList.add("Sunset:  "
							+ outputDateFormat.format(setDate));
				}
			}
		} catch (Exception e) {
			return new String[] { "Error finding sunrise/sunset info for "
					+ request };
		}

		return resultList.toArray(new String[resultList.size()]);
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
			e.printStackTrace();
		}
		return result;
	}
}
