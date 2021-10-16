package test;

import actions.Action;
import actions.UBCWeatherAction;

public class UBCWeatherTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UBCWeatherAction action = new UBCWeatherAction();
		// UBCWeatherResult result = (UBCWeatherResult) action.perform(null);
		// System.out.println(result.getDate()+" "+result.getOutsideTemp()+"ºC");
		doAction(action, null);
	}

	private static void doAction(Action action, String request) {
		String[] results = action.perform(request,"wb");
		// Send each line of the results to the channel
		for (String s : results)
			System.out.println(s);
	}

}
