package me.znickq.assemblyfurnace;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;



public class WorkingFurnace implements Serializable{
	private int x,y,z;
	private String creator, world;
	
	protected WorkingFurnace() {}
	
	public WorkingFurnace(Block furnace, String creator) {
		this.creator = creator;
		world = furnace.getWorld().getName();
		x = furnace.getX();
		y = furnace.getY();
		z = furnace.getZ();
	}

	public String getCreator() {
		return creator;
	}

	public String getWorld() {
		return world;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof WorkingFurnace))
			return false;
		WorkingFurnace other = (WorkingFurnace) obj;
		if(x == other.getX() && y == other.getY() && z == other.getZ() && world.equals(other.getWorld()))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 37 * hash + this.x;
		hash = 37 * hash + this.y;
		hash = 37 * hash + this.z;
		hash = 37 * hash + (this.world != null ? this.world.hashCode() : 0);
		return hash;
	}

	public Block getBlock() {
		System.out.println("Getting "+world);
		return Bukkit.getWorld(world).getBlockAt(x,y,z);
	}
}
