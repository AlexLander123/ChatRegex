package me.alexlander123.chatregex;


import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Location;

public class LocalRegexConfig extends RegexConfig{
	
	private Location location;
	private int radius;
	
	public LocalRegexConfig(Location location, int radius, Pattern regex, List<CommandEntry> commands, int action) {
		super(regex, commands, action);
		this.setLocation(location);
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
