package me.alexlander123.chatregex;


import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class RegexConfig {
	
	private Pattern regex;
	private List<CommandEntry> commands;
	private int action;
	private int cooldown;
	private boolean globalCooldown;
	private HashMap<UUID, Long> lastExecutionMap;
	private long lastExecutionTime;
	
	public RegexConfig(Pattern regex, List<CommandEntry> commands, int action, int cooldown, boolean globalCooldown) {
		this.commands = commands;
		this.regex = regex;
		this.action = action;
		this.cooldown = cooldown;
		this.globalCooldown = globalCooldown;
		if(globalCooldown == true) {
			lastExecutionTime = 0L;
		} 
		else {
			lastExecutionMap = new HashMap<UUID, Long>();
		}
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
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
	
	public boolean canExecute(UUID player) {
		if(globalCooldown == true) {
			return (System.currentTimeMillis() >= (lastExecutionTime + (cooldown * 1000))) ? true : false;
		}
		else {
			if(lastExecutionMap.containsKey(player)) {
				return (System.currentTimeMillis() >= (lastExecutionMap.get(player) + (cooldown * 1000))) ? true : false; 
			}
			else {
				return true;
			}
		}
	}

	public void setLastExecutionTime(UUID player) {
		if(globalCooldown == true) {
			this.lastExecutionTime = System.currentTimeMillis();
		}
		else {
			if(lastExecutionMap.containsKey(player)) {
				lastExecutionMap.replace(player, System.currentTimeMillis());
			}
			else {
				lastExecutionMap.put(player, System.currentTimeMillis());
			}
		}
	}
	
}
