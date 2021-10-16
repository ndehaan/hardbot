package actions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import main.MyBot;
import util.Configuration;

public class TimeAction extends Action {

	@Override
	public String getName() {
		return "time";
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the time at the location specified.",
				"Usage: Type \""
						+ command
						+ this.getName()
						+ "\" optionally followed by the city name.  Default city is Vancouver, BC." };
	}

	@Override
	public String[] perform(String request, String sender, MyBot myBot) {
		String outZone = null;

		// if there's no request(no parameter given by user) use Vancouver
		if (request.equals("") || request.equals(null))
			request = "America/Vancouver";

		// we only care about the first parameter
		//request = request.split(" ")[0].trim();
		request = request.replace(' ', '_');
		// search through all of the available Java timezones
		for (String s : TimeZone.getAvailableIDs()) {
			// look for a direct match
			if (s.equalsIgnoreCase(request)) {
				outZone = s;
				break;
			}
			// also look for just a city match
			if (s.contains("/")) {
				String city = s.split("/")[1];
				if (city.equalsIgnoreCase(request)) {
					outZone = s;
					break;
				}
			}
		}
		// if no match found, inform the user
		if (outZone == null) {
			String[] result = { "Timezone not recognized" };
			return result;
		}
		// set the timezone
		TimeZone stz = TimeZone.getTimeZone(outZone);
		stz.setID(outZone);
		// format the date
		SimpleDateFormat sdf1 = new SimpleDateFormat(
				"EEEE dd MMMM yyyy  HH:mm:ss Z zzzz");
		sdf1.setTimeZone(stz);

		// Get the current time/date
		Date date = new Date();

		// Compute the offset
		int secs = (int) stz.getOffset(date.getTime()) / 1000;
		int hours = secs / 3600, remainder = secs % 3600, minutes = remainder / 60;

		String offset = (hours >= 0 ? "+" : "") + hours
				+ (minutes < 10 ? ":0" : ":") + minutes;
		// Format it to our new timezone and return it
		// String[] result = { sdf1.format(date)+" ("+offset+")" };
		String[] result = { sdf1.format(date) };
		return result;
	}

}
