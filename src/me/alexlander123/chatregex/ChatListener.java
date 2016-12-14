package me.alexlander123.chatregex;

import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		for(Entry<Location, LocationConfig> entry : ChatRegex.config.entrySet()){
			LocationConfig config = entry.getValue();
			if(event.getPlayer().getLocation().distance(entry.getKey()) <= config.getRadius()){
				Matcher matcher = config.getRegex().matcher(event.getMessage());
				if(matcher.find()){
					for(String command : config.getCommands()){
						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command.replaceAll("%player", event.getPlayer().getName()));
					}
				}
			}
		}
	}
}
