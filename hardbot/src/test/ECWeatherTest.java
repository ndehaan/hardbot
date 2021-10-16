package test;

import actions.ECWeatherAction;

public class ECWeatherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ECWeatherAction wa = new ECWeatherAction();
		
		for(String s : wa.perform("vancouver","wb"))
			System.out.println(s);
	}

}
