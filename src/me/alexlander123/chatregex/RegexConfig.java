package me.alexlander123.chatregex;


import java.util.List;
import java.util.regex.Pattern;

public class RegexConfig {
	
	private Pattern regex;
	private List<CommandEntry> commands;
	private int action;
	
	public RegexConfig(Pattern regex, List<CommandEntry> commands, int action) {
		this.commands = commands;
		this.regex = regex;
		this.action = action;
	}

	public Pattern getRegex() {
		return regex;
	}

	public void setRegex(Pattern regex) {
		this.regex = regex;
	}

	public List<CommandEntry> getCommands() {
		return commands;
	}

	public void setCommands(List<CommandEntry> commands) {
		this.commands = commands;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
}
