package test;

import actions.Action;
import actions.MorseAction;

public class MorseTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Action a = new MorseAction();
		String[] results = (String[]) a.perform("This is text.","wb");
		for (String s : results)
			System.out.println(s);
		results = (String[]) a.perform("- .... .. ...   .. ...   - . -..- - .-.-.- ","wb");
		for (String s : results)
			System.out.println(s);

	}

}
