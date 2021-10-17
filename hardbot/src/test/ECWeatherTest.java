package test;

import actions.ECWeatherAction;
import main.MyBot;

public class ECWeatherTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ECWeatherAction wa = new ECWeatherAction();
		
		for(String s : wa.perform("vancouver","wb", new MyBot()))
			System.out.println(s);
	}

}
