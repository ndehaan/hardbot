package test;

import actions.Action;
import actions.SunAction;
import main.MyBot;

public class SunTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Action sa = new SunAction();
		for (String s : sa.perform("Halifax","wb", new MyBot()))
			System.out.println(s);
	}

}
