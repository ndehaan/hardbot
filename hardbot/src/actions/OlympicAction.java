package actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.Configuration;

public class OlympicAction extends Action {

	@Override
	public String getName() {
		return "olympics";
	}

	@Override
	public String[] perform(String request, String sender) {
		String[] result = new String[1];
		//SimpleDateFormat inFormatter = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat inDateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm z");
		SimpleDateFormat outFormatter = new SimpleDateFormat("MMMM dd, yyyy");
		Date vancouverDate = new Date();
		Date londonDate = new Date();
		Date rioDate = new Date();
		Date sochiDate = new Date();
		try {
			vancouverDate = inDateTimeFormatter.parse("02/12/2010 18:00 -0800");
			londonDate = inDateTimeFormatter.parse("07/27/2012 18:00 +0000");
			rioDate = inDateTimeFormatter.parse("09/07/2016 18:00 -0300");
			sochiDate = inDateTimeFormatter.parse("02/07/2014 18:00 +0400");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date now = new Date();
		// System.out.println(now.toString());
		// System.out.println(londonDate.toString());
		Long vancouverResult = (vancouverDate.getTime() - now.getTime()) / 3600 / 24 / 1000;
		Long londonResult = (londonDate.getTime() - now.getTime()) / 3600 / 24 / 1000;
		Long sochiResult = (sochiDate.getTime() - now.getTime()) / 3600 / 24 / 1000;
		Long rioResult = (rioDate.getTime() - now.getTime()) / 3600 / 24 / 1000;
		if (vancouverResult >= 0) {
			result = new String[4];
			result[0] = vancouverResult.toString()
					+ " days until the Vancouver 2010 Winter Olympics on "
					+ outFormatter.format(vancouverDate) + ".";
			result[1] = londonResult.toString()
					+ " days until the London 2012 Summer Olympics on "
					+ outFormatter.format(londonDate) + ".";
			result[2] = sochiResult.toString()
					+ " days until the Sochi 2014 Winter Olympics on "
					+ outFormatter.format(sochiDate) + ".";
			result[3] = rioResult.toString()
					+ " days until the Rio de Janeiro 2016 Summer Olympics on "
					+ outFormatter.format(rioDate) + ".";
		} else if (londonResult >= 0) {
			result = new String[3];
			result[0] = londonResult.toString()
					+ " days until the London 2012 Summer Olympics on "
					+ outFormatter.format(londonDate) + ".";
			result[1] = sochiResult.toString()
					+ " days until the Sochi 2014 Winter Olympics on "
					+ outFormatter.format(sochiDate) + ".";
			result[2] = rioResult.toString()
					+ " days until the Rio de Janeiro 2016 Summer Olympics on "
					+ outFormatter.format(rioDate) + ".";
		} else if (sochiResult >= 0) {
			result = new String[2];
			result[0] = sochiResult.toString()
					+ " days until the Sochi 2014 Winter Olympics on "
					+ outFormatter.format(sochiDate) + ".";
			result[1] = rioResult.toString()
					+ " days until the Rio de Janeiro 2016 Summer Olympics on "
					+ outFormatter.format(rioDate) + ".";

		} else {
			result[0] = rioResult.toString()
					+ " days until the Rio de Janeiro 2016 Summer Olympics on "
					+ outFormatter.format(rioDate) + ".";
		}
		return result;
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the number of days until upcoming summer and winter Olympics.",
				"Usage: Type \"" + command + this.getName() + "\"" };
	}
}
