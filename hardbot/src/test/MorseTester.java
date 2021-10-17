package test;

import actions.Action;
import actions.MorseAction;
import main.MyBot;

public class MorseTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Action a = new MorseAction();
		String[] results = (String[]) a.perform("This is text.", "wb", new MyBot());
		for (String s : results)
			System.out.println(s);
		results = (String[]) a.perform("- .... .. ...   .. ...   - . -..- - .-.-.- ", "wb", new MyBot());
		for (String s : results)
			System.out.println(s);

	}

}
