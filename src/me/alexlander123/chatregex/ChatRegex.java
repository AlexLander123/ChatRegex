package me.alexlander123.chatregex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatRegex extends JavaPlugin{
	
	public Logger logger = Logger.getLogger("Minecraft");
	public static Listener chatListener = new ChatListener();
	public static ArrayList<LocalRegexConfig> config;
	public static ArrayList<RegexConfig> globalConfig;
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version "  + pdfFile.getVersion() + " by " + pdfFile.getAuthors() + " Has Been Enabled!");
		getServer().getPluginManager().registerEvents(chatListener, this);
		config = new ArrayList<LocalRegexConfig>();
		globalConfig = new ArrayList<RegexConfig>();
		getConfig().options().copyDefaults(true);
		saveConfig();
		loadConfig();
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled!");
	}
	
	public void loadConfig() {
		Map<String, Object> locations = this.getConfig().getConfigurationSection("regexs").getValues(false);
		for(String string : locations.keySet()){

			//Check if config is empty
			if(!getConfig().isSet("regexs." + string + ".x")){logger.log(Level.WARNING, "[ChatRegex] X Coordinate is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("regexs." + string + ".y")){logger.log(Level.WARNING, "[ChatRegex] Y Coordinate is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("regexs." + string + ".z")){logger.log(Level.WARNING, "[ChatRegex] Z Coordinate is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("regexs." + string + ".world")){logger.log(Level.WARNING, "[ChatRegex] World is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("regexs." + string + ".radius")){logger.log(Level.WARNING, "[ChatRegex] Radius is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("regexs." + string + ".action")){logger.log(Level.WARNING, "[ChatRegex] The option action is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("regexs." + string + ".global")){logger.log(Level.WARNING, "[ChatRegex] The option global is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("regexs." + string + ".regex")){logger.log(Level.WARNING, "[ChatRegex] Regex is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("regexs." + string + ".commands")){logger.log(Level.WARNING, "[ChatRegex] Command(s) is missing from the entry: " + string); continue;}

			if(getConfig().getString("regexs." + string + ".world") == null){logger.log(Level.WARNING, "[ChatRegex] World is invalid from the entry: " + string); continue;}

			int action = getConfig().getInt("regexs." + string + ".action");
			Pattern regex = Pattern.compile(getConfig().getString("regexs." + string + ".regex"));
			List<String> commands = getConfig().getStringList("regexs." + string + ".commands");
			if(getConfig().getBoolean("regexs." + string + ".global") == false){
				World world = Bukkit.getWorld(getConfig().getString("regexs." + string + ".world"));
				int x = getConfig().getInt("regexs." + string + ".x");
				int y = getConfig().getInt("regexs." + string + ".y");
				int z = getConfig().getInt("regexs." + string + ".z");
				int radius = getConfig().getInt("regexs." + string + ".radius");
				config.add(new LocalRegexConfig(new Location(world, x, y, z), radius, regex, commands, action));
			} 
			else{
				globalConfig.add(new RegexConfig(regex, commands, action));
			}
		}
	}

}
