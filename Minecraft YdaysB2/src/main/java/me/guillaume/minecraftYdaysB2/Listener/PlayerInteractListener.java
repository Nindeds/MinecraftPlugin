package me.guillaume.minecraftYdaysB2.Listener;

import me.guillaume.minecraftYdaysB2.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Locale;

public class PlayerInteractListener extends ItemUtil implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Inventory teamMenuInventory = createInventory(player, ChatColor.RED + "Menu des équipe");
        teamMenuInventory.setItem(20, JoinTeam);
        teamMenuInventory.setItem(22, LeaveTeam);
        teamMenuInventory.setItem(24, CreateTeam);

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand().isSimilar(Banner) && player.getOpenInventory().getType() != InventoryType.CHEST) {
                event.setCancelled(true);
                player.openInventory(teamMenuInventory);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Team playerTeam = scoreboard.getEntryTeam(player.getName());

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        event.setCancelled(true);

        String title = event.getView().getTitle();

        // Menu principal
        if (title.equals(ChatColor.RED + "Menu des équipe")) {
            if (clickedItem.isSimilar(JoinTeam)) {
                Inventory joinTeamInventory = createInventory(player, ChatColor.AQUA + "Choississez votre équipe");
                int slot = -1;
                for (Team team : scoreboard.getTeams()) {
                    ChatColor color = team.getColor();
                    Material woolMaterial;
                    switch (color) {
                        case RED: woolMaterial = Material.RED_WOOL; break;
                        case BLUE: woolMaterial = Material.BLUE_WOOL; break;
                        case GREEN: woolMaterial = Material.GREEN_WOOL; break;
                        case YELLOW: woolMaterial = Material.YELLOW_WOOL; break;
                        default: woolMaterial = Material.WHITE_WOOL; break;
                    }
                    ItemStack wool = new ItemStack(woolMaterial);
                    ItemMeta meta = wool.getItemMeta();
                    meta.setDisplayName(color + team.getName());
                    wool.setItemMeta(meta);
                    joinTeamInventory.setItem(slot + 1 , wool);
                    slot++;
                }
                player.openInventory(joinTeamInventory);

            } else if (clickedItem.isSimilar(LeaveTeam)) {
                if (playerTeam != null) {
                    playerTeam.removeEntry(player.getName());
                    player.sendMessage(ChatColor.YELLOW + "Tu as quitté ton équipe !");
                    if (playerTeam.getEntries().isEmpty()) {
                        playerTeam.unregister();
                        player.sendMessage(ChatColor.GRAY + "L'équipe a été supprimée car elle était vide.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Tu n'es dans aucune équipe !");
                }
            } else if (clickedItem.isSimilar(CreateTeam)) {
                Inventory createTeamInventory = createInventory(player, ChatColor.BLUE + "Create team");
                createTeamInventory.setItem(10, new ItemStack(Material.BLUE_WOOL));
                createTeamInventory.setItem(12, new ItemStack(Material.RED_WOOL));
                createTeamInventory.setItem(14, new ItemStack(Material.GREEN_WOOL));
                createTeamInventory.setItem(16, new ItemStack(Material.YELLOW_WOOL));
                player.openInventory(createTeamInventory);
            }

            // Menu "Create team"
        } else if (title.equals(ChatColor.BLUE + "Create team")) {
            Material type = clickedItem.getType();
            if (type.toString().endsWith("_WOOL")) {
                String colorName = type.toString().replace("_WOOL", "").toLowerCase(Locale.ROOT);
                String teamName = capitalize(colorName);

                if (playerTeam != null) {
                    playerTeam.removeEntry(player.getName());
                    player.sendMessage(ChatColor.YELLOW + "Tu as quitté ton équipe !");
                    if (playerTeam.getEntries().isEmpty()) {
                        playerTeam.unregister();
                        player.sendMessage(ChatColor.GRAY + "L'équipe a été supprimée car elle était vide.");
                    }
                }

                if (scoreboard.getTeam(teamName) == null) {
                    Team team = scoreboard.registerNewTeam(teamName);
                    team.setDisplayName(teamName);
                    team.setColor(ChatColor.valueOf(colorName.toUpperCase()));
                    team.addEntry(player.getName());

                    player.sendMessage(ChatColor.GREEN + "Équipe " + teamName + " créée et rejointe !");
                } else {
                    player.sendMessage(ChatColor.RED + "L’équipe " + teamName + " existe déjà.");
                }
                player.closeInventory();
            }


            // Menu "Join team"
        } else if (title.equals(ChatColor.AQUA + "Choississez votre équipe")) {
            Material type = clickedItem.getType();
            if (type.toString().endsWith("_WOOL")) {
                String colorName = type.toString().replace("_WOOL", "").toLowerCase(Locale.ROOT);
                String teamName = capitalize(colorName);
                Team team = scoreboard.getTeam(teamName);
                if (team.hasPlayer(player)) {
                    player.sendMessage(ChatColor.RED + "Vous êtes déjà dans l'équipe " + teamName + " !");
                    return;
                }
                if (playerTeam != null) {
                    playerTeam.removeEntry(player.getName());
                    player.sendMessage(ChatColor.YELLOW + "Tu as quitté ton équipe !");
                    if (playerTeam.getEntries().isEmpty()) {
                        playerTeam.unregister();
                        player.sendMessage(ChatColor.GRAY + "L'équipe a été supprimée car elle était vide.");
                    }
                }
                if (!(team == null)){
                    if(team.getEntries().size()>= 4) {
                        player.sendMessage(ChatColor.RED + "L'équipe "+ teamName + " est pleine.");
                        return;
                    }
                    team.addEntry(player.getName());
                    player.sendMessage(ChatColor.GREEN + "Tu as rejoint l'équipe " + teamName + " !");
                } else {
                    player.sendMessage(ChatColor.RED + "Cette équipe n'existe pas.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.isSimilar(CreateTeam) || item.isSimilar(LeaveTeam) || item.isSimilar(JoinTeam) || item.isSimilar(Banner)) {
            event.setCancelled(true);
        }
    }

    private String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
