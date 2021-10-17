package actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.MyBot;
import util.Configuration;
import util.FileDownload;

public class IPV4Action extends Action {

	private static SimpleDateFormat inputDateFormatter = new SimpleDateFormat(
			"dd-MMM-yyyy");
	private static SimpleDateFormat outputDateFormatter = new SimpleDateFormat(
			"EEEEE, MMMMM d, yyyy");

	@Override
	public String getName() {
		return "ipv4";
	}

	@Override
	public String[] perform(String request, String sender, MyBot bot) {
		String url = "http://www.potaroo.net/tools/ipv4/index.html";
		String ianaSearch = "Projected IANA Unallocated";
		String rirSearch = "Projected RIR Unallocated";
		FileDownload.download(url, "ipv4.txt");
		Date iana = null, rir = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader("ipv4.txt"));
			String str;
			String[] blah;
			while ((str = in.readLine()) != null) {
				if (str.contains(ianaSearch)) {
					blah = str.split(" ");
					iana = inputDateFormatter.parse(blah[blah.length - 1]);
				}
				if (str.contains(rirSearch)) {
					blah = str.split(" ");
					rir = inputDateFormatter.parse(blah[blah.length - 1]);
					break;
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return new String[] { getName() + ": an error occurred." };
		}
		File file = new File("ipv4.txt");
		file.delete();
		String[] result = new String[2];
		result[0] = "IANA will have allocated all IPv4 addresses on "
				+ outputDateFormatter.format(iana) + " ("
				+ daysFromNow(iana) + " days from now)";
		result[1] = "RIR  will have allocated all IPv4 addresses on "
				+ outputDateFormatter.format(rir) + " ("
				+ daysFromNow(rir) + " days from now)";
		return result;
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the number of days until IPv4 address depletion.",
				"Usage: Type \"" + command + this.getName() + "\"" };
	}
	
	public static long daysFromNow(Date date) {
		Date now = new Date();
		return (date.getTime() - now.getTime()) / 3600 / 24 / 1000;
	}
}
