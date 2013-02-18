package net.catharos.port.util;

import org.bukkit.Location;
import org.bukkit.World;


public class LocationUtil {
	public static  Location getLocationFromString( World world, String location ) {
		String[] locs = location.split(",");
		
		if(locs.length != 3) return null;

		int x = Integer.parseInt(locs[0].trim());
		int y = Integer.parseInt(locs[1].trim());
		int z = Integer.parseInt(locs[2].trim());
		
		return new Location(world, x, y, z);
	}
}
