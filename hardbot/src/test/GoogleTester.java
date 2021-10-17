package test;

import actions.Action;
import actions.GoogleAction;
import actions.WeatherAction;
import main.MyBot;

public class GoogleTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Action ga = new GoogleAction();
		for (String s: ga.perform("wiredboy","wb", new MyBot()))
			System.out.println(s);
		ga=new WeatherAction();
		for (String s: ga.perform("Vancouver","wb", new MyBot()))
			System.out.println(s);
	}

}
