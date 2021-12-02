package actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.Configuration;

public class DtvAction extends Action {

	@Override
	public String getName() {
		return "dtv";
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the number of days until digital television conversion in Canada and the USA.",
				"Usage: Type \"" + command + this.getName() + "\"" };
	}

	@Override
	public String[] perform(String request, String sender) {
		String[] result = new String[1];
		SimpleDateFormat inFormatter = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat outFormatter = new SimpleDateFormat("MMMM dd, yyyy");
		Date canadaDate = new Date();
		Date usDate = new Date();
		try {
			canadaDate = inFormatter.parse("08/31/2011");
			usDate = inFormatter.parse("06/12/2009");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date now = new Date();
		Long canResult = (canadaDate.getTime() - now.getTime()) / 3600 / 24 / 1000;
		Long usResult = (usDate.getTime() - now.getTime()) / 3600 / 24 / 1000;
		if (usResult >= 0) {
			result = new String[2];
			String day = "days";
			if (usResult == 1)
				day = "day";
			result[0] = usResult.toString() + " " + day
					+ " until USA DTV changeover on "
					+ outFormatter.format(usDate) + ".";
			result[1] = canResult.toString()
					+ " days until Canada DTV changeover on "
					+ outFormatter.format(canadaDate) + ".";
		} else {

			result[0] = canResult.toString()
					+ " days until Canada DTV changeover on "
					+ outFormatter.format(canadaDate) + ".";
		}
		return result;
	}
}
