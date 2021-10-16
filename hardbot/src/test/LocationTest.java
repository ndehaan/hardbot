package test;

import actions.Action;
import actions.LocationAction;
import actions.WeatherAction;

public class LocationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Action la = new LocationAction();
		String[] results = la.perform("S010600095b3563a5.ca.shawcable.net","wb");
		for (String s : results)
			System.out.println(s);
		la = new WeatherAction();
		results = la.perform("Toronto","wb");
		for (String s : results)
			System.out.println(s);
	}

}
