package me.alexlander123.chatregex;


import java.util.List;
import java.util.regex.Pattern;

public class LocationConfig {
	
	private int radius;
	private Pattern regex;
	private List<String> commands;
	
	public LocationConfig(int radius, Pattern regex, List<String> commands) {
		this.radius = radius;
		this.commands = commands;
		this.regex = regex;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Pattern getRegex() {
		return regex;
	}

	public void setRegex(Pattern regex) {
		this.regex = regex;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}
}
