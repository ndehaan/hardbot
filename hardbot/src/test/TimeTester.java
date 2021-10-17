package test;

import java.util.TimeZone;

import actions.Action;
import actions.OlympicAction;
import actions.TimeAction;
import main.MyBot;

public class TimeTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		for (String s : TimeZone.getAvailableIDs())
			System.out.println(s);
		String[] requests = { "America/Vancouver", "America/Phoenix", "GMT",
				"", "Australia/Perth", "baghdad","new York", "dawson creek" };
		Action a = new TimeAction();
		for (String req : requests)
			for (String s : a.perform(req,"wb", new MyBot()))
				System.out.println(req+": "+s);

		OlympicAction oa = new OlympicAction();
		for (String s : oa.perform("","wb", new MyBot()))
			System.out.println(s);
	}

}
