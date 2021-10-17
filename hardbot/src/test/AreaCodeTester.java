package test;

import actions.AirportAction;
import actions.AreaCodeAction;
import main.MyBot;

public class AreaCodeTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		AreaCodeAction aca = new AreaCodeAction();
		String[] result = aca.perform("778-995","wb", new MyBot());
		for (String s : result)
			System.out.println(s);
		AirportAction apa = new AirportAction();
		result = apa.perform("Nelson","wb", new MyBot());
		for (String s : result)
			System.out.println(s);
		result = apa.perform("Vancouver","wb", new MyBot());
		for (String s : result)
			System.out.println(s);
		result = apa.perform("London","wb", new MyBot());
		for (String s : result)
			System.out.println(s);
	}

}
