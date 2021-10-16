package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.jibble.pircbot.PircBot;

import actions.*;

import util.Configuration;

public class MyBot extends PircBot {

	// Action map to hold all the possible actions
	private HashMap<String, Action> actionMap = new HashMap<String, Action>();

	// Set the default prefix
	private String commandPrefix = "%";

	// Set the default SQL logging preference
	private boolean logging = false;

	// Set the default SQL information
	private String SQLuser;
	private String SQLpass;
	private String SQLurl;

	public MyBot() throws Exception {
		initialize();
	}

	private void initialize() {
		Configuration config = Configuration.getConfig();
		setName(config.getBotName());
		if (config.isLogOn() && config.getSqlUserName() != null
				&& config.getSqlPassword() != null
				&& config.getSqlUserName() != null) {
			setLogging(true);
			setSQLurl(config.getSqlServerConnection());
			setSQLuser(config.getSqlUserName());
			setSQLpass(config.getSqlPassword());
		}

		setCommandPrefix(config.getCommandPrefix());
		setLogin(config.getBotName());
		setVersion(config.getVersion());
		setFinger(config.getBotName());
		setAutoNickChange(true);
		initActions();
	}

	private void initActions() {
		// Add weather action/formatter
		//actionMap.put(new WeatherAction().getName(), new WeatherAction());
		// Add ecweather action/formatter
		//actionMap.put(new ECWeatherAction().getName(), new ECWeatherAction(this));
		// Add location action/formatter
		//actionMap.put(new LocationAction().getName(), new LocationAction());
		// Add ipv4 action/formatter
		//actionMap.put(new IPV4Action().getName(), new IPV4Action());
		// add ubcweather stuff
		//actionMap.put(new UBCWeatherAction().getName(), new UBCWeatherAction());
		// add time stuff
		actionMap.put("time", new TimeAction());
		// add scores stuff
		//actionMap.put("scores", new ScoresAction());
		// add MAC stuff
		//actionMap.put("mac", new MacAction());
		// add callsign stuff (and alias)
		actionMap.put("c", new CallsignAction());
		actionMap.put(new CallsignAction().getName(), new CallsignAction());
		// add Morse stuff
		actionMap.put(new MorseAction().getName(), new MorseAction());
		// add tide stuff
		//actionMap.put(new TideAction().getName(), new TideAction());
		// add DTV stuff
		// actionMap.put(new DtvAction().getName(), new DtvAction());
		// add Olympic countdown
		// actionMap.put(new OlympicAction().getName(), new OlympicAction());
		// add Work temperatures
		// actionMap.put(new WorkTempAction().getName(), new WorkTempAction());
		// add Google search
		// actionMap.put(new GoogleAction().getName(), new GoogleAction());
		// add area code search
		// actionMap.put(new AreaCodeAction().getName(), new AreaCodeAction());
		// add airport stuff
		//actionMap.put(new AirportAction().getName(), new AirportAction());
		// add sunrise/set stuff
		//actionMap.put(new SunAction().getName(), new SunAction());
		//actionMap.put(new RxslAction().getName(), new RxslAction());
	}

	private void doAction(String action, String request, String channel,
			String sender) {
		// Retrieve the appropriate action and formatter
		Action a = actionMap.get(action);
		// Perform the action
		String[] b = (String[]) a.perform(request, sender, this);
		// Format the results
		for (String s : b)
			this.sendMessage(channel, s);
	}

	private void doHelp(String action, String request, String channel) {
		// Retrieve the appropriate action and formatter
		if (request.equals("")) {
			sendMessage(channel, "Hi, I'm " + getNick() + ", "
					+ Configuration.getConfig().getOwner()
					+ "'s information bot built on the PircBot platform.");
			TreeSet<String> commands = getCommandList();
			Object[] commandArray = commands.toArray();
			String list = "";
			for (int i = 0; i < commandArray.length; i++) {
				list += commandArray[i].toString();
				if (i < commandArray.length - 1)
					list += ", ";
			}
			sendMessage(channel, "The following commands are available: "
					+ list);
			return;
		}
		if (actionMap.containsKey(request)) {
			Action a = actionMap.get(request);
			// Send each line of the results to the channel
			for (String s : a.help())
				sendMessage(channel, s);
		} else {
			sendMessage(channel, "No help found for that topic.");
		}
	}

	private TreeSet<String> getCommandList() {
		TreeSet<String> output = new TreeSet<String>();
		Set<Entry<String, Action>> entries = actionMap.entrySet();
		for (Entry<String, Action> e : entries) {
			output.add(((Action) e.getValue()).getName());
		}
		return output;
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		// normalise the input string
		message = message.trim();
		if (isLogging()) {
			logMessage(channel, sender, login, hostname, message);
		}
		boolean op = isOwner(sender);

		if (message.startsWith(getCommandPrefix())) {
			doCommand(op, channel, message.substring(1), hostname, sender);
		} else if (message.length() == 4
				&& message.endsWith("ing")
				&& !(sender.equalsIgnoreCase("WifiFred") || sender
						.equalsIgnoreCase("inhiding"))) {
			message = message.charAt(0) + "ong";
			sendMessage(channel, message);
		} else if (message.startsWith("?")) {
			doLookup(op, channel, message.substring(1), hostname);
		}
	}

