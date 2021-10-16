package thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.MyBot;

public class MailSpoolChecker implements Runnable {
	private static final String PATH = "//var//spool//mail//";
	private String mailPath;
	private String channel;
	private MyBot bot;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd MMM yyyy HH:mm:ss ZZZ");
	private File outputFile = new File("lastDate.txt");

	public MailSpoolChecker(String username, MyBot bot, String channel) {
		setChannel(channel);
		setMailPath(PATH + username);
		this.bot = bot;
	}

	public void setMailPath(String username) {
		this.mailPath = username;
	}

	public String getMailPath() {
		return mailPath;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}

	private Date readDate() {
		Date result = null;
		FileReader fr;
		try {
			fr = new FileReader(outputFile);
			BufferedReader br = new BufferedReader(fr);
			result = dateFormatter.parse(br.readLine());
		} catch (Exception e) {
			System.err.println("Error reading date!");
			e.printStackTrace();
		}
		return result;
	}

	private void writeDate(Date date) {
		FileWriter fw;
		try {
			fw = new FileWriter(outputFile);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(dateFormatter.format(date));
			bw.flush();
			fw.close();
		} catch (IOException e) {
			System.err.println("Error keeping track of dates!");
			e.printStackTrace();
		}
	}

	private void sendMessage(String channel, String message, Date date) {
		if (date.getTime() > readDate().getTime()) {
			bot.sendMessage("#scanbc", message);
			writeDate(date);
		}
	}

	public void run() {
		while (true) {
			File spoolFile = new File(getMailPath());
			FileReader fr = null;
			try {
				fr = new FileReader(spoolFile);
				BufferedReader br = new BufferedReader(fr);
				String currentLine = null;
				boolean subjectSeen = false;
				String currDateString = null;
				Date currentDate = null;
				while ((currentLine = br.readLine()) != null) {
					if (currentLine.startsWith("Date:")) {
						if (currentLine.contains("("))
							currentLine = currentLine.substring(0,
									currentLine.length() - 5).trim();
						currentLine = currentLine.substring(5).trim();
						currDateString = currentLine.split(",")[1].trim();
					} else if (currentLine.contains("From"))
						subjectSeen = false;
					else if (subjectSeen && !currentLine.trim().equals("")) {
						try {
							currentDate = dateFormatter.parse(currDateString);
							this.sendMessage(getChannel(), currentLine,
									currentDate);
						} catch (Exception e) {
							System.err.println("CurrDateString: "
									+ currDateString);
							System.err.println(currentLine);
						}
					} else if (currentLine.contains("Subject: BCC"))
						subjectSeen = true;
				}
			} catch (Exception e) {
				System.err
						.println("An error occurred while reading the mail spool file.");
				e.printStackTrace();
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				System.err
						.println("Something happened while sleeping/waking the mailspool thread!");
				e.printStackTrace();
			}
		}
	}
}
