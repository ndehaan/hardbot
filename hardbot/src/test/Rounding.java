package test;

import java.text.DecimalFormat;

public class Rounding {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 
		//this is to round up
		DecimalFormat myFormat=new DecimalFormat("0"); 
		 
		//this is to round up to 2 decimal places
		//DecimalFormat myFormat=new DecimalFormat("0.00"); 
		 
		int temp = Integer.parseInt(myFormat.format(33.5000000000000000000000000000000001));
		System.out.println(temp);

	}

}
