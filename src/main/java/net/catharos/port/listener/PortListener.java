package net.catharos.port.listener;

import java.util.Map;
import net.catharos.port.PortPlugin;
import net.catharos.port.PortSign;
import net.catharos.port.TableTeleportationEvent;
import net.catharos.port.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
		if(block.getType() == Material.ENCHANTMENT_TABLE && block.getData() == 1) {
			// Cancel the event
			event.setCancelled(true);

			Location loc = block.getLocation();
			Player player = event.getPlayer();

			// Get sign
			Block signBlock = block.getWorld().getBlockAt(loc.subtract(0, 2, 0));
			PortSign sign = PortPlugin.getInstance().getOrCreatePortSignAt(signBlock);
			if(sign == null) {
				player.sendMessage(ChatColor.DARK_RED + "[Error] " + ChatColor.GOLD + "This table leads to nowhere!");
				return;
			}

			// Off we go!
			Location tp = LocationUtil.findNearestAirSpaceAround(sign.getTarget(), 3).add(0.5, 0, 0.5);
			
			TableTeleportationEvent e = new TableTeleportationEvent(tp, player);
			Bukkit.getServer().getPluginManager().callEvent(e);

			if(!e.isCancelled()) {
				player.teleport(e.getTargetLocation());
				
				player.addPotionEffect( new PotionEffect( PotionEffectType.BLINDNESS, 4 * 20, 0 ), true );
				player.addPotionEffect( new PotionEffect( PotionEffectType.CONFUSION, 4 * 20, 0 ), true );
			}

			// TODO add denizen script activation
		}
	}
	
	@EventHandler
	public void place(SignChangeEvent event) {
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
				PortSign sign = PortPlugin.getInstance().createSignAt(loc, event.getLines());
				if(sign == null) {
					throw new Exception("Error creating sign. Please check console!");
				}
				
				event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Success! Linked to: " + sign.getTarget());
				
			} catch( Exception e ) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "[Error] " + ChatColor.GOLD + e.getMessage());
				
				Block block = event.getBlock();
				if(event.getPlayer().getGameMode() == GameMode.CREATIVE) block.setType(Material.AIR);
				else block.breakNaturally();
					
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void destroy(BlockBreakEvent event) {
		if(event.isCancelled()) return;
		
		Block block = event.getBlock();
		if(block.getType() != Material.SIGN || block.getType() != Material.SIGN_POST) return;
		
		Map<Location, PortSign> signs = PortPlugin.getInstance().getSignMap();

		if(signs.containsKey(block.getLocation())) {
			signs.remove(block.getLocation());
			event.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Successfully removed cPort sign");
		}
	}
}
