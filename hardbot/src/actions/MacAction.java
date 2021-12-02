package actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import util.Configuration;
import util.FileDownload;

public class MacAction extends Action {

	@Override
	public String getName() {
		return "mac";
	}

	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the manufacturer name associated with the entered MAC address",
				"Usage: Type \""
						+ command
						+ this.getName()
						+ "\" followed by 3 or more octets separated by hyphens (-) or colons (:)." };
	}

	@Override
	public String[] perform(String request, String sender) {
		String result = "No results found for " + request;
		// Process the parameter
		if (request.length() < 8) {
			return new String[] { result };
		}
		String prefix = (String) request.subSequence(0, 8);
		prefix = prefix.trim().replace(':', '-');
		prefix = prefix.toUpperCase();
		// another source might be
		// http://anonsvn.wireshark.org/wireshark/trunk/manuf
		String url = "http://standards.ieee.org/regauth/oui/oui.txt";
		// Find out if we need to update our file
		Date mod = new Date(new File("oui.txt").lastModified());
		Date now = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(mod);
		cal.add(Calendar.DAY_OF_YEAR, 90);
		Date expiry = new Date(cal.getTimeInMillis());
		if (expiry.before(now)) {
			// send(channel, "Source is more than 90 days old.");
			// send(channel, "Downloading update from " + url);
			FileDownload.download(url, "oui.txt");
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader("oui.txt"));
			String str;
			String[] blah;
			while ((str = in.readLine()) != null) {
				if (str.contains(prefix)) {
					result = str.split(" ")[0] + "   ";
					blah = str.split("(hex)");
					result += ((String) blah[1]
							.subSequence(1, blah[1].length())).trim();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String[] { result };
	}
}
