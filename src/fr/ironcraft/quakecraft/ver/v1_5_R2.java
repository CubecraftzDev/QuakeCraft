package fr.ironcraft.quakecraft.ver;

import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import fr.ironcraft.quakecraft.*;
import fr.ironcraft.quakecraft.v1_5_R2.EntityFireworksNew;



public class v1_5_R2 implements NMS {

	@Override
	public void load() {
	      try {
	    	  
	          Method a = net.minecraft.server.v1_5_R2.EntityTypes.class
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
		// TODO Auto-generated method stub
		
	}


}
