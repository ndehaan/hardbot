package actions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.Configuration;
import util.UBCWeatherResult;

public class UBCWeatherAction extends Action {

	private static final String USERNAME = "weather";
	private static final String PASSWORD = "wxubc";
	private static final String ADDRESS = "142.103.194.1:2121/";
	private static final String FILENAME = "downld02.txt";
	private static SimpleDateFormat inputFormatter = new SimpleDateFormat(
			"dd/MM/yy hh:mma");
	private static SimpleDateFormat outputFormatter = new SimpleDateFormat(
			"h:mma z EEEEE, MMMMM dd, yyyy");

	@Override
	public String getName() {
		return "wxubc";
	}

	@Override
	public String[] perform(String request, String sender) {
		downloadFile();
		UBCWeatherResult weatherResult = null;
		BufferedReader in;
		Date date = null;
		double insideTemp = 999;
		double outsideTemp = 999;
		double outsideHum = 999;
		double windSpeed = 999;
		String windDir = "---";
		double barometer = 999;
		double rain = 999;
		try {
			in = new BufferedReader(new FileReader(FILENAME));
			String str;
			in.readLine();
			while ((str = in.readLine()) != null) {
				if (!str.contains("Date") && str.contains(" ")) {
					if (str.startsWith(" "))
						str = str.trim();
					String[] stringArray = str.split(" +");
					String temp = stringArray[0] + " " + stringArray[1];
					if (temp.contains("p")) {
						temp = temp.replace("p", "PM");
					}
					if (temp.contains("a"))
						temp = temp.replace("a", "AM");
					date = inputFormatter.parse(temp);
					if (!stringArray[20].equals("---"))
						insideTemp = Double.parseDouble(stringArray[20]);
					if (!stringArray[2].equals("---"))
						outsideTemp = Double.parseDouble(stringArray[2]);
					if (!stringArray[5].equals("---"))
						outsideHum = Double.parseDouble(stringArray[5]);
					windSpeed = Double.parseDouble(stringArray[7]);
					windDir = stringArray[8];
					if (!stringArray[15].equals("------")) {
						DecimalFormat myFormat = new DecimalFormat("#.##");
						barometer = Double.parseDouble(stringArray[15]);
						barometer = Double.valueOf(myFormat.format(barometer));
					}
					rain = Double.parseDouble(stringArray[16]);
				}
				weatherResult = new UBCWeatherResult(date, insideTemp,
						outsideTemp, outsideHum, windSpeed, windDir, barometer,
						rain);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new String[] { "No results found." };
		}
		if (weatherResult == null)
			return new String[] { "No results found." };
		DecimalFormat myFormat = new DecimalFormat("#.##");
		String[] result = new String[3];
		result[0] = "Weather observations from VE7UBC at "
				+ outputFormatter.format(weatherResult.getDate());
		result[1] = "Temperature: " + weatherResult.getOutsideTemp()
				+ "ºC, Wind: " + weatherResult.getWindSpeed() + "km/h "
				+ weatherResult.getWindDir() + ", Barometer: "
				+ myFormat.format(weatherResult.getBarometer() / 10) + "kPa, Humidity: "
				+ weatherResult.getOutsideHumidity() + "%";
		String insideString = "It is ";
		if (insideTemp != 999 && insideTemp > 30)
			insideString += "an unbearable ";
		else if (insideTemp != 999 && insideTemp > 24)
			insideString += "a balmy ";
		else if (insideTemp != 999 && insideTemp < 19)
			insideString += "a cool ";
		insideString += insideTemp + "ºC inside the club room.";
		result[2] = insideString;
		return result;
	}

	private static void downloadFile() {
		URL url;
		try {
			url = new URL("ftp://" + USERNAME + ":" + PASSWORD + "@" + ADDRESS
					+ "/" + FILENAME);
			URLConnection con = url.openConnection();
			BufferedInputStream in = new BufferedInputStream(con
					.getInputStream());
			FileOutputStream out = new FileOutputStream(FILENAME);

			int i = 0;
			byte[] bytesIn = new byte[1024];
			while ((i = in.read(bytesIn)) >= 0) {
				out.write(bytesIn, 0, i);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the most recent weather report from the weather station at the UBC Amateur Radio Society(VE7UBC) club room.",
				"Usage: Type \"" + command + this.getName() + "\"" };
	}
	
}
