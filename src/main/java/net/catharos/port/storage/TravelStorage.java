package net.catharos.port.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.catharos.port.PortPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TravelStorage implements FileStorage {
	protected File file;
	protected FileConfiguration config;
	
	protected List<TravelSection> travels;
	
	
	public TravelStorage(JavaPlugin plugin) {
		file = new File(plugin.getDataFolder(), "travel.yml");
		
		this.reload();
	}
	
	public void save() {
		try {
			get().save(file);
		} catch (IOException ex) {
			PortPlugin.log(Level.SEVERE, "Could not save the travel storage: " + ex.getMessage());
		}
	}

	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
		
		travels = new ArrayList<TravelSection>();
		
		for(String key : get().getKeys(false)) {
			ConfigurationSection section = get().getConfigurationSection(key);
			
			TravelSection ts = new TravelSection(key);
			
			add(ts);
		}
	}

	public FileConfiguration get() {
		return config;
	}
	
	public void add(TravelSection section) {
		this.travels.add(section);
	}
	
	public TravelSection get( Location loc ) {
		if(get() != null) {
			for(TravelSection t : travels) {
				Location l = t.getStart();
				
				if(l.getX() == loc.getX() && l.getY() == loc.getY() && l.getZ() == loc.getZ())
					return t;
			}
		}
		
		return null;
	}
	
}
