package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SpoolReadTest {

	private static SimpleDateFormat inputFormatter = new SimpleDateFormat(
			"dd MMM yyyy HH:mm:ss ZZZ");
	
	private static File outputFile = new File ("outputFile.txt");

	public static void main(String[] args) throws IOException {
		if (!outputFile.exists())
			outputFile.createNewFile();
		Read();

	}

	private static void writeDate(Date date) throws IOException {
		FileWriter fw = new FileWriter(outputFile);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(inputFormatter.format(date));
		bw.flush();
		fw.close();
		
	}

	private static void Read() {
		File spoolFile = new File("ipnspool");
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
						currentDate = inputFormatter.parse(currDateString);
					} catch (Exception e) {
						System.err.println("CurrDateString: " + currDateString);
						System.err.println(currentLine);
					}
					System.out.println("[INCIDENT DATE] "
							+ inputFormatter.format(currentDate));
					System.out.println("[INCIDENT PAGE] " + currentLine);
					writeDate(currentDate);
				} else if (currentLine.contains("Subject: BCC"))
					subjectSeen = true;
			}
		} catch (Exception e) {
			System.err
					.println("An error occurred opening the mail spool file.");
			e.printStackTrace();
		}
	}
}
