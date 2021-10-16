package test;

import actions.Action;
import actions.SunAction;

public class SunTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Action sa = new SunAction();
		for (String s : sa.perform("Halifax","wb"))
			System.out.println(s);
	}

}
