package me.znickq.assemblyfurnace;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AssemblyFurnace extends JavaPlugin implements Listener {

	private WorkingFurnaceListener listener;

	@Override
	public void onDisable() {
		listener.shutdown();
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		listener = new WorkingFurnaceListener(this);
		pm.registerEvents(listener, this);
		pm.registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
			return;
		}
		if (block == null) {
			return;
		}
		if (PlacementUtils.isValid(block)) {
			if (listener.isWorkingFurnace(block)) {
				listener.removeWorkingFurnace(block);
				event.getPlayer().sendMessage(ChatColor.RED + "Disabled working furnace!");
			} else {
				boolean worked = listener.addWorkingFurnace(block, event.getPlayer());
				if (worked) {
					event.getPlayer().sendMessage(ChatColor.GREEN + "Added working furnace");
				}
			}
		}
	}
}
