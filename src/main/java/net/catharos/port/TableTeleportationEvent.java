package net.catharos.port;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class TableTeleportationEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private Location loc;
	private boolean cancelled = false;
	
	public TableTeleportationEvent(Location target, Player player) {
		this.loc = target;
		this.player = player;
	}
	
	public Location getTargetLocation() {
		return this.loc;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
	
}
