package actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import util.Configuration;
import util.FileDownload;

public class TideAction extends Action {

	private SimpleDateFormat inFormatter = new SimpleDateFormat(
			"EEE, MMM dd yyyy");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"EEEEE, dd MMMMM yyyy");
	private SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
	private SimpleDateFormat inTimeFormatter = new SimpleDateFormat(
			"EEEEE, dd MMMMM yyyy hh:mm a");
	private SimpleDateFormat outTimeFormatter = new SimpleDateFormat("hh:mm a");

	@Override
	public String getName() {
		return "tide";
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the tide report for Vancouver, BC for the current day.",
				"Usage: Type \"" + command + this.getName() + "\"" };
	}

	@Override
	public String[] perform(String request, String sender) {
		ArrayList<String> results = new ArrayList<String>();
		FileDownload
				.download(
						// "http://www.myforecast.com/bin/tide.m?city=54366&metric=false&tideLocationID=T2484",
						"http://www.myforecast.com/bin/tide_extended.m?city=54366&metric=false&tideLocationID=T1706",
						"vantides.htm");
		BufferedReader in = null;
		results.add("Tide Report for Vancouver, BC (Point Atkinson)");
		try {
			in = new BufferedReader(new FileReader("vantides.htm"));
			String str, currDateString = "";
			TreeMap<Date, String> tides = new TreeMap<Date, String>();
			Date currDate = null;
			int count = 0;
			while ((str = in.readLine()) != null)
				if (str
						.contains("<td align=\"left\" valign=\"middle\" class=\"bldwt\" rowspan=\"7\"")) {
					str = str.split(">")[1];
					str = str.substring(0, str.length() - 4);
					str = str + " " + yearFormatter.format(new Date());
					str = dateFormatter.format(inFormatter.parse(str));
					currDateString = str;
					results.add(str);
				} else if (str
						.contains("<td align=\"left\" valign=\"middle\" class=\"normal\"")
						&& count < 8) {
					count++;
					str = str.split(">")[1];
					str = str.substring(0, str.length() - 4);
					if (str.length() != 0) {
						// Get the time
						if (count % 2 == 1) {
							String temp = currDateString + " " + str;
							currDate = inTimeFormatter.parse(temp);
						}
						// get the tide and insert it along with the saved date
						else {
							tides.put(currDate, str);
						}
					}
				}
			for (Date d : tides.keySet())
				results.add(outTimeFormatter.format(d) + " - " + tides.get(d));

		} catch (Exception e) {
			e.printStackTrace();
			results.add("Error accessing/processing tide information");
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File("vantides.htm");
		file.delete();
		String[] stringArray = new String[results.size()];
		for (int i = 0; i < results.size(); i++)
			stringArray[i] = results.get(i);
		return stringArray;
	}
}
