package test;

import actions.AirportAction;
import actions.AreaCodeAction;

public class AreaCodeTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AreaCodeAction aca = new AreaCodeAction();
		String[] result = aca.perform("778-995","wb");
		for (String s : result)
			System.out.println(s);
		AirportAction apa = new AirportAction();
		result = apa.perform("Nelson","wb");
		for (String s : result)
			System.out.println(s);
		result = apa.perform("Vancouver","wb");
		for (String s : result)
			System.out.println(s);
		result = apa.perform("London","wb");
		for (String s : result)
			System.out.println(s);
	}

}
