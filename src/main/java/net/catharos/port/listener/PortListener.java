package net.catharos.port.listener;

import net.catharos.port.PortPlugin;
import net.catharos.port.PortSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PortListener implements Listener {
	public PortListener(JavaPlugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		
		Block block = event.getClickedBlock();
		if(block.getType() != Material.ENCHANTMENT_TABLE && block.getData() != 1) return;
		
		// Cancel the event
		event.setCancelled(true);
		
		Location loc = block.getLocation();
		Player player = event.getPlayer();
		
		// Get sign
		PortSign sign = PortPlugin.getInstance().getOrCreatePortSignAt(loc.subtract(0, 2, 0));
		if(sign == null) {
			player.sendMessage(ChatColor.DARK_RED + "[Error] " + ChatColor.GOLD + "This table leads to nowhere!");
			return;
		}
		
		// Off we go!
		player.teleport(sign.getTarget());
		player.addPotionEffect( new PotionEffect( PotionEffectType.BLINDNESS, 4 * 20, 0 ), true );
		player.addPotionEffect( new PotionEffect( PotionEffectType.CONFUSION, 4 * 20, 0 ), true );
		
		// TODO add denizen script activation
	}
	
	@EventHandler
	public void sign(SignChangeEvent event) {
		if(event.isCancelled()) return;
		
		if(event.getLine(0).equalsIgnoreCase("[cPort]")) {
			Location loc = event.getBlock().getLocation();
			
			try {
				// Check for permisisons
				if(!event.getPlayer().hasPermission("cport.create")) {
					throw new Exception("You don't have permission to create such a sign!");
				}
				
				// Check for table
				Block table = loc.getWorld().getBlockAt(loc.add(0, 2, 0));
				if(table.getType() != Material.ENCHANTMENT_TABLE) {
					throw new Exception("Missing enchantment table (Place it 2 blocks above)!");
				}
				
				// Create the sign
				PortSign sign = PortPlugin.getInstance().getOrCreatePortSignAt(event.getBlock(), true);
				if(sign == null) {
					throw new Exception("Error creating sign. Please check console!");
				}
				
				event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Success! Linked to: " + sign.getTarget());
				
			} catch( Exception e ) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "[Error] " + ChatColor.GOLD + e.getMessage());
				event.getBlock().breakNaturally();
				event.setCancelled(true);
			}
		}
	}
}
