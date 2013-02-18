package net.catharos.port.listener;

import net.catharos.port.PortPlugin;
import net.catharos.port.PortSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class InteractionListener implements Listener {
	public InteractionListener(JavaPlugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		
		Block block = event.getClickedBlock();
		if(block.getType() != Material.ENCHANTMENT_TABLE && block.getData() != 1) return;
		
		Location loc = block.getLocation();
		
		PortSign sign = PortPlugin.getInstance().getSignMap().get(loc);
		
		// Get sign
		if(sign == null) {
			Block sB = block.getWorld().getBlockAt(loc.add(0, -2, 0));
			if(!(sB instanceof Sign)) return;
			
			Sign signBlock = (Sign) sB;
			
			if(!signBlock.getLine(0).equalsIgnoreCase("[cPort]")) return;
			
			String worldName = signBlock.getLine(1);
			World world = Bukkit.getServer().getWorld(worldName);
			if(world == null) return;
			
			Location target = getLocationFromString(world, signBlock.getLine(2));
			if(target == null) return;
			
			sign = new PortSign(target);
			
			String scriptName = signBlock.getLine(3);
			if(scriptName != null && !scriptName.isEmpty()) sign.setScript(scriptName);
			
			// Save the sign
			PortPlugin.getInstance().getSignMap().put(loc, sign);
		}
		
		// Off we go!
		event.getPlayer().teleport(sign.getTarget());
		// TODO add denizen script activation
	}
	
	private Location getLocationFromString( World world, String location ) {
		String[] locs = location.split(",");
		if(locs.length != 3) return null;

		int x = Integer.parseInt(locs[0].trim());
		int y = Integer.parseInt(locs[1].trim());
		int z = Integer.parseInt(locs[2].trim());
		
		return new Location(world, x, y, z);
	}
}
