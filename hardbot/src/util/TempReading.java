package util;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TempReading {
	private Timestamp timestamp;
	private double internalTemp;
	private double serverRoomTemp;
	private double officeTemp;
	private static SimpleDateFormat outputFormatter = new SimpleDateFormat(
			"EEEEE, MMM dd, yyyy  hh:mm:ss aa");

	public TempReading() {
	}

	public TempReading(Timestamp timestamp, double internalTemp,
			double serverRoomTemp, double officeTemp) {
		setTimestamp(timestamp);
		setInternalTemp(internalTemp);
		setServerRoomTemp(serverRoomTemp);
		setOfficeTemp(officeTemp);
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public double getInternalTemp() {
		return internalTemp;
	}

	public void setInternalTemp(double internalTemp) {
		this.internalTemp = internalTemp;
	}

	public double getServerRoomTemp() {
		return serverRoomTemp;
	}

	public void setServerRoomTemp(double serverRoomTemp) {
		this.serverRoomTemp = serverRoomTemp;
	}

	public double getOfficeTemp() {
		return officeTemp;
	}

	public void setOfficeTemp(double officeTemp) {
		this.officeTemp = officeTemp;
	}

	public boolean equals(TempReading tr) {
		return tr.getTimestamp().equals(getTimestamp());
	}

	public String toString() {
		return outputFormatter.format(getTimestamp()) + "\n" + "Internal: "
				+ getInternalTemp() + " C\n" + "External: "
				+ getServerRoomTemp() + " C\n" + "Office  : " + getOfficeTemp()
				+ " C\n";
	}

}
