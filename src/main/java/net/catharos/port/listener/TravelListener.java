package net.catharos.port.listener;

import net.catharos.port.PortPlugin;
import net.catharos.port.storage.TravelSection;
import net.catharos.port.storage.TravelStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class TravelListener implements Listener {
	public TravelListener(JavaPlugin plugin) {
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		if(event.isCancelled() || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		Block block = event.getClickedBlock();
		
		if(block.getType() == Material.ENCHANTMENT_TABLE) {
			TravelStorage travels = PortPlugin.getInstance().getTravelStorage();
			TravelSection section = travels.get(block.getLocation());
			
			// TODO: Make it work!
		}
	}
}
