package util;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class Configuration {

	private String botName;

	private String commandPrefix;

	private String owner;

	private String version;

	private String sqlUserName;

	private String sqlPassword;

	private String sqlServerConnection;

	private String server;

	private ArrayList<String> channels = new ArrayList<String>();

	private boolean logging;

	private boolean verbose;

	public Configuration() {
	}

	public boolean isLogOn() {
		return logging;
	}

	public void setLogOn(boolean logging) {
		this.logging = logging;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public ArrayList<String> getChannels() {
		return channels;
	}

	public void addChannel(String channel) {
		channels.add(channel);
	}

	public String getBotName() {
		return botName;
	}

	public void setBotName(String botName) {
		this.botName = botName;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public String getCommandPrefix() {
		return commandPrefix;
	}

	public void setCommandPrefix(String commandPrefix) {
		this.commandPrefix = commandPrefix;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSqlPassword() {
		return sqlPassword;
	}

	public void setSqlPassword(String sqlPassword) {
		this.sqlPassword = sqlPassword;
	}

	public String getSqlServerConnection() {
		return sqlServerConnection;
	}

	public void setSqlServerConnection(String sqlServerConnection) {
		this.sqlServerConnection = sqlServerConnection;
	}

	public String getSqlUserName() {
		return sqlUserName;
	}

	public void setSqlUserName(String sqlUserName) {
		this.sqlUserName = sqlUserName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String toString() {
		String result = "Bot name: "+this.getBotName()+"\n";
		result += "Server Name: "+ this.getServer() + "\n";
		result += "Command prefix: "+ this.getCommandPrefix() + "\n";
		result += "Owner: "+ this.getOwner() + "\n";
		result += "Version: "+ this.getVersion() + "\n";
		result += "Server Name: "+ this.getServer() + "\n";
		result += "Channels: \n";
		for (String s : this.getChannels())
			result += "\t" + s + "\n";
		result += "Verbose = " + this.isVerbose() + "\n";
		result += "Logging = " + this.isLogOn() + "\n";
		return result;
	}
	
	public static Configuration getConfig() {

		Configuration config = null;
		// Load configuration file
		// URL to load data from
		File url;
		try {
			url = new File("config" + File.separator + "config.xml");
			// Get a SAXParser from the SAXParserFactory
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			// Get the XMLReader of the SAXParser we created
			XMLReader xr = sp.getXMLReader();
			ConfigHandler myHandler = new ConfigHandler();
			xr.setContentHandler(myHandler);
			xr.parse(new InputSource(new FileReader(url)));
			config = myHandler.getConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

}
