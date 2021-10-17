package test;

import actions.Action;
import actions.UBCWeatherAction;
import main.MyBot;

public class UBCWeatherTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		UBCWeatherAction action = new UBCWeatherAction();
		// UBCWeatherResult result = (UBCWeatherResult) action.perform(null);
		// System.out.println(result.getDate()+" "+result.getOutsideTemp()+"�C");
		doAction(action, null);
	}

	private static void doAction(Action action, String request) throws Exception {
		String[] results = action.perform(request,"wb", new MyBot());
		// Send each line of the results to the channel
		for (String s : results)
			System.out.println(s);
	}

}
