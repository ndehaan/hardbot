package util;

public class ForecastResult {

	private String condition;

	private String day;

	private int low;

	private int high;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		if (day.equalsIgnoreCase("Today"))
			day = "Today:    ";
		else if (day.equalsIgnoreCase("Mon"))
			day = "Monday:   ";
		else if (day.equalsIgnoreCase("Tue"))
			day = "Tuesday:  ";
		else if (day.equalsIgnoreCase("Wed"))
			day = "Wednesday:";
		else if (day.equalsIgnoreCase("Thu"))
			day = "Thursday: ";
		else if (day.equalsIgnoreCase("Fri"))
			day = "Friday:   ";
		else if (day.equalsIgnoreCase("Sat"))
			day = "Saturday: ";
		else if (day.equalsIgnoreCase("Sun"))
			day = "Sunday:   ";
		this.day = day;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public String toString() {
		return getDay() + " " + getCondition() + ", High: " + getHigh() + "°C, Low: "
				+ getLow() + "°C.";
	}

}
