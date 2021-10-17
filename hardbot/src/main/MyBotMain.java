package main;

import util.Configuration;

public class MyBotMain {

	public static void main(String[] args) throws Exception {
		Configuration config = Configuration.getConfig();
		if (config == null) {
			System.out.println("Unable to load configuration file");
			System.exit(9);
		}
		// Create bot (need to set name and logging)
		MyBot bot = new MyBot();
		// Set whether or not to output entries to standard out
		bot.setVerbose(config.isVerbose());
		// Connect to server
		bot.connect(config.getServer());
		// Join channels
		bot.setMessageDelay(250);
		for (String s : config.getChannels()) {
			bot.joinChannel(s);
		}
	}
}
