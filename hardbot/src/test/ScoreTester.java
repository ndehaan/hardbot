package test;

import actions.Action;
import actions.ScoresAction;
import main.MyBot;

public class ScoreTester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Action scoreAction = new ScoresAction();
		for (String s : scoreAction.perform("nfl","wb", new MyBot()))
			System.out.println(s);
	}

}
