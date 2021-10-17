package actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

import main.MyBot;
import util.Configuration;
import util.FileDownload;

public class ScoresAction extends Action {

	@Override
	public String getName() {

		return "scores";
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the sporting scores for the following: NHL, NBA, NFL, MLB, NCAA Basketball",
				"Usage: Type \""
						+ command
						+ this.getName()
						+ "\" followed one of the leagues above will return all of the scores and games for the day.",
				"Optionally, adding the team name after the league will limit the scores to just that game." };
	}

	@Override
	public String[] perform(String request, String sender, MyBot bot) {
		String url = null;
		String title = null;
		ArrayList<String> grep = null;
		String[] args = request.split(" ");
		if (args.length > 1) {
			grep = new ArrayList<String>();
			for (int i = 1; i < args.length; i++) {
				grep.add(args[i]);
			}
		}

		// http://www.mpiii.com/scores/nhl.php
		// also ...nfl, mlb, nba, ncaa, wnba

		if (args[0].equalsIgnoreCase("nhl")) {
			url = "http://m.espn.go.com/nhl/scoreboard";
			title = "NHL";
		}
		if (args[0].equalsIgnoreCase("nba")) {
			url = "http://m.espn.go.com/nba/scoreboard";
			title = "NBA";
		}
		if (args[0].equalsIgnoreCase("nfl")) {
			url = "http://m.espn.go.com/nfl/scoreboard";
			title = "NFL";
		}
		if (args[0].equalsIgnoreCase("mlb")) {
			url = "http://m.espn.go.com/mlb/scoreboard";
			title = "MLB";
		}
		if (args[0].equalsIgnoreCase("ncaa")) {
			url = "http://m.espn.go.com/ncb/scoreboard";
			title = "NCAA - Mens Basketball";
		}
		ArrayList<String> lines = null;
		try {
			FileDownload.download(url, "scores.htm");
			BufferedReader in = new BufferedReader(new FileReader("scores.htm"));
			lines = new ArrayList<String>();
			String str;
			while ((str = in.readLine()) != null) {
				String date = "";
				if (str.contains("<h2 id='mainpagetitle'>")) {
					System.out.println(str);
					String[] temp = str.split("<div class='logo-small logo-nfl-small nfl-small-6'></div><strong>");
					for (String s: temp)
						System.out.println(s);
					/*temp = temp[1].split(">");
					date = (String) temp[3].subSequence(3, 9);

					title = title + " Scores and Schedule - " + date;

					// Gets 1st game
					temp = str.split("<div class=\"sub dark\">");
					temp = str.split("gameId=");
					for (int i = 1; i < temp.length; i++)
						if (!temp[i].contains("guid")) {
							// System.out.println(temp[i]);
							String[] temp1 = temp[i].split(">");
							String game = temp1[1].substring(0, temp1[1]
									.length() - 3);
							lines.add(game);
						}
*/
				}
			}
			in.close();
			File file = new File("scores.htm");
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] stringArray = { "No results for that query" };
		if (grep != null) {
			HashSet<String> set = new HashSet<String>();
			for (String word : grep) {
				for (String line : lines) {
					if (line.toLowerCase().contains(word.trim().toLowerCase()))
						set.add(line);
				}
			}
			if (!set.isEmpty()) {
				ArrayList<String> temp = new ArrayList<String>();
				for (String s : set)
					temp.add(s);
				lines = temp;
			} else
				lines = null;
		}

		if (lines != null) {
			stringArray = new String[lines.size() + 1];
			stringArray[0] = title;
			for (int i = 1; i < lines.size() + 1; i++) {
				stringArray[i] = lines.get(i - 1);
			}
		}
		return stringArray;
	}
}
