package me.alexlander123.chatregex;

import java.util.Set;
import java.util.regex.Matcher;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		for(RegexConfig globalRegex : ChatRegex.globalConfig){
			Matcher matcher = globalRegex.getRegex().matcher(event.getMessage());
			if(matcher.find()){
				for(String command : globalRegex.getCommands()){
					command = command.replaceAll("%player", event.getPlayer().getName());
					command = command.replaceAll("%message", event.getMessage());
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
				}
				if(globalRegex.getAction() == 1){
					event.setCancelled(true);
				}
				else if(globalRegex.getAction() == 2){
					Set<Player> recipients = event.getRecipients();
					recipients.clear();
					recipients.add(event.getPlayer());
				}
			}
		}
		for(LocalRegexConfig localRegex : ChatRegex.config){
			if(event.getPlayer().getWorld() == localRegex.getLocation().getWorld()){
				if(event.getPlayer().getLocation().distance(localRegex.getLocation()) <= localRegex.getRadius()){
					Matcher matcher = localRegex.getRegex().matcher(event.getMessage());
					if(matcher.find()){
						for(String command : localRegex.getCommands()){
							command = command.replaceAll("%player", event.getPlayer().getName());
							command = command.replaceAll("%message", event.getMessage());
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
						}
						if(localRegex.getAction() == 1){
							event.setCancelled(true);
						}
						else if(localRegex.getAction() == 2){
							Set<Player> recipients = event.getRecipients();
							recipients.clear();
							recipients.add(event.getPlayer());
						}
					}
				}
			}
		}
	}
}
