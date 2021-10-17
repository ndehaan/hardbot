package test;

import actions.*;
import main.MyBot;

public class newTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		getAirport("YYC");
		getAirport("Vancouver");
		getAirport("Toronto");
			getAirport("Thunder Bay");
			getAirport("Abbotsford");
	}

	private static void ipv4Tester() throws Exception {
		IPV4Action ia = new IPV4Action();
		for (String s : ia.perform("","wb", new MyBot()))
			System.out.println(s);
		;
	}

	private static void locationTester() throws Exception {
		String[] locations = new String[] {
				"d154-20-159-187.bchsia.telus.net,", ",www.cbc.ca",
				",www.chantefrance.com", ",www.google.com", ",in.valid" };
		for (String s : locations)
			getLocation(s);
	}

	private static void weatherTester() throws Exception {
		String[] weather = new String[] { "Vancouver", "Washington",
				"SillyTown", "blank" };
		for (String s : weather)
			getWeather(s);
		for (int i = 0; i < 3; i++)
			System.out.println();
		for (String s : weather)
			getECWeather(s);
	}

	private static void getWeather(String city) throws Exception {
		WeatherAction wa = new WeatherAction();
		for (String s : wa.perform(city,"wb", new MyBot()))
			System.out.println(s);
	}

	private static void getECWeather(String city) throws Exception {
		ECWeatherAction wa = new ECWeatherAction();
		for (String s : wa.perform(city,"wb", new MyBot()))
			System.out.println(s);
	}

	private static void getLocation(String location) throws Exception {
		LocationAction la = new LocationAction();
		for (String s : la.perform(location,"wb", new MyBot()))
			System.out.println(s);
	}
	private static void getAirport(String inputString) throws Exception {
		AirportAction la = new AirportAction();
		for (String s : la.perform(inputString,"wb", new MyBot()))
			System.out.println(s);
	}
	
}
