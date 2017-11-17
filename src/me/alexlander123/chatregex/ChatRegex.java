package me.alexlander123.chatregex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatRegex extends JavaPlugin{
	
	public Logger logger = Logger.getLogger("Minecraft");
	public static Listener chatListener = new ChatListener();
	public static ArrayList<LocalRegexConfig> config;
	public static ArrayList<RegexConfig> globalConfig;
	private static ChatRegex instance;
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version "  + pdfFile.getVersion() + " by " + pdfFile.getAuthors() + " Has Been Enabled!");
		instance = this;
		getServer().getPluginManager().registerEvents(chatListener, this);
		config = new ArrayList<LocalRegexConfig>();
		globalConfig = new ArrayList<RegexConfig>();
		saveDefaultConfig();
		loadConfig();
	}
	
	@Override
	public void onDisable() {
		config = null;
		globalConfig = null;
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled!");
	}
	
	public void loadConfig() {
		Map<String, Object> locations = this.getConfig().getConfigurationSection("regexs").getValues(false);
		for(String string : locations.keySet()){
			
			int action = 0;
			int cooldown = 0;
			boolean isGlobal = true;
			boolean isGlobalCooldown = true;

			//Check if config is empty and setting values
			
			if(!getConfig().isSet("regexs." + string + ".action")){
				logger.log(Level.WARNING, "[ChatRegex] The option action is missing from the entry: " + string + ". Using default value of 0"); 
			} 
			else {
				action = getConfig().getInt("regexs." + string + ".action");
			}
			
			if(!getConfig().isSet("regexs." + string + ".global")){
				logger.log(Level.WARNING, "[ChatRegex] The option global is missing from the entry: " + string + ". Using default value of true"); 
			}
			else {
				isGlobal = getConfig().getBoolean("regexs." + string + ".global");
			}
			
			if(!getConfig().isSet("regexs." + string + ".cooldown")){
				logger.log(Level.WARNING, "[ChatRegex] The option cooldown is missing from the entry: " + string + ". Using default value of 0");
			}
			else {
				cooldown = getConfig().getInt("regexs." + string + ".cooldown");
			}
			
			if(!getConfig().isSet("regexs." + string + ".global cooldown")){
				logger.log(Level.WARNING, "[ChatRegex] The option global cooldown is missing from the entry: " + string + ". Using default value of true");
			}
			else {
				isGlobalCooldown = getConfig().getBoolean("regexs." + string + ".global cooldown");
			}
			
			if(!getConfig().isSet("regexs." + string + ".regex")){logger.log(Level.WARNING, "[ChatRegex] Regex is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("regexs." + string + ".commands")){logger.log(Level.WARNING, "[ChatRegex] Command(s) is missing from the entry: " + string); continue;}
			
			Pattern regex = Pattern.compile(getConfig().getString("regexs." + string + ".regex"));
			
			//Parse commands
			List<CommandEntry> commands = new ArrayList<CommandEntry>();
			Pattern commandEntryPattern = Pattern.compile("^\\s*c=\"(.+)\"(\\s+d=(\\d+))?\\s*$");
			
			for(String commandConfigEntry : getConfig().getStringList("regexs." + string + ".commands")){
				Matcher commandEntryMatcher = commandEntryPattern.matcher(commandConfigEntry);
				if(commandEntryMatcher.matches()) {
					commands.add(new CommandEntry(commandEntryMatcher.group(1), (commandEntryMatcher.group(3) == null) ? 0 : Integer.parseInt(commandEntryMatcher.group(3))));
				}
				else{
					logger.log(Level.WARNING, "[ChatRegex] The command " + commandConfigEntry + " from the entry: " + string + " is using a invalid format. Ignoring the command.");
					continue;
				}
			}
						
			if(isGlobal == false){
				
				//Check if config is empty
				if(!getConfig().isSet("regexs." + string + ".x")){logger.log(Level.WARNING, "[ChatRegex] X Coordinate is missing from the entry: " + string); continue;}
				if(!getConfig().isSet("regexs." + string + ".y")){logger.log(Level.WARNING, "[ChatRegex] Y Coordinate is missing from the entry: " + string); continue;}
				if(!getConfig().isSet("regexs." + string + ".z")){logger.log(Level.WARNING, "[ChatRegex] Z Coordinate is missing from the entry: " + string); continue;}
				if(!getConfig().isSet("regexs." + string + ".world")){logger.log(Level.WARNING, "[ChatRegex] World is missing from the entry: " + string); continue;}
				if(!getConfig().isSet("regexs." + string + ".radius")){logger.log(Level.WARNING, "[ChatRegex] Radius is missing from the entry: " + string); continue;}
				
				if(Bukkit.getWorld(getConfig().getString("regexs." + string + ".world")) == null){logger.log(Level.WARNING, "[ChatRegex] World is invalid from the entry: " + string); continue;}
				
				World world = Bukkit.getWorld(getConfig().getString("regexs." + string + ".world"));
				int x = getConfig().getInt("regexs." + string + ".x");
				int y = getConfig().getInt("regexs." + string + ".y");
				int z = getConfig().getInt("regexs." + string + ".z");
				int radius = getConfig().getInt("regexs." + string + ".radius");
				config.add(new LocalRegexConfig(new Location(world, x, y, z), radius, regex, commands, action, cooldown, isGlobalCooldown));
			} 
			else{
				globalConfig.add(new RegexConfig(regex, commands, action, cooldown, isGlobalCooldown));
			}
		}
	}

	public static ChatRegex getInstance() {
		return instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(label.equalsIgnoreCase("chatregexreload") || label.equalsIgnoreCase("crreload")){
			if(sender.hasPermission("chatregex.reload")){
				reloadConfig();
				saveConfig();
				config.clear();
				globalConfig.clear();
				loadConfig();
				sender.sendMessage(ChatColor.GREEN + "[ChatRegex] Configuration Reloaded!");
				return true;
			}
			return false;
		}
		return false;
	}
}
