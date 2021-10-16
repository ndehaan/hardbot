package util.ECWeather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ECSitesHandler extends DefaultHandler {

	private String currentSiteCode;
	
	private String siteCode;

	private String searchCity;

	private String searchProvince;

	private boolean nameEn = false;

	public String getCode() {
		return siteCode;
	}

	public ECSitesHandler(String searchCity, String searchProvince) {
		this.searchProvince = searchProvince;
		if (searchCity.equalsIgnoreCase("Montreal"))
			this.searchCity = "Montr�al";
		else if (searchCity.equalsIgnoreCase("Saint-Remi"))
			this.searchCity = "Saint-R�mi";
		else if (searchCity.equalsIgnoreCase("Bernieres"))
			this.searchCity = "Berni�res";
		else if (searchCity.equalsIgnoreCase("Riviere-du-Loup"))
			this.searchCity = "Rivi�re-du-Loup";
		else if (searchCity.equalsIgnoreCase("Saint-Nicephore"))
			this.searchCity = "Saint-Nic�phore";
		else if (searchCity.equalsIgnoreCase("Trois-Rivieres"))
			this.searchCity = "Trois-Rivi�res";
		else if (searchCity.equalsIgnoreCase("Becancour"))
			this.searchCity = "B�cancour";
		else if (searchCity.equalsIgnoreCase("Ottawa"))
			this.searchCity = "Ottawa (Kanata - Orl�ans)";
		else if (searchCity.equalsIgnoreCase("Gaspe"))
			this.searchCity = "Gasp�";
		else
			this.searchCity = searchCity;
	}

	/**
	 * Gets called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (qName.equalsIgnoreCase("site"))
			currentSiteCode = atts.getValue("code");
		else if (qName.equalsIgnoreCase("nameEn"))
			nameEn = true;
	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (qName.equalsIgnoreCase("site"))
			currentSiteCode = "";
		else if (qName.equalsIgnoreCase("nameEn"))
			nameEn = false;
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		if (nameEn && (new String(ch, start, length)).equalsIgnoreCase(searchCity)) {
			siteCode = currentSiteCode;
		}
	}
}