package src.Dockter.BoxFurnace;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.block.SpoutChest;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.PopupScreen;

import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.block.SpoutChest;
import org.getspout.spoutapi.event.input.InputListener;
import org.getspout.spoutapi.event.input.KeyPressedEvent;
import org.getspout.spoutapi.event.input.KeyReleasedEvent;
import org.getspout.spoutapi.event.input.RenderDistanceChangeEvent;
import org.getspout.spoutapi.gui.ScreenType;
import org.getspout.spoutapi.player.SpoutPlayer;
import java.util.logging.Level;
import java.util.logging.Logger;

import src.Dockter.BoxFurnace.DocSpoutMessages;

public class DBFPlayerListener implements Listener {
	public static Player player = null;
	public static Location locfurn = null;

	public DBFPlayerListener(final DocBoxFurnace plugin) {
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!((Boolean) DocBoxFurnace.config.get("leftclick")) && (event.getClickedBlock().getType() != Material.FURNACE || event.getClickedBlock().getType() != Material.BURNING_FURNACE/*||event.getClickedBlock().getType()!=Material.SIGN*/)) {
			if ((Boolean) DocBoxFurnace.config.get("announcements.debug")){
				player.sendMessage(ChatColor.GREEN + "[AutoFurnace] - Read Player Interact with Furnace Event");
			}
				
			//	player.sendMessage(ChatColor.GREEN + "[AutoFurnace] - Running");
			return;
		}

		Player player = event.getPlayer();
		Block isFurnace = event.getClickedBlock();

		if (player.hasPermission("docboxfurnace.use")) {
			if (isFurnace == null) {
				player.sendMessage(ChatColor.GREEN + "[AutoFurnace] - Event isFurnace returned null, event halted.");
				return;
			}
			if ((Boolean) DocBoxFurnace.config.get("announcements.debug")){
				player.sendMessage(ChatColor.GREEN + "[AutoFurnace] - Event Passed Permissions handler.");
			}
			if (event.getAction() == Action.LEFT_CLICK_BLOCK && isFurnace.getType().equals(Material.FURNACE) || isFurnace.getType().equals(Material.BURNING_FURNACE)) {
				if (DBFInventoryListener.getInputChest(isFurnace) != null && (isFurnace.getType().equals(Material.FURNACE) || isFurnace.getType().equals(Material.BURNING_FURNACE))) {
					Location locfurn = event.getClickedBlock().getLocation();
					DocBoxFurnace.usedplayers.put(locfurn, player);
					DBFInventoryListener.reload(isFurnace);
					//player.sendMessage(ChatColor.GREEN + "[AutoFurnace] 2");
				} else {
					Location locfurn = event.getClickedBlock().getLocation();
					DocBoxFurnace.usedplayers.put(locfurn, player);
					//player.sendMessage(ChatColor.GREEN + "[AutoFurnace] 3");
				}
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					Furnace state = (Furnace) event.getClickedBlock().getState();
					Inventory finventory = state.getInventory();
					ItemStack slot1 = finventory.getItem(1);
					//player.sendMessage(ChatColor.GREEN + "[AutoFurnace] 4");
					if (slot1 != null && slot1.getType() != Material.AIR) {
						//player.sendMessage(ChatColor.GREEN + "[AutoFurnace] 5");
						return;
					}

					if ((finventory.getItem(1) == null || finventory.getItem(1).getType() == Material.AIR) && DBFInventoryListener.getFuelChest(event.getClickedBlock()) != null) {
						DBFInventoryListener.reloadFuel(event.getClickedBlock());
						//player.sendMessage(ChatColor.GREEN + "[AutoFurnace] 6");
						//if ((Boolean) AutoFurnace.config.get("activated")) {
						//	SpoutPlayer sPlayer = SpoutManager.getPlayer(player);
						//	if (sPlayer.isSpoutCraftEnabled()) {
						//	DocSpoutMessages.sendNotification(sPlayer,
						//		"[AutoFurnace] Activated");}
						//else {
						//	player.sendMessage(ChatColor.GREEN + "[AutoFurnace] is now running!");
						//}
						//}
					}
				}
				//player.sendMessage(ChatColor.GREEN + "[AutoFurnace] is now running!");
			}
		}
	}
}