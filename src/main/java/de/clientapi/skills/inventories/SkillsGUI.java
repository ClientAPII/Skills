package de.clientapi.skills.inventories;

import de.clientapi.skills.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.SQLException;
import java.util.Arrays;

public class SkillsGUI {
    private final DatabaseManager dbManager;

    public SkillsGUI(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public Inventory createGUI(OfflinePlayer player) throws SQLException {
        Inventory inv = Bukkit.createInventory(null, 36, "Skills");

        int bowLevel = dbManager.getLevel(player.getUniqueId().toString(), "bow");
        int swordLevel = dbManager.getLevel(player.getUniqueId().toString(), "sword");

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

        inv.setItem(4, head);
        inv.setItem(21, bow);
        inv.setItem(23, sword);

        return inv;
    }

    public void openGUI(Player viewer, OfflinePlayer target) throws SQLException {
        viewer.openInventory(createGUI(target));
    }
}