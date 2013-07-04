package fr.ironcraft.quakecraft.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EventFireworkCollision extends EntityEvent {

	public EventFireworkCollision(Entity what) {
		super(what);
		// TODO Auto-generated constructor stub
	}
	   private static final HandlerList handlers = new HandlerList();
	   @Override
	    public Firework getEntity() {
	        return (Firework) entity;
	    }

	    @Override
	    public HandlerList getHandlers() {
	        return handlers;
	    }

	    public static HandlerList getHandlerList() {
	        return handlers;
	    }
	

}
