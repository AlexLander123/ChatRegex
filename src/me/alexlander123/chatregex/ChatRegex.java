package me.alexlander123.chatregex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public static HashMap<Location, LocationConfig> config = new HashMap<Location, LocationConfig>();
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version "  + pdfFile.getVersion() + " by " + pdfFile.getAuthors() + " Has Been Enabled!");
		getServer().getPluginManager().registerEvents(chatListener, this);
		this.saveDefaultConfig();
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
			World world = Bukkit.getWorld(getConfig().getString("locations." + string + ".world"));
			int x = getConfig().getInt("locations." + string + ".x");
			int y = getConfig().getInt("locations." + string + ".y");
			int z = getConfig().getInt("locations." + string + ".z");
			int radius = getConfig().getInt("locations." + string + ".radius");
			Pattern regex = Pattern.compile(getConfig().getString("locations." + string + ".regex"));
			List<String> commands = getConfig().getStringList("locations." + string + ".commands");
			config.put(new Location(world, x, y, z), new LocationConfig(radius, regex, commands));
		}
	}

}
