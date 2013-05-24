package fr.ironcraft.quakecraft.utils;

import java.util.HashMap;
import java.util.Map.Entry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SimpleInventorySaver {
	private final HashMap<Integer, ItemStack> contents = new HashMap<Integer, ItemStack>();
	private ItemStack helmet = null;
	private ItemStack chestplate = null;
	private ItemStack leggings = null;
	private ItemStack boots = null;

	private final void clear() {
		contents.clear();
		helmet = null;
		chestplate = null;
		leggings = null;
		boots = null;
	}

	public final void restore(final Player player) {
		player.getInventory().clear();
		player.setItemInHand(null);
		player.getInventory().setBoots(boots);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setHelmet(helmet);
		for (final Entry<Integer, ItemStack> entry : contents.entrySet())
			player.getInventory().setItem(entry.getKey(), entry.getValue());
		clear();
	}

	public final void save(final Player player) {
		clear();
		helmet = player.getInventory().getHelmet();
		chestplate = player.getInventory().getChestplate();
		leggings = player.getInventory().getLeggings();
		boots = player.getInventory().getBoots();
		for (int i = 0; i < player.getInventory().getContents().length; i++) {
			final ItemStack itemStack = player.getInventory().getContents()[i];
			if (itemStack != null)
				contents.put(i, itemStack.clone());
		}
	}
}