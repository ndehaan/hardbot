package test;

import actions.Action;
import actions.LocationAction;
import actions.WeatherAction;
import main.MyBot;

public class LocationTest {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Action la = new LocationAction();
		String[] results = la.perform("S010600095b3563a5.ca.shawcable.net","wb", new MyBot());
		for (String s : results)
			System.out.println(s);
		la = new WeatherAction();
		results = la.perform("Toronto","wb", new MyBot());
		for (String s : results)
			System.out.println(s);
	}

}
