package me.guillaume.minecraftYdaysB2.util;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {

    public static final ItemStack Banner = getItem(new ItemStack(Material.CYAN_BANNER) ,ChatColor.DARK_AQUA + "Menu des équipes", ChatColor.MAGIC + "Description pas tres interessante");
    public static final ItemStack JoinTeam = getItem(new ItemStack(Material.GREEN_BANNER), ChatColor.GREEN +"Rejoindre une équipe","Pour sélectionner une équipe");
    public static final ItemStack LeaveTeam = getItem(new ItemStack(Material.RED_BANNER), ChatColor.RED + "Quittez l'équipe","Pour quitter une équipe");
    public static final ItemStack CreateTeam = getItem(new ItemStack(Material.FLOWER_BANNER_PATTERN), ChatColor.DARK_AQUA + "Creer une équipe","Pour creer une équipe");
    public static final int TeamMenuSize = 9*5;

    public static ItemStack getItem(ItemStack item, String name, String ... description){
        ItemMeta meta = item.getItemMeta();
        if (meta != null){
            meta.setDisplayName(name);
            List<String> lore = new ArrayList<>();

            for (String s : description){
                lore.add(s);
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static Inventory createInventory(Player player,String title){
        ItemStack decoration = getItem(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE)," "," ");
        Inventory inventory = Bukkit.createInventory(player, TeamMenuSize, title);
        for (int i = 0 ; i < TeamMenuSize ; i++){
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemStack(decoration));
            }
        }
        return inventory;
    }



}