	// method to support knowledge-base lookups
	private void doLookup(boolean op, String channel, String substring,
			String hostname) {
		// TODO Auto-generated method stub

	}

	public void onAction(String sender, String login, String hostname,
			String target, String action) {
		logMessage(target, sender, login, hostname, sender + " " + action);
	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {
		if (channel.equalsIgnoreCase("#thecube")
				&& sender.toLowerCase().startsWith("anon")) {
			sendNotice(sender,
					"Hello and welcome! We're not always here, but do say hi!");
			sendNotice(
					sender,
					"If you have any general inquiries and no one's around, feel free to email csss@thecube.ca with your questions or concerns!");
		}
	}

	private void logMessage(String channel, String sender, String login,
			String hostname, String message) {
		Statement st = null;
		Connection con = getConnection();
		try {
			String sqlMessage = message.replace("'", "''");
			st = con.createStatement();
			st
					.executeUpdate("INSERT INTO `randall`.`log` (`channel`,`sender`,`hostname`,`message`) VALUES ('"
							+ channel
							+ "', '"
							+ sender
							+ "', '"
							+ hostname
							+ "', '" + sqlMessage + "');");
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public void onPrivateMessage(String sender, String login, String hostname,
			String message) {
		onMessage(sender, sender, login, hostname, message);
	}

	private void doCommand(boolean op, String channel, String message,
			String hostname, String sender) {
		String command = "";
		String parameter = "";
		// take the first word as the command
		command = message.split(" ")[0].toLowerCase();
		// take the rest of the input as arguments
		parameter = message.substring(command.length()).trim();
		if (op) {
			if (command.equalsIgnoreCase("quit")) {
				disconnect();
				System.exit(0);
			}
			if (command.equalsIgnoreCase("join")) {
				joinChannel(parameter);
				return;
			}
			if (command.equalsIgnoreCase("part")) {
				partChannel(parameter);
				return;
			}
			if (command.equalsIgnoreCase("say")) {
				channel = parameter.split(" ")[0];
				sendMessage(channel, parameter.substring(channel.length())
						.trim());
				return;
			}
			if (command.equalsIgnoreCase("nick")) {
				changeNick(parameter);
				return;
			}
			if (command.equalsIgnoreCase("reset")) {
				initialize();
				return;
			}

		}
		if (command.equalsIgnoreCase("help")) {
			doHelp(command, parameter, channel);
			return;
		}
		if (actionMap.containsKey(command)) {
			// the location command requires an extra parameter
			if (command.equalsIgnoreCase("location")) {
				parameter = hostname + "," + parameter;
			}
			doAction(command, parameter, channel, sender);
			return;
		}

	}

	private boolean isOwner(String sender) {
		return sender.equalsIgnoreCase(getOwner());
	}

	private boolean isLogging() {
		return logging;
	}

	private void setLogging(boolean logging) {
		this.logging = logging;
	}

	private String getOwner() {
		return Configuration.getConfig().getOwner();
	}

	private String getCommandPrefix() {
		return commandPrefix;
	}

	private void setCommandPrefix(String command_prefix) {
		this.commandPrefix = command_prefix;
	}

	/**
	 * @return the sQLuser
	 */
	private String getSQLuser() {
		return SQLuser;
	}

	/**
	 * @param luser
	 *            the sQLuser to set
	 */
	private void setSQLuser(String luser) {
		SQLuser = luser;
	}

	/**
	 * @return the sQLpass
	 */
	private String getSQLpass() {
		return SQLpass;
	}

	/**
	 * @param lpass
	 *            the sQLpass to set
	 */
	private void setSQLpass(String lpass) {
		SQLpass = lpass;
	}

	/**
	 * @return the sQLurl
	 */
	private String getSQLurl() {
		return SQLurl;
	}

	/**
	 * @param lurl
	 *            the sQLurl to set
	 */
	private void setSQLurl(String lurl) {
		SQLurl = lurl;
	}

	private Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(this.getSQLurl(), getSQLuser(),
					getSQLpass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public void onDisconnect() {
		Configuration config = Configuration.getConfig();
		while (!isConnected()) {
			try {
				reconnect();
				for (String s : config.getChannels()) {
					joinChannel(s);
				}
				changeNick(config.getBotName());
			} catch (Exception e) {
				System.err
						.println("Couldn't connect... pausing for 5 seconds.");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					System.err.println("Couldn't sleep the thread.");
				}
			}
		}
	}

	/*
	 * private void doLS(String channel, String parameter) { Runtime rt =
	 * 
	 * Runtime.getRuntime(); Process proc = null; try { proc =
	 * rt.exec("ls -ahl"); } //proc = rt.exec("cmd.exe /C dir"); catch
	 * (IOException e) { e.printStackTrace(); } InputStream in =
	 * proc.getInputStream(); BufferedInputStream buf = new
	 * BufferedInputStream(in); InputStreamReader ir = new
	 * InputStreamReader(buf); BufferedReader br = new BufferedReader(ir);
	 * String line; ArrayList<String> results = new ArrayList<String>(); try {
	 * while ((line = br.readLine()) != null) { results.add(line); } } catch
	 * (IOException e) { e.printStackTrace(); } for (String s : results)
	 * this.sendMessage(channel, s); }
	 */
}
