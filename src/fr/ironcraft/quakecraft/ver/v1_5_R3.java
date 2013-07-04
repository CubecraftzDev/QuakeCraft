package fr.ironcraft.quakecraft.ver;

import java.lang.reflect.Method;

import net.minecraft.server.v1_5_R3.EntityPlayer;
import net.minecraft.server.v1_5_R3.MinecraftServer;
import net.minecraft.server.v1_5_R3.PlayerInteractManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import fr.ironcraft.quakecraft.*;
import fr.ironcraft.quakecraft.v1_5_R3.EntityFireworksNew;

public class v1_5_R3 implements NMS {

	@Override
	public void load() {
	      try {
	    	  
	          Method a = net.minecraft.server.v1_5_R3.EntityTypes.class
	              .getDeclaredMethod("a", Class.class, String.class,
	                  int.class);
	          a.setAccessible(true);
	   
	          a.invoke(a,EntityFireworksNew.class, "FireworksRocketEntity", 22);
	          System.out.println("ddd");
	      } catch (Exception e) {
	             e.printStackTrace();
	      }
	}

	@Override
	public void spawnFirework(Player p) {
		CraftWorld cWorld = (CraftWorld) p.getWorld();
		 Location loc = p.getEyeLocation();
		World world = Bukkit.getWorld("world"); //The world you want to spawn the player in
		net.minecraft.server.v1_5_R3.World nmsWorld = ((CraftWorld)world).getHandle();
		PlayerInteractManager pim = new PlayerInteractManager(nmsWorld);
		
	
		
		 EntityFireworksNew firework = new EntityFireworksNew(cWorld.getHandle(), loc.getX(), loc.getY(), loc.getZ());
		 cWorld.getHandle().addEntity(firework, SpawnReason.NATURAL);
		
		 firework.setVelocity(p.getLocation().getDirection().multiply(4));
	}



}
