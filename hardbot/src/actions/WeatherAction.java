package actions;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import util.Configuration;
import util.ForecastResult;
import util.GoogleWeatherHandler;
import util.WeatherResult;

public class WeatherAction extends Action {

	public String getName() {
		return "weather";
	}

	public String[] perform(String request, String sender) {
		WeatherResult result = this.getWeatherResult(request);
		if (result == null)
			return new String[] { "No results found." };
		if (result.getCity() == null)
			return new String[] { "No results found." };
		ArrayList<String> outputList = new ArrayList<String>();
		ForecastResult curr = result.getCurrent();
		ArrayList<ForecastResult> results = result.getForecastList();
		if (result.getCity() != null) {
			outputList.add(result.getCity());
			if (curr.getDay() != null)
				outputList.add(curr.toString());
			for (ForecastResult fr : results)
				outputList.add(fr.toString());
		}
		return outputList.toArray(new String[outputList.size()]);

	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns a weather summary from Google for most cities worldwide.",
				"Usage: Type \"" + command + this.getName()
						+ "\" followed the city name." };
	}

	public WeatherResult getWeatherResult(String request) {
		WeatherResult result = null;
		String address = "http://www.google.ca/ig/api?weather=";
		String city = request.replace(" ", "%20");
		try {
			// URL to load data from
			URL url = new URL(address + city);
			// Get a SAXParser from the SAXParserFactory
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			// Get the XMLReader of the SAXParser we created
			XMLReader xr = sp.getXMLReader();
			GoogleWeatherHandler myHandler = new GoogleWeatherHandler();
			xr.setContentHandler(myHandler);
			InputSource is = new InputSource(url.openStream());
			is.setEncoding("ISO-8859-1");
			xr.parse(is);
			result = myHandler.getResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
}
