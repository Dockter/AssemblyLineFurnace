package me.znickq.assemblyfurnace;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.FurnaceAndDispenser;

public class PlacementUtils {

	private static List<BlockFace> directions = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
	private static List<Material> burnables = Arrays.asList(Material.IRON_ORE, Material.GOLD_ORE, Material.SAND, Material.COBBLESTONE, Material.PORK, Material.CLAY_BALL, Material.getMaterial(17), Material.CACTUS, Material.DIAMOND_ORE, Material.RAW_FISH, Material.RAW_BEEF, Material.RAW_CHICKEN); 
	private static List<Material> fuels = Arrays.asList(Material.COAL, Material.WOOD, Material.SAPLING, Material.STICK, Material.FENCE, Material.WOOD_STAIRS, Material.TRAP_DOOR, Material.WOOD, Material.getMaterial(58), Material.BOOKSHELF, Material.CHEST, Material.JUKEBOX, Material.NOTE_BLOCK, Material.LOCKED_CHEST, Material.BLAZE_ROD);

	public static boolean isValid(Block furnace) {
		/*
		System.out.println("Checking if valid!");
		System.out.println(getInputChest(furnace));
		System.out.println(getOutputChest(furnace, false));
		System.out.println(getFuelChest(furnace));
		*/ 
		return furnace!= null && (furnace.getType() == Material.FURNACE || furnace.getType() == Material.BURNING_FURNACE) && getInputChest(furnace) != null && getFuelChest(furnace) != null && getOutputChest(furnace, false) != null;
	}
	
	public static boolean isBurnable(Material burnable) {
		return burnables.contains(burnable);
	}
	
	public static boolean isFuel(Material fuel) {
		return fuels.contains(fuel);
	}
	
	public static Block getInputChest(Block furnace) {
		Furnace state = (Furnace) furnace.getState();
		BlockFace facing = ((FurnaceAndDispenser) state.getData()).getFacing();
		BlockFace dir = directions.get((directions.indexOf(facing) + 1) % directions.size());
		Block adjacentchest = furnace.getRelative(dir);
		Block doublechest = adjacentchest.getRelative(dir);

		if (adjacentchest.getType() != Material.CHEST) {
			return null;
		}

		if (doublechest.getType() != Material.CHEST || doublechest.getType() == Material.AIR) {
			return adjacentchest;
		}

		Chest state1 = (Chest) doublechest.getState();
		ItemStack[] items = state1.getInventory().getContents();
		for (ItemStack item : items) {
			if (item != null && burnables.contains(item.getType())) {
				return doublechest;
			}
		}
		if (adjacentchest.getType() == Material.CHEST) {
			return adjacentchest;
		}

		return null;
	}

	public static Block getOutputChest(Block furnace, boolean canBeDouble) {
		Furnace state = (Furnace) furnace.getState();
		BlockFace facing = ((FurnaceAndDispenser) state.getData()).getFacing();
		BlockFace dir = directions.get((directions.indexOf(facing) + 3) % directions.size());
		Block adjacentchest = furnace.getRelative(dir);
		Block doublechest = adjacentchest.getRelative(dir);
		if (adjacentchest.getType() == Material.CHEST && doublechest.getType() == Material.CHEST && canBeDouble) {
			return doublechest;
		}

		if (adjacentchest.getType() == Material.CHEST) {
			return adjacentchest; //return that chest;
		}

		return null; // No output chest
	}

	public static Block getFuelChest(Block furnace) {
		Furnace state = (Furnace) furnace.getState();
		BlockFace facing = ((FurnaceAndDispenser) state.getData()).getFacing();
		BlockFace dir = directions.get((directions.indexOf(facing) + 2) % directions.size());
		Block adjacentchest = furnace.getRelative(dir);
		Block doublechest = adjacentchest.getRelative(dir);

		if (adjacentchest.getType() != Material.CHEST) {
			return null;
		}

		if (doublechest.getType() != Material.CHEST || doublechest.getType() == Material.AIR) {
			return adjacentchest;
		}

		Chest state1 = (Chest) doublechest.getState();
		ItemStack[] items = state1.getInventory().getContents();
		for (ItemStack item : items) {
			if (item != null && fuels.contains(item.getType())) {
				return doublechest;
			}
		}
		if (adjacentchest.getType() == Material.CHEST) {
			return adjacentchest;
		}

		return null;
	}
}
