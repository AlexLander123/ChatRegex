package me.alexlander123.chatregex;

import java.util.HashMap;
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
	public static HashMap<Location, LocationConfig> config;
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version "  + pdfFile.getVersion() + " by " + pdfFile.getAuthors() + " Has Been Enabled!");
		getServer().getPluginManager().registerEvents(chatListener, this);
		config = new HashMap<Location, LocationConfig>();
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
		Map<String, Object> locations = this.getConfig().getConfigurationSection("locations").getValues(false);
		for(String string : locations.keySet()){
			
			//Check if config is empty
			if(!getConfig().isSet("locations." + string + ".x")){logger.log(Level.WARNING, "[ChatRegex] X Coordinate is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("locations." + string + ".y")){logger.log(Level.WARNING, "[ChatRegex] Y Coordinate is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("locations." + string + ".z")){logger.log(Level.WARNING, "[ChatRegex] Z Coordinate is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("locations." + string + ".world")){logger.log(Level.WARNING, "[ChatRegex] World is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("locations." + string + ".radius")){logger.log(Level.WARNING, "[ChatRegex] Radius is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("locations." + string + ".action")){logger.log(Level.WARNING, "[ChatRegex] action is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("locations." + string + ".regex")){logger.log(Level.WARNING, "[ChatRegex] Regex is missing from the entry: " + string); continue;}
			if(!getConfig().isSet("locations." + string + ".commands")){logger.log(Level.WARNING, "[ChatRegex] Command(s) is missing from the entry: " + string); continue;}
			
		    World world = Bukkit.getWorld(getConfig().getString("locations." + string + ".world"));
			int x = getConfig().getInt("locations." + string + ".x");
			int y = getConfig().getInt("locations." + string + ".y");
			int z = getConfig().getInt("locations." + string + ".z");
			int radius = getConfig().getInt("locations." + string + ".radius");
			int action = getConfig().getInt("locations." + string + ".action");
			Pattern regex = Pattern.compile(getConfig().getString("locations." + string + ".regex"));
			List<String> commands = getConfig().getStringList("locations." + string + ".commands");
			config.put(new Location(world, x, y, z), new LocationConfig(radius, regex, commands, action));
		}
	}

}
