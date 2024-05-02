package de.clientapi.skills.inventories;

import de.clientapi.skills.DatabaseManager;
import de.clientapi.skills.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.SQLException;
import java.util.Arrays;

public class SkillsGUI implements Listener {
    public Inventory createGUI(OfflinePlayer player) {
        Inventory inv = Bukkit.createInventory(new SkillsInventoryHolder(), 27, ChatColor.AQUA + "Skills");
        String uuid = player.getUniqueId().toString();
        DatabaseManager dbManager = Main.getInstance().getDatabaseManager();

        try {
            dbManager.addMissingSkills(uuid); // Ensure all skills are present in the player's database entry

            int bowLevel = dbManager.getLevel(uuid, "bow");
            int swordLevel = dbManager.getLevel(uuid, "sword");
            int crossbowLevel = dbManager.getLevel(uuid, "crossbow");
            int axeLevel = dbManager.getLevel(uuid, "axe");

            ItemStack bow = new ItemStack(Material.BOW);
            ItemMeta bowMeta = bow.getItemMeta();
            bowMeta.setDisplayName(ChatColor.YELLOW + "Bogen Skill");
            bowMeta.setLore(Arrays.asList("Level: " + bowLevel));
            bow.setItemMeta(bowMeta);

            ItemStack sword = new ItemStack(Material.IRON_SWORD);
            ItemMeta swordMeta = sword.getItemMeta();
            swordMeta.setDisplayName(ChatColor.RED + "Schwert Skill");
            swordMeta.setLore(Arrays.asList("Level: " + swordLevel));
            sword.setItemMeta(swordMeta);

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            headMeta.setOwningPlayer(player);
            headMeta.setDisplayName(ChatColor.GREEN + player.getName() + "'s Skills");
            head.setItemMeta(headMeta);

            ItemStack crossbow = new ItemStack(Material.CROSSBOW);
            ItemMeta crossbowMeta = crossbow.getItemMeta();
            crossbowMeta.setDisplayName(ChatColor.YELLOW + "Armbrust Skill");
            crossbowMeta.setLore(Arrays.asList("Level: " + crossbowLevel));
            crossbow.setItemMeta(crossbowMeta);

            ItemStack axe = new ItemStack(Material.IRON_AXE);
            ItemMeta axeMeta = axe.getItemMeta();
            axeMeta.setDisplayName(ChatColor.RED + "Axt Skill");
            axeMeta.setLore(Arrays.asList("Level: " + axeLevel));
            axe.setItemMeta(axeMeta);

            inv.setItem(4, head);
            inv.setItem(21, bow);
            inv.setItem(23, sword);
            inv.setItem(20, crossbow);
            inv.setItem(24, axe);

            return inv;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof SkillsInventoryHolder) {
            event.setCancelled(true);
        }
    }

    public void openGUI(Player viewer, OfflinePlayer target) throws SQLException {
        viewer.openInventory(createGUI(target));
    }
}