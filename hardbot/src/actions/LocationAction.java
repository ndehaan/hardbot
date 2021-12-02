package actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import util.Configuration;
import util.FileDownload;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

public class LocationAction extends Action {

	@Override
	public String getName() {
		return "location";
	}

	@Override
	public String[] perform(String request, String sender) {

		// Source data - http://www.maxmind.com/app/geolitecity

		// If there is no hostname specified, use the requestor's hostname
		// request == hostname,request
		String[] input = request.split(",");
		String hostname = "";
		boolean self = false;
		if (input.length > 1) {
			hostname = input[1];
			if (input[1].trim().equals(input[0].trim()))
				self = true;
		} else {
			hostname = input[0];
			self = true;
		}
		InetAddress ia = null;
		try {
			ia = InetAddress.getByName(hostname);
		} catch (Exception e) {
			// e.printStackTrace();
			return new String[] { "Location not found" };
		}
		if (ia == null)
			return new String[] { "Location not found" };
		String ipAddress = ia.getHostAddress();
		LookupService cl;
		Location myLocation = null;
		try {
			cl = new LookupService("lib/GeoLiteCity.dat",
					LookupService.GEOIP_STANDARD);

			myLocation = cl.getLocation(ipAddress);
		} catch (IOException e) {
			e.printStackTrace();
			return new String[] { "Location not found" };
		}
		String result = "";
		if (self)
			result += "You, ";
		result += hostname + "(" + ipAddress + ")";
		if (self)
			result += ", are in ";
		else
			result += " is in ";
		if (myLocation.city != null)
			result += myLocation.city + ", ";
		if (myLocation.region != null)
			result += getRegion(myLocation) + ", ";
		if (myLocation.countryName != null)
			result += myLocation.countryName;
		return new String[] { result };
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the location of the entered hostname or IP address",
				"Usage: Type \"" + command + this.getName()
						+ "\" followed by the hostname or IP address." };
	}
	
	public static String getRegion(Location location) {
		String result = "";

		if (location.region == null)
			return result;

		if (location.region.equals("00"))
			result += " ";
		else if (location.countryName.equalsIgnoreCase("Canada")
				|| location.countryName.equalsIgnoreCase("United States")) {
			// lookup the code from here
			// http://www.maxmind.com/app/iso3166_2

			// Find out if we need to update our file
			Date mod = new Date(new File("iso3166_2.htm").lastModified());
			Date now = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(mod);
			cal.add(Calendar.DAY_OF_YEAR, 30);
			Date expiry = new Date(cal.getTimeInMillis());
			if (expiry.before(now)) {
				// sendMessage(channel, "Source is more than 30 days
				// old.");
				// sendMessage(channel, "Downloading update.");
				FileDownload.download("http://www.maxmind.com/app/iso3166_2",
						"iso3166_2.htm");
			}

			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader("iso3166_2.htm"));

				String str;
				String[] blah;
				while ((str = in.readLine()) != null) {
					blah = str.split(",");
					if (str.contains(location.region)
							&& blah[0].equalsIgnoreCase(location.countryCode)) {
						result = (String) blah[2].subSequence(1, blah[2]
								.length() - 1);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else {
			// lookup the code from here
			// http://www.maxmind.com/app/fips10_4

			// Find out if we need to update our file
			Date mod = new Date(new File("fips10_4.htm").lastModified());
			Date now = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(mod);
			cal.add(Calendar.DAY_OF_YEAR, 30);
			Date expiry = new Date(cal.getTimeInMillis());
			if (expiry.before(now)) {
				// sendMessage(channel, "Source is more than 30 days
				// old.");
				// sendMessage(channel, "Downloading update.");
				FileDownload.download("http://www.maxmind.com/app/fips10_4",
						"fips10_4.htm");
			}

			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader("fips10_4.htm"));

				String str;
				String[] blah;
				while ((str = in.readLine()) != null) {
					if (str.contains(location.region)
							&& str.contains(location.countryCode)) {
						blah = str.split(",");
						for (int i = 0; i < blah.length; i++)
							result = (String) blah[2].subSequence(1, blah[2]
									.length() - 1);
						break;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

}
