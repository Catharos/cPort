package net.catharos.port.storage;

import org.bukkit.Location;


public class TravelSection {
	protected String name;
	protected Location start, destination;
	
	public TravelSection(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Location getStart() {
		return start;
	}
	
	public Location getDestination() {
		return destination;
	}
}
