package net.catharos.port;

import java.util.logging.Level;
import net.catharos.port.storage.TravelStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class PortPlugin extends JavaPlugin {
	protected static PortPlugin instance;
	
	protected TravelStorage travels;
	
	
	@Override
	public void onEnable() {
		instance = this;
		
		travels = new TravelStorage(this);
	}
	
	@Override
	public void onDisable() {}
	
	public TravelStorage getTravelStorage() {
		return travels;
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
