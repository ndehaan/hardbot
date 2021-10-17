package test;

import actions.*;
import main.MyBot;

public class RfTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Action a = new RxslAction();
		for (String s : a.perform("2450 20 14.5 0 5 14.5", "wb", new MyBot()))
			System.out.println(s);
	}

}
