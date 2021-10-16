package test;

import actions.Action;
import actions.CallsignAction;

public class CallsignTester {
	public static void main(String[] args) {
		Action cs = new CallsignAction();
		for (String s : cs.perform("ve7scc","wb"))
			System.out.println(s);
	}
}
