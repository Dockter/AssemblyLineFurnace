package src.Dockter.BoxFurnace;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.getspout.spoutapi.player.SpoutPlayer;

public class DocSpoutMessages {
	
   public static void sendNotification(SpoutPlayer sPlayer, String string) {
    	if (sPlayer.isSpoutCraftEnabled()  && (sPlayer instanceof SpoutPlayer)) {
   		if (string.length()<25) {
				sPlayer.sendNotification(sPlayer.getName(), string, Material.BURNING_FURNACE);
			} else {
				sPlayer.sendNotification(sPlayer.getName(), string.substring(0, 25), Material.BURNING_FURNACE);
			}
		} else {
			sPlayer.sendMessage(string);
		}
    }
}
