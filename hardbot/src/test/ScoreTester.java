package test;

import actions.Action;
import actions.ScoresAction;

public class ScoreTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Action scoreAction = new ScoresAction();
		for (String s : scoreAction.perform("nfl","wb"))
			System.out.println(s);
	}

}
