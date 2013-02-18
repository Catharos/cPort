package net.catharos.port.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;


public class LocationUtil {
	public static Location getLocationFromString( World world, String location ) {
		String[] locs = location.split(",");
		
		if(locs.length != 3) return null;

		int x = Integer.parseInt(locs[0].trim());
		int y = Integer.parseInt(locs[1].trim());
		int z = Integer.parseInt(locs[2].trim());
		
		return new Location(world, x, y, z);
	}
	
	public static Location findNearestAirSpaceAround( Location src, int radius ) {
		int y = (int) src.getY();
		
		for(int r = 0; r < radius; r++) {
			for(int x = (int) src.getX()-r; x < src.getX()+r; x++) {
				for(int z = (int) src.getZ()-r; z < src.getZ()+r; z++) {
					Block a = src.getWorld().getBlockAt(x, y, z);
					
					if(a.getType() == Material.AIR) {
						Block b = src.getWorld().getBlockAt(x, y + 1, z);
						
						if(b.getType() == Material.AIR) return a.getLocation();
					}
				}
			}
		}
		
		return null;
	}
}
