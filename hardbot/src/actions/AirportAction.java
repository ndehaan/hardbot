package actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;

import util.Configuration;
import util.FileDownload;

public class AirportAction extends Action {

	@Override
	public String getName() {
		return "airport";
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Searches IATA for airport codes and locations ",
				"Usage: Type \""
						+ command
						+ getName()
						+ "\" followed by the city or code you are looking for." };
	}

	@Override
	public String[] perform(String request, String sender) {
		if (request.trim().length() < 3) {
			String[] result1 = { "No results found." };
			return result1;
		}
		ArrayList<String> resultList = new ArrayList<String>();
		String fileName = "airport_codes_alpha.html";
		request = request.trim();
		String requestLower = request.toLowerCase();
		String url = "http://www.photius.com/wfb2001/airport_codes_alpha.html";
		FileDownload.download(url, fileName);
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str;
			while ((str = in.readLine()) != null) {
				if (str.toLowerCase().contains(requestLower)) {
					if (!resultList.contains(str))
						resultList.add(processLine(str));
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return new String[] { getName() + ": an error occurred." };
		}
		File file = new File(fileName);
		file.delete();
		String[] result = new String[1];
		if (resultList.isEmpty()) {
			result[0] = "No results found.";
		} else if (resultList.size() <= 10) {
			result = resultList.toArray(new String[resultList.size()]);
		} else {
			result[0] = "More than 10 results found.  Please refine your search and try again.";
		}
		return result;
	}

	private String processLine(String line) {
		String result = "";
		String[] array = line.split(" ");
		String code = array[1];
		result = code + " -";
		for (int i = 2; i < array.length; i++)
			if (!array[i].trim().equals("-"))
				result += " " + array[i];

		return result;
	}
}
