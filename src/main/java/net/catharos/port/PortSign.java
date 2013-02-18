package net.catharos.port;

import org.bukkit.Location;


public class PortSign {
	
	protected Location target;
	protected String scriptName;
	
	
	public PortSign(Location target) {
		this.target = target;
	}
	
	public Location getTarget() { return target;
	
	}
	public String getScript() { return scriptName; }
	public void setScript( String name ) { this.scriptName = name; }
	
}
