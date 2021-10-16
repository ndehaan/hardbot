package actions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import util.Configuration;
import util.TempReading;

public class WorkTempAction extends Action {
	private static SimpleDateFormat outputFormatter = new SimpleDateFormat(
			"EEEEE, MMM dd, yyyy  hh:mm:ss aa");

	@Override
	public String getName() {
		return "worktemps";
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the temperatures in the server room and main office at Wiredboy's workplace.",
				"Usage: Type \"" + command + getName() + "\"" };
	}

	@Override
	public String[] perform(String request, String sender) {
		TempReading tr = new TempReading();
		try {
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM `log` ORDER BY `log` . `date` DESC LIMIT 1");
			rs.first();
			tr.setTimestamp(rs.getTimestamp(1));
			tr.setInternalTemp(rs.getDouble(2));
			tr.setServerRoomTemp(rs.getDouble(3));
			tr.setOfficeTemp(rs.getDouble(4));

		} catch (Exception e) {
			e.printStackTrace();
			return new String[] { "Error retrieving data." };
		}
		((Double) tr.getServerRoomTemp()).toString();
		return new String[] { outputFormatter.format(tr.getTimestamp()),
				"Server Room: " + ((Double) tr.getServerRoomTemp()).toString()+"°C",
				"Server Rack: " + ((Double) tr.getInternalTemp()).toString()+"°C",
				"Main Office: " + ((Double) tr.getOfficeTemp()).toString()+"°C" };
	}

	private Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://64.180.60.170/templog", "root", "878extra");
		return con;
	}
}
