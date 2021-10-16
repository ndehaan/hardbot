package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import actions.Action;
import actions.UBCWeatherAction;

import util.FileDownload;
import util.ForecastResult;
import util.GoogleWeatherHandler;
import util.Observation;
import util.WeatherResult;

public class test {

	/**
	 * @param args
	 */

	private static final String SQL_USERNAME = "randall";

	private static final String SQL_PASSWORD = "iamabot";

	private static final String SERVER = "jdbc:mysql://10.0.1.5/randall";

	private static Connection con;

	public static void main(String[] args) throws Exception {
		// Class.forName("com.mysql.jdbc.Driver");
		// con = DriverManager.getConnection(SERVER, SQL_USERNAME,
		// SQL_PASSWORD);
		// System.out.println(getMAC("channel", "00-13-02"));
		// location("#wiredboy", "www.google.com", "www.yahoo.com");
		// weather("#wiredboy", "Vancouver");
		Action wx = new UBCWeatherAction();
		for (String s : wx.perform("","wb"))
			System.out.println(s);

	}

	private static void weather(String channel, String parameter) {
		String address = "http://www.google.ca/ig/api?weather=";
		String city = parameter.replace(" ", "%20");
		WeatherResult wr = null;
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
			xr.parse(new InputSource(url.openStream()));
			wr = myHandler.getResult();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		ForecastResult curr = wr.getCurrent();
		ArrayList<ForecastResult> results = wr.getForecastList();
		if (curr == null)
			System.out.println("No results found for: " + parameter);
		else {
			if (wr.getCity() != null)
				System.out.println(wr.getCity());
			if (curr.getDay() != null)
				System.out.println(curr.toString());
			for (ForecastResult fr : results) {
				System.out.println(fr.toString());
			}
		}

	}

	private static String getMAC(String channel, String parameter) {
		String result = "No results found for " + parameter;
		// Process the parameter
		if (parameter.length() < 8)
			return result;
		String prefix = (String) parameter.subSequence(0, 8);
		prefix = prefix.trim().replace(':', '-');
		String url = "http://standards.ieee.org/regauth/oui/oui.txt";
		// Find out if we need to update our file
		Date mod = new Date(new File("oui.txt").lastModified());
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(mod);
		cal.add(Calendar.DAY_OF_YEAR, 30);
		Date expiry = new Date(cal.getTimeInMillis());
		if (expiry.before(now)) {
			System.out
					.println("Source is more than 30 days old, downloading update.");
			FileDownload.download(url, "oui.txt");
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader("oui.txt"));
			String str;
			String[] blah;
			while ((str = in.readLine()) != null) {
				if (str.contains(prefix)) {
					result = str.split(" ")[0] + "\t";
					blah = str.split("(hex)");
					result += ((String) blah[1]
							.subSequence(1, blah[1].length())).trim();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void location(String channel, String message, String hostname) {
		// String hostip = "http://api.hostip.info/?ip=";
		if (message.trim().length() == 0) {
			message = hostname;
		}
		InetAddress ia = null;
		try {
			ia = InetAddress.getByName(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error finding location");
		}
		if (ia == null) {
			System.out.println("Error finding location");
			return;
		}
		String address = ia.getHostAddress();
		String[] octets = address.split("\\.");
		String classA = "ip4_" + octets[0];
		PreparedStatement stmt;
		ResultSet rs = null;
		int cityCode = 0;
		int countryCode = 0;
		String cityString = null;
		String countryString = null;
		try {
			stmt = con.prepareStatement("SELECT city, country FROM hostip."
					+ classA + " WHERE (b = ? and c = ?);");
			stmt.setInt(1, Integer.parseInt(octets[1]));
			stmt.setInt(2, Integer.parseInt(octets[2]));
			rs = stmt.executeQuery();
			rs.first();
			cityCode = rs.getInt("city");
			countryCode = rs.getInt("country");

			stmt = con
					.prepareStatement("SELECT name FROM hostip.countries WHERE id = ?");
			stmt.setInt(1, countryCode);
			rs.close();
			rs = stmt.executeQuery();
			rs.first();
			countryString = rs.getString(1);

			stmt = con
					.prepareStatement("SELECT name FROM hostip.cityByCountry  WHERE city = ?");
			stmt.setInt(1, cityCode);
			rs.close();
			rs = stmt.executeQuery();
			rs.first();
			cityString = rs.getString(1);

			String result = "";
			if (cityString != null)
				result += cityString + "   ";
			if (countryString != null)
				result += countryString;
			result = URLDecoder.decode(result, "UTF-8");
			System.out.println(result);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * private void locationHostip(String channel, String message, String
	 * hostname) { // String hostip = "http://api.hostip.info/?ip="; boolean
	 * self = false; if (message.trim().equalsIgnoreCase("help")) {
	 * sendMessage(channel, "Please submit updates to http://www.hostip.info");
	 * sendMessage(channel,
	 * "The local database should be updated on or about the 15th of each month."
	 * ); return; } if (message.trim().length() == 0) { message = hostname; self
	 * = true; } InetAddress ia = null; try { ia =
	 * InetAddress.getByName(message); } catch (Exception e) { // TODO
	 * Auto-generated catch block System.out.println("Error finding location");
	 * return; } if (ia == null) { System.out.println("Error finding location");
	 * return; } String address = ia.getHostAddress(); String[] octets =
	 * address.split("\\."); String classA = "ip4_" + octets[0];
	 * PreparedStatement stmt; ResultSet rs = null; int cityCode = 0; int
	 * countryCode = 0; String cityString = null; String countryString = null;
	 * String result = ""; try { stmt =
	 * con.prepareStatement("SELECT city, country FROM hostip." + classA +
	 * " WHERE (b = ? and c = ?);"); stmt.setInt(1,
	 * Integer.parseInt(octets[1])); stmt.setInt(2,
	 * Integer.parseInt(octets[2])); rs = stmt.executeQuery(); if (rs.first()) {
	 * cityCode = rs.getInt("city"); countryCode = rs.getInt("country"); stmt =
	 * con .prepareStatement("SELECT name FROM hostip.countries WHERE id = ?");
	 * stmt.setInt(1, countryCode); rs.close(); rs = stmt.executeQuery(); if
	 * (rs.first()) countryString = rs.getString(1); stmt = con
	 * .prepareStatement
	 * ("SELECT name FROM hostip.cityByCountry  WHERE city = ?"); stmt.setInt(1,
	 * cityCode); rs.close(); rs = stmt.executeQuery(); if (rs.first())
	 * cityString = rs.getString(1); if (cityString != null) result +=
	 * cityString + "   "; if (countryString != null) result += countryString;
	 * result = URLDecoder.decode(result, "UTF-8"); } if (result.length() == 0)
	 * sendMessage(channel, "No results found for " + message + " (" + address +
	 * ")"); else { if (self) sendMessage(channel, "You, " + message + " (" +
	 * address + "), are in " + result); else sendMessage(channel, message +
	 * " (" + address + ") is in " + result); } sendMessage(channel,
	 * "If this entry is wrong or incomplete, please type \"%location help\" ");
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */
}
