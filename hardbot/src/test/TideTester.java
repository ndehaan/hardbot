package test;

import java.io.BufferedReader;
import java.io.FileReader;

import actions.Action;
import actions.TideAction;
import main.MyBot;
import util.FileDownload;

public class TideTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Action ta = new TideAction();
		String[] results = (String[]) ta.perform("","wb", new MyBot());
		for (String s : results)
			System.out.println(s);
	}
}
