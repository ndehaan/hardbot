package test;

import actions.*;

public class RfTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Action a = new RxslAction();
		for (String s : a.perform("2450 20 14.5 0 5 14.5", "wb"))
			System.out.println(s);
	}

}
