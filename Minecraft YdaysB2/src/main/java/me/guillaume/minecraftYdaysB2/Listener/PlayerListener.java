package me.guillaume.minecraftYdaysB2.Listener;
import me.guillaume.minecraftYdaysB2.util.ItemUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener extends ItemUtil implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Inventory playerInventory = player.getInventory();
        for (ItemStack item : playerInventory.getContents()) {
            if (item != null && item.isSimilar(Banner)) {
                    return;
            }
        }
        playerInventory.setItem(1,Banner);
    }
}
