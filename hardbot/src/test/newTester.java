package test;

import actions.*;

public class newTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		getAirport("YYC");
		getAirport("Vancouver");
		getAirport("Toronto");
			getAirport("Thunder Bay");
			getAirport("Abbotsford");
	}

	private static void ipv4Tester() {
		IPV4Action ia = new IPV4Action();
		for (String s : ia.perform("","wb"))
			System.out.println(s);
		;
	}

	private static void locationTester() {
		String[] locations = new String[] {
				"d154-20-159-187.bchsia.telus.net,", ",www.cbc.ca",
				",www.chantefrance.com", ",www.google.com", ",in.valid" };
		for (String s : locations)
			getLocation(s);
	}

	private static void weatherTester() {
		String[] weather = new String[] { "Vancouver", "Washington",
				"SillyTown", "blank" };
		for (String s : weather)
			getWeather(s);
		for (int i = 0; i < 3; i++)
			System.out.println();
		for (String s : weather)
			getECWeather(s);
	}

	private static void getWeather(String city) {
		WeatherAction wa = new WeatherAction();
		for (String s : wa.perform(city,"wb"))
			System.out.println(s);
	}

	private static void getECWeather(String city) {
		ECWeatherAction wa = new ECWeatherAction();
		for (String s : wa.perform(city,"wb"))
			System.out.println(s);
	}

	private static void getLocation(String location) {
		LocationAction la = new LocationAction();
		for (String s : la.perform(location,"wb"))
			System.out.println(s);
	}
	private static void getAirport(String inputString) {
		AirportAction la = new AirportAction();
		for (String s : la.perform(inputString,"wb"))
			System.out.println(s);
	}
	
}
