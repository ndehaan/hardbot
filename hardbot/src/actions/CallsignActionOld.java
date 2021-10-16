package actions;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import util.ClientHttpRequest;
import util.Configuration;

public class CallsignActionOld extends Action {

	/*
		21:53 <dcubed> feature request for wired/rf/bot
		21:53 <dcubed> in the %c search
		21:53 <dcubed> include prefix's
		21:54 <dcubed> http://www.sierrapapa.it/cq&itu.htm
		21:54 <dcubed> so we can %C vk
		21:55 <dcubed> and it will say "VK1 - VK8 Australia-VK CQ 30 ITU 59"
		21:55 <dcubed> could get comlicated
		21:56 <dcubed> might take some hand bombing to get the mulipl zones
		21:56 <dcubed> ei
		21:56 <dcubed> w7
		21:56 <dcubed> k1
		21:56 <dcubed> n0
		21:57 <dcubed> hmm actualy thoes would return a good value from that list i just posted
		22:08 <wb> no handbombing required

	 */
	@Override
	public String getName() {
		return "callsign";
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Searches Canadian amateur radio callsigns and returns the registrant's name, city and qualifications.",
				"Usage: Type \"" + command + "callsign\" or \"" + command
						+ "c\" followed by the callsign you're looking for." };
	}

	@SuppressWarnings("deprecation")
	@Override
	public String[] perform(String request, String sender) {
		String searchType = "name";
		char c = request.charAt(2);
		if (request.trim().startsWith("V") || request.trim().startsWith("v")
				&& Character.isDigit(c))
			searchType = "call";
		// String address = "http://www.rac.ca/callbook/search.php";
		String address = "http://www.rac.ca/en/rac/services/callbook/search.php";
		InputStream serverInput = null;
		try {
			URL url = new URL(address);
			serverInput = ClientHttpRequest.post(url, new Object[] {
					"QueryText", request, "QueryType", searchType, "submit" });
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> output = new ArrayList<String>();
		if (serverInput != null) {
			DataInputStream dis = new DataInputStream(serverInput);
			String currLine;
			try {
				while ((currLine = dis.readLine()) != null)
					results.add(currLine);
			} catch (IOException e) {
			}
		}

		String callsign = "";
		String name = "";
		// String street = "";
		String location = "";
		String qualifications = "";
		String club = "";
		for (String s : results) {
			// System.out.println(s);
			if (s.contains("<td>Callsign")) {
				String[] blah = s.split("</td><td>");
				String chopped = blah[1].substring(0, 6);
				if (chopped.charAt(chopped.length() - 1) == '<')
					chopped = (String) chopped.subSequence(0,
							chopped.length() - 1);
				callsign = chopped;
			}
			if (s.contains("<td>Name:")) {
				String[] blah = s.split("</td><td>");
				name = blah[1].substring(0, blah[1].length() - 10);
			}
			/*
			 * if (s.contains("Address: ")) { String[] blah =
			 * s.split("</td><td>"); street = blah[1].substring(0,
			 * blah[1].length() - 10).trim(); }
			 */
			if (s.contains("Club Name:")) {
				String[] blah = s.split("</td><td>");
				club = blah[1].substring(0, blah[1].length() - 10);
			}
			if (s.contains("Qualifications")) {
				String[] blah = s.split("</td><td>");
				qualifications = blah[1].substring(0, blah[1].length() - 10);
			}

			if (s.contains(", BC") || s.contains(", AB") || s.contains(", SK")
					|| s.contains(", MB") || s.contains(", ON")
					|| s.contains(", QC") || s.contains(", NL")
					|| s.contains(", NB") || s.contains(", PE")
					|| s.contains(", NS") || s.contains(", NU")
					|| s.contains(", NT") || s.contains(", YT")) {
				String[] blah = s.split("</td><td>");
				location = blah[1].substring(0, blah[1].length() - 17);
			}
		}
		String string1 = callsign + " - " + name + " - " + qualifications;
		// String string2 = street.trim() + ", " + location.trim();
		String string2 = location.trim();
		if (!club.equals(""))
			string2 += ", " + club;
		if (string1.length() > 6) {
			output.add(string1);
			if (location.length() > 1) {
				String margin = "        ";
				if (callsign.length() > 5)
					margin += " ";
				output.add(margin + string2);
			}
		}
		if (output.isEmpty())
			output.add("No results found for the search string: " + request);
		if (output.size() > 12) {
			ArrayList<String> temp = new ArrayList<String>();
			temp
					.add("Output too large for IRC, printing the first 12 lines only.");
			temp.add("Please refine your search or visit " + address);
			for (String s : output)
				temp.add(s);
			output = temp;
		}
		int max = output.size();
		if (max > 13)
			max = 12;
		String[] stringArray = new String[max];
		for (int i = 0; i < max; i++)
			stringArray[i] = output.get(i);
		return stringArray;
	}
}
