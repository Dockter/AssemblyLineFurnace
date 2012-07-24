package com.almramc.assemblylinefurnace;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public class WorkingFurnaceListener implements Listener {

	private AssemblyFurnace plugin;
	private Map<Block, WorkingFurnace> workingFurnaces = new HashMap<Block, WorkingFurnace>();

	public WorkingFurnaceListener(AssemblyFurnace plugin) {
		this.plugin = plugin;
		plugin.getDataFolder().mkdirs();
		Set<WorkingFurnace> loaded = new HashSet<WorkingFurnace>();
		try {
			loaded = (Set<WorkingFurnace>) SLAPI.load(plugin.getDataFolder() + File.separator + "data.dat");
		} catch (Exception ex) {
		}
		for (WorkingFurnace ll : loaded) {
			try {
				Block bl = ll.getBlock();
				if (bl.getType() == Material.FURNACE || bl.getType() == Material.BURNING_FURNACE) {
					workingFurnaces.put(bl, ll);
				}
			} catch (Exception ex) {
				System.out.println("Error loading working furnace: "+ex.getMessage());
			}
		}
	}

	public void shutdown() {
		Set<WorkingFurnace> toSave = new HashSet<WorkingFurnace>();
		for (WorkingFurnace val : workingFurnaces.values()) {
			toSave.add(val);
		}
		try {
			SLAPI.save(toSave, plugin.getDataFolder() + File.separator + "data.dat");
		} catch (Exception ex) {
			Logger.getLogger(WorkingFurnaceListener.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public boolean addWorkingFurnace(Block block, Player who) {
		if (!(block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE)) {
			return false;
		}
		if (workingFurnaces.containsKey(block)) {
			return false;
		}
		workingFurnaces.put(block, new WorkingFurnace(block, who.getName()));
		Furnace furnace = (Furnace) block.getState();
		boolean refFuel = refreshFuel(furnace);
		boolean refInput = refreshInput(furnace);
		boolean delOutput = delayedTakeOutput(furnace);
		return refFuel && refInput && delOutput;
	}

	public void removeWorkingFurnace(Block block) {
		workingFurnaces.remove(block);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (workingFurnaces.containsKey(event.getBlock())) {
			removeWorkingFurnace(event.getBlock());
		}
	}

	@EventHandler
	public void onFurnaceSmelt(FurnaceSmeltEvent event) {
		if (!workingFurnaces.containsKey(event.getBlock())) {
			return;
		}
		final Furnace furnace = (Furnace) event.getBlock().getState();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			public void run() {
				delayedTakeOutput(furnace);
			}
		}, 1L);
	}

	@EventHandler
	public void onFurnaceBurn(FurnaceBurnEvent event) {
		if (!workingFurnaces.containsKey(event.getBlock())) {
			return;
		}
		final Furnace f = ((Furnace) event.getBlock().getState());
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			public void run() {
				refreshFuel(f);
			}
		}, 1L);
	}

	public boolean isWorkingFurnace(Block block) {
		return workingFurnaces.containsKey(block);
	}

	private boolean delayedTakeOutput(final Furnace furnace) {
		Block output = PlacementUtils.getOutputChest(furnace.getBlock(), true);
		if (output == null) {
			return true;
		}
		if (furnace.getInventory().getResult() == null) {
			return true;
		}
		Chest chest = (Chest) output.getState();
		HashMap<Integer, ItemStack> noFit = chest.getBlockInventory().addItem(furnace.getInventory().getResult());
		if (!noFit.isEmpty()) {
			ItemStack remained = noFit.get(0);
			Block soutput = PlacementUtils.getOutputChest(furnace.getBlock(), false);
			if (soutput == null) {
				furnace.getInventory().setResult(remained);
			} else {
				Chest schest = (Chest) soutput.getState();
				noFit = schest.getBlockInventory().addItem(remained);
				if (noFit.get(0) != null) {
					furnace.getInventory().setResult(noFit.get(0));
					String creator = workingFurnaces.get(furnace.getBlock()).getCreator();
					Player plr = Bukkit.getPlayer(creator);
					plr.sendMessage(ChatColor.GOLD + "One of your furnaces at " + furnace.getBlock().getX() + ", " + furnace.getBlock().getY() + ", " + furnace.getBlock().getZ() + " run out of chest space!");
					removeWorkingFurnace(furnace.getBlock());
					return false;
				} else {
					furnace.getInventory().setResult(new ItemStack(0, 0));
				}
			}
		} else {
			furnace.getInventory().setResult(new ItemStack(0, 0));
		}
		return refreshInput(furnace);

	}

	private boolean refreshFuel(Furnace furnace) {
		Block fuel = PlacementUtils.getFuelChest(furnace.getBlock());
		if (fuel == null) {
			return true;
		}
		Chest ichest = (Chest) fuel.getState();
		for (int slot = 0; slot < ichest.getBlockInventory().getSize(); slot++) {
			ItemStack cur = ichest.getBlockInventory().getItem(slot);
			if (cur == null) {
				continue;
			}
			if (!(PlacementUtils.isFuel(cur.getType()))) {
				continue;
			}
			furnace.getInventory().setFuel(cur);
			ichest.getBlockInventory().setItem(slot, null);
			slot = ichest.getBlockInventory().getSize();
		}
		if (furnace.getInventory().getFuel() == null) {
			String creator = workingFurnaces.get(furnace.getBlock()).getCreator();
			Player plr = Bukkit.getPlayer(creator);
			plr.sendMessage(ChatColor.GOLD + "One of your furnaces at " + furnace.getBlock().getX() + ", " + furnace.getBlock().getY() + ", " + furnace.getBlock().getZ() + " run out of fuel!");
			removeWorkingFurnace(furnace.getBlock());
			return false;
		}
		return true;
	}

	private boolean refreshInput(Furnace furnace) {
		Block input = PlacementUtils.getInputChest(furnace.getBlock());
		if (input == null) {
			return true;
		}
		if (furnace.getInventory().getSmelting() != null) {
			return true;
		}
		Chest ichest = (Chest) input.getState();
		for (int slot = 0; slot < ichest.getBlockInventory().getSize(); slot++) {
			ItemStack cur = ichest.getBlockInventory().getItem(slot);
			if (cur == null) {
				continue;
			}
			if (!(PlacementUtils.isBurnable(cur.getType()))) {
				continue;
			}
			furnace.getInventory().setSmelting(cur);
			ichest.getBlockInventory().setItem(slot, null);
			slot = ichest.getBlockInventory().getSize();
		}
		if (furnace.getInventory().getSmelting() == null) {
			String creator = workingFurnaces.get(furnace.getBlock()).getCreator();
			Player plr = Bukkit.getPlayer(creator);
			plr.sendMessage(ChatColor.GOLD + "One of your furnaces at " + furnace.getBlock().getX() + ", " + furnace.getBlock().getY() + ", " + furnace.getBlock().getZ() + " is done smelting!");
			removeWorkingFurnace(furnace.getBlock());
			return false;
		}
		return true;
	}
}
