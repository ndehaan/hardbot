package test;

import actions.Action;
import actions.WeatherAction;

public class WeatherTester {

	public static void main(String[] args) {
		Action wa = new WeatherAction();
		for (String s : wa.perform("marseille","wb"))
			System.out.println(s);

	}

}
