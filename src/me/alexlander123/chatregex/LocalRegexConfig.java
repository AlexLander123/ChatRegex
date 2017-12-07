package me.alexlander123.chatregex;


import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocalRegexConfig extends RegexConfig{
	
	private Location location;
	private int radius;
	
	public LocalRegexConfig(Location location, int radius, Pattern regex, List<CommandEntry> commands, int action,  int cooldown, boolean globalCooldown, String addNode, String node) {
		super(regex, commands, action, cooldown, globalCooldown, addNode, node);
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
	
	@Override
	public boolean canExecute(UUID player) {
		return super.canExecute(player) && Bukkit.getPlayer(player).getWorld() == getLocation().getWorld() && Bukkit.getPlayer(player).getLocation().distance(getLocation()) <= getRadius();
	}

}
