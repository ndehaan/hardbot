package test;

import java.io.BufferedReader;
import java.io.FileReader;

import actions.Action;
import actions.TideAction;


import util.FileDownload;

public class TideTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Action ta = new TideAction();
		String[] results = (String[]) ta.perform("","wb");
		for (String s : results)
			System.out.println(s);
	}
}
