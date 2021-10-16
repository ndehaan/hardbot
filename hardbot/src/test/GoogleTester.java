package test;

import actions.Action;
import actions.GoogleAction;
import actions.WeatherAction;

public class GoogleTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Action ga = new GoogleAction();
		for (String s: ga.perform("wiredboy","wb"))
			System.out.println(s);
		ga=new WeatherAction();
		for (String s: ga.perform("Vancouver","wb"))
			System.out.println(s);
	}

}
