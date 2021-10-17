package test;

import actions.Action;
import actions.WeatherAction;
import main.MyBot;

public class WeatherTester {

	public static void main(String[] args) throws Exception {
		Action wa = new WeatherAction();
		for (String s : wa.perform("marseille","wb", new MyBot()))
			System.out.println(s);

	}

}
