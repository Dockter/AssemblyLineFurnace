package src.Dockter.AssemblyLineFurnace;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.Configuration;
import org.getspout.spoutapi.player.SpoutPlayer;

public class DocBoxFurnace extends JavaPlugin {
	public Configuration config1;
	private final DBFInventoryListener inventoryListener = new DBFInventoryListener(this);
	private final DBFPlayerListener playerListener = new DBFPlayerListener(this);
	static HashMap<Location, Player> usedplayers = new HashMap<Location, Player>();
	static HashMap<String, String> ResultNames = new HashMap<String, String>();
	static HashMap<String, Object> config = new HashMap<String, Object>();
	static HashMap<Location, Boolean> isenabled = new HashMap<Location, Boolean>();
	Logger log = Logger.getLogger("Minecraft");

	@Override
	public void onEnable() {
		log.info("DocBoxFurnace version " + this.getDescription().getVersion() + " initializing...");
		loadConfig();
		log.info("DocBoxFurnace version " + this.getDescription().getVersion() + " has been enabled!");
		PluginManager pm = getServer().getPluginManager();
				
		pm.registerEvents(inventoryListener, this);
		pm.registerEvents(playerListener, this);
		
		loadResultNames();
	}

	@Override
	public void onDisable() {
		log.info("DocBoxFurnace has been disabled.");
	}

	private void loadConfig() {
		config1 = getConfig();
		config1.options().copyDefaults(true);
		config.put("enable-item-teleport", config1.getBoolean("enable-item-teleport", false));
		if (config1.getBoolean("announcements.overall-disable", false)) {
			config.put("disable-message", true);
			config.put("disable-full", false);
			config.put("output-full", true); //Displays Output Chest Full Message
			config.put("activated", true); //Displays Furnace Activated Message
			config.put("debug", false);
		} else {
			config.put("disable-message", config1.getBoolean("announcements.disable-message", false));
			config.put("disable-full", config1.getBoolean("announcements.disable-full", false));
			config.put("output-full", config1.getBoolean("announcements.output-full", true)); //Displays Output Chest Full Message
			config.put("activated", config1.getBoolean("announcements.activated", true)); //Displays Furnace Activated Message
			config.put("debug", config1.getBoolean("announcements.debug", true));
		}
		config.put("leftclick", !config1.getBoolean("disable-leftclick", false));
		//config.put("auto-update", config1.getBoolean("auto-update", false));
		this.saveConfig();
	}

	private void loadResultNames() {
		ResultNames.put("IRON_INGOT", "iron ingot");
		ResultNames.put("GOLD_INGOT", "gold ingot");
		ResultNames.put("GLASS", "glass");
		ResultNames.put("STONE", "smooth stone");
		ResultNames.put("GRILLED_PORK", "cooked porkchop");
		ResultNames.put("CLAY_BRICK", "clay brick");
		ResultNames.put("COOKED_FISH", "cooked fish");
		ResultNames.put("COAL", "charcoal");
		ResultNames.put("INK_SACK", "cactus green");
		ResultNames.put("COOKED_CHICKEN", "cooked chicken");
		ResultNames.put("COOKED_BEEF", "cooked beef");
	}
}
