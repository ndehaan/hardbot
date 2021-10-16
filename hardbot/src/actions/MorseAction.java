package actions;

import java.util.HashMap;
import java.util.Set;

import main.MyBot;
import util.Configuration;

public class MorseAction extends Action {

	@Override
	public String getName() {
		return "morse";
	}

	@Override
	public String[] help() {
		String command = Configuration.getConfig().getCommandPrefix();
		return new String[] {
				"Returns the morse code translation of the entered text.",
				"Usage: Type \"" + command + this.getName()
						+ "\" followed by the text to translate." };
	}

	@Override
	public String[] perform(String request, String sender, MyBot myBot) {
		request = request.trim().toUpperCase();
		HashMap<Character, String> code = getCodeMap();
		HashMap<String, Character> text = getTextMap(code);
		String result = "";
		if (!request.matches("^[. |-]*$"))
			for (char c : request.toCharArray()) {
				if (code.containsKey(c))
					result += code.get(c);
				else
					result += "?";
				result += " ";
			}
		else {
			String[] words = request.split(" {2,}");
			for (String s : words) {
				String[] letters = s.split(" ");
				for (String t : letters) {
					result += text.get(t);
				}
				result+=" ";
			}
		}

		return new String[] { result };
	}

	private HashMap<Character, String> getCodeMap() {
		HashMap<Character, String> code = new HashMap<Character, String>();
		code.put(new Character('A'), ".-");
		code.put(new Character('B'), "-...");
		code.put(new Character('C'), "-.-.");
		code.put(new Character('D'), "-..");
		code.put(new Character('E'), ".");
		code.put(new Character('F'), "..-.");
		code.put(new Character('G'), "--.");
		code.put(new Character('H'), "....");
		code.put(new Character('I'), "..");
		code.put(new Character('J'), ".---");
		code.put(new Character('K'), "-.-");
		code.put(new Character('L'), ".-..");
		code.put(new Character('M'), "--");
		code.put(new Character('N'), "-.");
		code.put(new Character('O'), "---");
		code.put(new Character('P'), ".--.");
		code.put(new Character('Q'), "--.-");
		code.put(new Character('R'), ".-.");
		code.put(new Character('S'), "...");
		code.put(new Character('T'), "-");
		code.put(new Character('U'), "..-");
		code.put(new Character('V'), "...-");
		code.put(new Character('W'), ".--");
		code.put(new Character('X'), "-..-");
		code.put(new Character('Y'), "-.--");
		code.put(new Character('Z'), "--..");
		code.put(new Character('1'), ".----");
		code.put(new Character('2'), "..---");
		code.put(new Character('3'), "...--");
		code.put(new Character('4'), "....-");
		code.put(new Character('5'), ".....");
		code.put(new Character('6'), "-....");
		code.put(new Character('7'), "--...");
		code.put(new Character('8'), "---..");
		code.put(new Character('9'), "----.");
		code.put(new Character('0'), "-----");
		code.put(new Character('.'), ".-.-.-");
		code.put(new Character('!'), "-.-.--");
		code.put(new Character(','), "--..--");
		code.put(new Character(':'), "---...");
		code.put(new Character('?'), "..--..");
		code.put(new Character('\''), ".----.");
		code.put(new Character('`'), ".----."); // treat ` as '
		code.put(new Character('-'), "-....-");
		code.put(new Character('/'), "-..-.");
		code.put(new Character('('), "-.--.-");
		code.put(new Character(')'), "-.--.-");
		code.put(new Character('"'), ".-..-.");
		code.put(new Character('='), "-...-");
		code.put(new Character('+'), ".-.-.");
		code.put(new Character(';'), "-.-.-.");
		code.put(new Character('_'), "..--.-");
		code.put(new Character('$'), "...-..-");
		code.put(new Character('@'), ".--.-.");
		code.put(new Character(' '), " ");
		return code;
	}

	private HashMap<String, Character> getTextMap(
			HashMap<Character, String> code) {
		HashMap<String, Character> hText = new HashMap<String, Character>();
		Set<Character> set = code.keySet();
		for (Character c : set) {
			String s = code.get(c);
			hText.put(s, c);
		}
		return hText;
	}
}
