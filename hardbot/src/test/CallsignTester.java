package test;

import actions.Action;
import actions.CallsignAction;
import main.MyBot;

public class CallsignTester {
	public static void main(String[] args) throws Exception {
		Action cs = new CallsignAction();
		for (String s : cs.perform("vk1","wb", new MyBot()))
			System.out.println(s);
	}
}
