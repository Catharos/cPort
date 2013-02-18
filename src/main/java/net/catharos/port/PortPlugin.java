package net.catharos.port;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import net.catharos.port.listener.InteractionListener;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;


public class PortPlugin extends JavaPlugin {
	protected static PortPlugin instance;
	
	// ---- Listeners ----
	protected InteractionListener interactionListener;
	
	// ---- Storages -----
	protected Map<Location, PortSign> signs;
	
	
	@Override
	public void onEnable() {
		instance = this;
		
		// Initialize lists
		signs = new HashMap<Location, PortSign>();
		
		// Register listeners
		interactionListener = new InteractionListener(this);
		
		// Finish
		log("Enabled!");
	}
	
	@Override
	public void onDisable() {
		// Free RAM
		signs.clear();
	}
	
	public InteractionListener getInteractionListener() {
		return interactionListener;
	}
	
	public Map<Location, PortSign> getSignMap() {
		return signs;
	}
	
	public static void log(String msg) {
		log(Level.INFO, msg);
	}
	
	public static void log(Level lvl, String msg) {
		getInstance().getLogger().log(lvl, msg);
	}
	
	public static PortPlugin getInstance() {
		return instance;
	}
}
