package util;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigHandler extends DefaultHandler {

	private Configuration config = new Configuration();

	private String currentElement;

	public Configuration getConfig() {
		return config;
	}

	public ConfigHandler() {

	}

	/**
	 * Gets called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		currentElement = qName;
	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		currentElement = "";
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		String temp = "";
		if (currentElement.equals("botname"))
			config.setBotName(new String(ch, start, length));
		else if (currentElement.equals("command_prefix"))
			config.setCommandPrefix(temp+ch[start]);
		else if (currentElement.equals("owner"))
			config.setOwner(new String(ch, start, length));
		else if (currentElement.equals("version"))
			config.setVersion(new String(ch, start, length));
		else if (currentElement.equals("sql_username"))
			config.setSqlUserName(new String(ch, start, length));
		else if (currentElement.equals("sql_password"))
			config.setSqlPassword(new String(ch, start, length));
		else if (currentElement.equals("sql_server_connection"))
			config.setSqlServerConnection(new String(ch, start, length));
		else if (currentElement.equals("server"))
			config.setServer(new String(ch, start, length));
		else if (currentElement.equals("channel"))
			config.addChannel(new String(ch, start, length));
		else if (currentElement.equals("logging"))
			config
					.setLogOn(Boolean
							.parseBoolean(new String(ch, start, length)));
		else if (currentElement.equals("verbose"))
			config.setVerbose(Boolean
					.parseBoolean(new String(ch, start, length)));
	}
}