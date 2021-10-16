package util;

public class Callsign {
	private String callsign;
	private String surname;
	private String givenNames;
	private String streetAddress;
	private String city;
	private String province;
	private String postalCode;
	private String licenseType;
	private String clubName;

	public Callsign(String callsign, String surname, String givenNames, String streetAddress, String city,
			String province, String postalCode) {
		this.setCallsign(callsign);
		this.setSurname(surname);
		this.setGivenNames(givenNames);
		this.setStreetAddress(streetAddress);
		this.setCity(city);
		this.setProvince(province);
		this.setPostalCode(postalCode);
	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGivenNames() {
		return givenNames;
	}

	public void setGivenNames(String givenNames) {
		this.givenNames = givenNames;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public void addQualification(String qual) {
		if (licenseType==null)
			licenseType = qual+" ";
		else
			licenseType += qual + " ";
	}

	@Override
	public String toString() {
		if (getClubName()!=null) {
			return callsign + " - " + surname + ", " + streetAddress
					+ ", " + city + ", " + province + ", " + postalCode + ", "
					+ licenseType;
		}
		return callsign + " - " + surname + " " + givenNames.trim() + ", " + streetAddress
				+ ", " + city + ", " + province + ", " + postalCode + ", "
				+ licenseType;
	}
}
