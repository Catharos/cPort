package net.catharos.port.storage;

import org.bukkit.configuration.file.FileConfiguration;


public interface FileStorage {
	public void save();
	
	public void reload();
	
	public FileConfiguration get();
}
