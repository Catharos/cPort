package net.catharos.port;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import net.catharos.port.listener.PortListener;
import net.catharos.port.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;


public class PortPlugin extends JavaPlugin {
	protected static PortPlugin instance;
	
	// ---- Listeners ----
	protected PortListener portListener;
	
	// ---- Storages -----
	protected Map<Location, PortSign> signs;
	
	
	@Override
	public void onEnable() {
		instance = this;
		
		// Initialize lists
		signs = new HashMap<Location, PortSign>();
		
		// Register listeners
		portListener = new PortListener(this);
		
		// Finish
		log("Enabled!");
	}
	
	@Override
	public void onDisable() {
		// Free RAM
		signs.clear();
	}
	
	public PortListener getPortListener() {
		return portListener;
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
	
	public static void signError(String msg) {
		log(ChatColor.DARK_RED + "Error creating sign: " + ChatColor.GOLD + msg);
	}
	
	public static PortPlugin getInstance() {
		return instance;
	}
	
	public PortSign getOrCreatePortSignAt( Block block ) {
		if(block == null) return null;
		
		PortSign sign = getSignMap().get(block.getLocation());
		
		if(sign == null) {
			if(block.getType() != Material.SIGN && block.getType() != Material.SIGN_POST) {
				signError("No sign found (Block is of type " + block.getType().name() +")!");
				return null;
			}
			
			sign = createSignAt( block.getLocation(), ((Sign) block.getState()).getLines());
		}
		
		return sign;
	}
	
	public PortSign createSignAt( Location loc, String[] lines ) {
		try {
			String worldName = lines[1];
			World world = Bukkit.getServer().getWorld(worldName);
			if(world == null) throw new Exception("No world with name " + worldName + " found!");

			String targetString = lines[2];
			Location target = LocationUtil.getLocationFromString(world, targetString);
			if(target == null ) throw new Exception("Invalid location: " + targetString);

			PortSign sign = new PortSign(target);

			String scriptName = lines[3];
			if(scriptName != null && !scriptName.isEmpty()) sign.setScript(scriptName);

			// Save the sign
			PortPlugin.getInstance().getSignMap().put(loc, sign);
			return sign;
			
		} catch( Exception e ) {
			signError(e.getMessage());
			return null;
		}
	}
}
