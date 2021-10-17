package actions;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import main.MyBot;
import util.FileDownload;

public class AreaCodeAction extends Action {

	@Override
	public String getName() {
		return "npa";
	}

	@Override
	public String[] help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] perform(String request, String sender, MyBot bot) {
		String[] result = new String[1];
		String[] temp = request.split("-");
		int areaCode = 0;
		int exchange = 0;
		try {
			areaCode = Integer.parseInt(temp[0]);
			exchange = Integer.parseInt(temp[1]);
		} catch (Exception e) {
			result[0] = "Please use the following format for your queries: 604-555";
			return result;
		}
		// example
		// http://www.localcallingguide.com/xmlprefix.php?npa=778&nxx=995
		String url = "http://www.localcallingguide.com/xmlprefix.php?npa="
				+ areaCode + "&nxx=" + exchange;
		FileDownload.download(url, "xmlprefix.xml");
		File file = new File("xmlprefix.xml");
		String tempResult = "";
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			
			NodeList nl = doc.getElementsByTagName("prefixdata").item(0).getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				tempResult += nl.item(i).getTextContent().trim()+" ";
			}
		} catch (Exception e) {
			result[0] = "Unable to parse result.";
			return result;
		}
		result[0] = tempResult;
		return result;
	}
}
