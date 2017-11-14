package me.alexlander123.chatregex;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
	
	class ExecuteCommand implements Runnable {
		
		private String command;
		
		public ExecuteCommand(String command) {
			this.command = command;
		}

		@Override
		public void run() {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
			
		}
		
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		Pattern captureGroupRegex = Pattern.compile("%cg([0-9]*)");
		for(RegexConfig globalRegex : ChatRegex.globalConfig){
			Matcher matcher = globalRegex.getRegex().matcher(event.getMessage());
			if(matcher.find()){
				for(CommandEntry commandEntry : globalRegex.getCommands()){
					
					String command = commandEntry.getCommand();
					
					command = command.replaceAll("%player", event.getPlayer().getName());
					command = command.replaceAll("%message", event.getMessage());
					
					Matcher cgMatcher = captureGroupRegex.matcher(command);
					while(cgMatcher.find()){
						int cgGroup = Integer.parseInt(cgMatcher.group(1));
						command = command.replaceAll("%cg" + cgGroup, matcher.group(cgGroup));
					}
					
					if(commandEntry.getDelay() == 0){
						Bukkit.getScheduler().runTask(ChatRegex.getInstance(), new ExecuteCommand(command));
					}
					else{
						Bukkit.getScheduler().runTaskLater(ChatRegex.getInstance(), new ExecuteCommand(command), commandEntry.getDelay() * 20);
					}
					
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
						for(CommandEntry commandEntry : localRegex.getCommands()){
							
							String command = commandEntry.getCommand();
							
							command = command.replaceAll("%player", event.getPlayer().getName());
							command = command.replaceAll("%message", event.getMessage());
							
							Matcher cgMatcher = captureGroupRegex.matcher(command);
							while(cgMatcher.find()){
								int cgGroup = Integer.parseInt(cgMatcher.group(1));
								command = command.replaceAll("%cg" + cgGroup, matcher.group(cgGroup));
							}
							
							if(commandEntry.getDelay() == 0){
								Bukkit.getScheduler().runTask(ChatRegex.getInstance(), new ExecuteCommand(command));
							}
							else{
								Bukkit.getScheduler().runTaskLater(ChatRegex.getInstance(), new ExecuteCommand(command), commandEntry.getDelay() * 20);
							}
							
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
