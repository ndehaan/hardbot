package actions;

import java.util.ArrayList;

import main.MyBot;

public class RxslAction extends Action {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "rxsl";
	}

	@Override
	public String[] help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] perform(String request, String sender, MyBot bot) {
		System.out.println(request);
		String[] requestArray = request.split(" ");
		if (requestArray.length < 3)
			return new String[] { "specify: <frequency> <xmit power in dBm> <ant gain in dBi> <starting mile> <ending mile> <rx antenna gain>" };
		double freq = Double.parseDouble(requestArray[0]);
		double txdb = Double.parseDouble(requestArray[1]);
		int txloss = 0;
		double txgain = Double.parseDouble(requestArray[2]);
		double rxgain = txgain;
		double miles = 0;
		if (requestArray.length > 3)
			miles = Double.parseDouble(requestArray[3]) - 1;
		if (miles < 0)
			miles = 0;
		System.out.println("Miles "+miles);
		double maxmiles = 0;
		if (requestArray.length > 4)
			maxmiles = Integer.parseInt(requestArray[4]);
		else
			maxmiles = miles + 5;
		if (maxmiles < miles)
			return new String[] { "try again" };
		System.out.println("Max miles "+maxmiles);
		if (requestArray.length > 5)
			rxgain = Double.parseDouble(requestArray[5]);
		if (maxmiles == miles)
			maxmiles++;
		double mmod = 1;
		mmod = (maxmiles - miles) / 5;

		int rxloss = 0;
		ArrayList<String> returnList = new ArrayList<String>();
		returnList.add(freq + "MHz, " + txdb + "dB xmit, tx gain " + txgain
				+ "dBi, rx gain " + rxgain + "dBi, no cable loss.");
		double rxsl = 0;
		do {
			if (miles > maxmiles)
				return returnList.toArray(new String[returnList.size()]);
			miles += mmod;
			double fsl = (20 * Math.log10(freq)) + (20 * Math.log10(miles))
					+ 36.6;
			rxsl = ((txdb - txloss) + txgain) - fsl + rxgain - rxloss;
			returnList.add(miles+" miles: "+Math.round(rxsl)+"dBm");
			if (rxsl < -150)
				returnList.add(miles + "miles: signal below -150dBm.");
		} while (rxsl > -100);
		return returnList.toArray(new String[returnList.size()]);
	}

}
