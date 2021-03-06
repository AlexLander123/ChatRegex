package me.alexlander123.chatregex;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RegexConfig {
	
	private Pattern regex;
	private List<CommandEntry> commands;
	private int action;
	private int cooldown;
	private boolean globalCooldown;
	private HashMap<UUID, Long> lastExecutionMap;
	private long lastExecutionTime;
	private List<String> addNodes;
	private List<String> nodes;
	private List<String> permissionsToHave;
	private List<String> permissionsNotToHave;
	
	public RegexConfig(Pattern regex, List<CommandEntry> commands, List<String> permissions, int action, int cooldown, boolean globalCooldown, String addNode, String node) {
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
		
		//Parse Nodes To Be Added To Player When The REGEX Matches
		if(!addNode.isEmpty() || !(addNode == null)) {
			addNodes = Arrays.asList(addNode.split(",\\s*"));
			for (int i = 0; i < addNodes.size(); i++) {
				addNodes.set(i, addNodes.get(i).trim());
			}
		}
		
		//Parse Nodes
		if(!node.isEmpty()) {
			nodes = Arrays.asList(node.split(",\\s*"));
			for (int i = 0; i < nodes.size(); i++) {
				nodes.set(i, nodes.get(i).trim());
			}
		}
		
		//Parse permissions
		if(!permissions.isEmpty()) {
			
			permissionsToHave = new ArrayList<String>();
			permissionsNotToHave = new ArrayList<String>();
			
			for(String permission : permissions) {
				if(permission.startsWith("-")) {
					permissionsNotToHave.add(permission.substring(1));
				}
				else {
					permissionsToHave.add(permission);
				}
			}
		}
	}
	
	public boolean canExecute(UUID player){
		return checkTime(player) && checkNodes(player) && checkPermissions(player);
	}
	
	public boolean checkTime(UUID player){
		if(globalCooldown == true) {
			return (System.currentTimeMillis() >= (lastExecutionTime + (cooldown * 1000))) ? true : false;
		}
		else {
			if (lastExecutionMap.containsKey(player)){
				return (System.currentTimeMillis() >= (lastExecutionMap.get(player) + (cooldown * 1000))) ? true : false;
			}
			return true;
		}
	}
	
	public boolean checkNodes(UUID player){
		
		//Check if there is a requirement of nodes
		if(nodes == null || nodes.isEmpty()) {
			return true; //No requirement of nodes 
		}
		
		HashMap<UUID, List<String>> playerNodes = ChatRegex.playerNodes;
		
		if(playerNodes.get(player) != null){
			if(nodes.containsAll(playerNodes.get(player))){
				return true;
			}
			else{ 
				ChatRegex.playerNodes.remove(player);
				return false;
			}
		}
		return false;
	}
	
	public boolean checkPermissions(UUID playerUUID) {
		
		Player player = Bukkit.getPlayer(playerUUID);
		
		//Check if there is a requirement of permissions
		if(permissionsToHave != null || !permissionsToHave.isEmpty()) {
			//Check if player has required permissions
			for(String permission : permissionsToHave) {
				if(!player.hasPermission(permission)) {
					return false;
				}
			}
		}
		
		//Check if there is a requirement to not have certain permissions
		if(permissionsNotToHave != null || !permissionsNotToHave.isEmpty()) {
			//Check if player has permissions that they must not have
			for(String permission : permissionsNotToHave) {
				if(player.hasPermission(permission)) {
					return false;
				}
			}
		}
		
		return true;
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
	
	public void setLastExecutionTime(UUID player){
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
	
	public void addNodes(UUID player) {
		HashMap<UUID, List<String>> playerNodes =  ChatRegex.playerNodes;
		playerNodes.putIfAbsent(player, new ArrayList<String>());
		for(String addNode : addNodes){
			if(!playerNodes.get(player).contains(addNode)) {
				playerNodes.get(player).add(addNode);
			}
		}
	}

	public List<String> getPermissionsToHave() {
		return permissionsToHave;
	}

	public void setPermissionsToHave(List<String> permissionToHave) {
		this.permissionsToHave = permissionToHave;
	}

	public List<String> getPermissionsNotToHave() {
		return permissionsNotToHave;
	}

	public void setPermissionsNotToHave(List<String> permissionNotToHave) {
		this.permissionsNotToHave = permissionNotToHave;
	}
	
}
