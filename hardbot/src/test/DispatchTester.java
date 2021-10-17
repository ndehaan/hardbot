package test;

import java.util.HashMap;

import actions.Action;
import actions.CallsignActionOld;
import actions.DtvAction;
import actions.ECWeatherAction;
import actions.GoogleAction;
import actions.IPV4Action;
import actions.LocationAction;
import actions.MacAction;
import actions.MorseAction;
import actions.OlympicAction;
import actions.ScoresAction;
import actions.TideAction;
import actions.TimeAction;
import actions.UBCWeatherAction;
import actions.WeatherAction;
import actions.WorkTempAction;
import main.MyBot;

public class DispatchTester {

	private static HashMap<String, Action> actionMap = new HashMap<String, Action>();
	
	private static void initActions() {
		// Add weather action/formatter
		actionMap.put(new WeatherAction().getName(), new WeatherAction());
		// Add ecweather action/formatter
		actionMap.put(new ECWeatherAction().getName(), new ECWeatherAction());
		// Add location action/formatter
		actionMap.put(new LocationAction().getName(), new LocationAction());
		// Add ipv4 action/formatter
		actionMap.put(new IPV4Action().getName(), new IPV4Action());
		// add ubcweather stuff
		actionMap.put(new UBCWeatherAction().getName(), new UBCWeatherAction());
		// add time stuff
		actionMap.put("time", new TimeAction());
		// add scores stuff
		actionMap.put("scores", new ScoresAction());
		// add MAC stuff
		actionMap.put("mac", new MacAction());
		// add callsign stuff (and alias)
		actionMap.put("c", new CallsignActionOld());
		actionMap.put(new CallsignActionOld().getName(), new CallsignActionOld());
		// add Morse stuff
		actionMap.put(new MorseAction().getName(), new MorseAction());
		// add tide stuff
		actionMap.put(new TideAction().getName(), new TideAction());
		// add DTV stuff
		actionMap.put(new DtvAction().getName(), new DtvAction());
		// add Olympic countdown
		actionMap.put(new OlympicAction().getName(), new OlympicAction());
		// add Work temperatures
		actionMap.put(new WorkTempAction().getName(), new WorkTempAction());
		// add Google search
		actionMap.put(new GoogleAction().getName(), new GoogleAction());
	}
	public static void main(String[] args) throws Exception {
		initActions();
		String command = "olympics";
		String parameters = "";
		
		Action action = actionMap.get(command);
		String[] results = action.perform(parameters,"wb", new MyBot());
		
		for (String s : results)
			System.out.println(s);
		
	}

}
