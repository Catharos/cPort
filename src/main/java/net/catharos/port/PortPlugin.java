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
	
	public static PortPlugin getInstance() {
		return instance;
	}
	
	public PortSign getOrCreatePortSignAt( Location loc ) {
		Block sB = loc.getWorld().getBlockAt(loc);
		if(sB.getType() != Material.SIGN && sB.getType() != Material.SIGN_POST) {
			String msg = "No sign found (Block is of type " + sB.getType().name() +")!";
			PortPlugin.log(ChatColor.DARK_RED + "Error creating sign: " + ChatColor.GOLD + msg);
		}
			
		return getOrCreatePortSignAt(sB, false);
	}
	
	public PortSign getOrCreatePortSignAt( Block block, boolean isSign ) {
		PortSign sign = getSignMap().get(block.getLocation());
		
		if(sign != null) return sign;
		
		try {
			Sign signBlock = (Sign) block.getState();

			if(!isSign && !signBlock.getLine(0).equalsIgnoreCase("[cPort]"))
				throw new Exception("No valid cPort sign found!");

			String worldName = signBlock.getLine(1);
			World world = Bukkit.getServer().getWorld(worldName);
			if(world == null) throw new Exception("No world with name " + worldName + " found!");

			String targetString = signBlock.getLine(2);
			Location target = LocationUtil.getLocationFromString(world, targetString);
			if(target == null ) throw new Exception("Invalid location: " + targetString);

			sign = new PortSign(target);

			String scriptName = signBlock.getLine(3);
			if(scriptName != null && !scriptName.isEmpty()) sign.setScript(scriptName);

			// Save the sign
			PortPlugin.getInstance().getSignMap().put(block.getLocation(), sign);
		} catch( Exception e ) {
			PortPlugin.log(ChatColor.DARK_RED + "Error creating sign: " + ChatColor.GOLD + e.getMessage());
		}
		
		return sign;
	}
}
