package com.almramc.assemblylinefurnace;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
		if (event.getPlayer().hasPermission("assemblylinefurnace.use")) {
			if (PlacementUtils.isValid(block)) {
				if (listener.isWorkingFurnace(block)) {
					listener.removeWorkingFurnace(block);
					event.getPlayer().sendMessage(ChatColor.RED + "Disabled Assembly Line Furnace!");
				} else {
					boolean worked = listener.addWorkingFurnace(block, event.getPlayer());
					if (worked) {
						event.getPlayer().sendMessage(ChatColor.GREEN + "Activated Assembly Line Furnace!");
					}
				}
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		if (args[0].equalsIgnoreCase("keeploaded")) {
				listener.addLoaded((Player) sender);			
				return true;
		}
		return false;
	}
}
