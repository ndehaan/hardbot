package actions;

import main.MyBot;

public abstract class Action {
	public abstract String getName();
	public abstract String[] help();
	public abstract String[] perform(String request, String sender, MyBot bot);
}
