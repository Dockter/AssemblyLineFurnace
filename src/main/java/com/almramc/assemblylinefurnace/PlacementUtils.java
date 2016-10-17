package com.almramc.assemblylinefurnace;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.material.FurnaceAndDispenser;

public class PlacementUtils {

	private static List<BlockFace> directions = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
	private static List<Material> burnables = Arrays.asList(Material.CLAY, Material.IRON_ORE, Material.GOLD_ORE, Material.SAND, Material.COBBLESTONE, Material.STONE, Material.EGG, Material.PORK, Material.CLAY_BALL, Material.getMaterial(17), Material.CACTUS, Material.DIAMOND_ORE, Material.RAW_FISH, Material.RAW_BEEF, Material.RAW_CHICKEN, Material.POTATO_ITEM, Material.QUARTZ_ORE, Material.FLINT, Material.ROTTEN_FLESH, Material.getMaterial("ALMURA_INGREDIENTSROASTBEEF_RAW"), Material.getMaterial("ALMURA_INGREDIENTSLAMBCHOP_RAW"), Material.getMaterial("ALMURA_INGREDIENTSCHICKENLEG_RAW"), Material.getMaterial("MILK_BUCKET"), Material.DIAMOND_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.COAL_ORE, Material.getMaterial("ALMURA_ORESMARBLEORE"), Material.getMaterial("ALMURA_INGREDIENTSPORKBELLY_RAW"), Material.getMaterial("MOCREATURES_TURKEYRAW"), Material.getMaterial("MOCREATURES_OSTRICHRAW"), Material.getMaterial("MOCREATURES_RATRAW"), Material.getMaterial("IC2_ITEMDUST"), Material.getMaterial("IC2_ITEMPURIFIEDCRUSHEDORE")); 
	private static List<Material> fuels = Arrays.asList(Material.COAL, Material.COAL_BLOCK, Material.WOOD, Material.SAPLING, Material.STICK, Material.FENCE, Material.WOOD_STAIRS, Material.TRAP_DOOR, Material.getMaterial(58), Material.BOOKSHELF, Material.CHEST, Material.JUKEBOX, Material.NOTE_BLOCK, Material.LOCKED_CHEST, Material.BLAZE_ROD, Material.LAVA_BUCKET);

	public static boolean isValid(Block furnace) {
		/*
		 System.out.println("Checking if valid!");
		 System.out.println(getInputChest(furnace));
		 System.out.println(getOutputChest(furnace, false));
		 System.out.println(getFuelChest(furnace));
		 */
		return furnace != null && (furnace.getType() == Material.FURNACE || furnace.getType() == Material.BURNING_FURNACE) && getInputChest(furnace) != null && getFuelChest(furnace) != null && getOutputChest(furnace) != null;
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
		if (adjacentchest.getType() != Material.CHEST) {
		    if (adjacentchest.getType() != Material.TRAPPED_CHEST) {		
		        return null;
		    }
		}
		return adjacentchest;
	}

	public static Block getOutputChest(Block furnace) {
		Furnace state = (Furnace) furnace.getState();
		BlockFace facing = ((FurnaceAndDispenser) state.getData()).getFacing();
		BlockFace dir = directions.get((directions.indexOf(facing) + 3) % directions.size());
		Block adjacentchest = furnace.getRelative(dir);
		if (adjacentchest.getType() == Material.CHEST || adjacentchest.getType() == Material.TRAPPED_CHEST) {
			return adjacentchest; //return that chest;
		}
		return null; // No output chest
	}

	public static Block getFuelChest(Block furnace) {
		Furnace state = (Furnace) furnace.getState();
		BlockFace facing = ((FurnaceAndDispenser) state.getData()).getFacing();
		BlockFace dir = directions.get((directions.indexOf(facing) + 2) % directions.size());
		Block adjacentchest = furnace.getRelative(dir);

		if (adjacentchest.getType() != Material.CHEST) {
		    if(adjacentchest.getType() != Material.TRAPPED_CHEST) {
		        return null;
		    }
		}
		return adjacentchest;
	}
}
