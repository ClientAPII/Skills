package de.clientapi.skills.inventories;

import de.clientapi.skills.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.Arrays;

public class SkillsGUI {
    private final DatabaseManager dbManager;

    public SkillsGUI(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public Inventory createGUI(Player player) throws SQLException {
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

        inv.setItem(10, bow);
        inv.setItem(13, sword);

        return inv;
    }

    public void openGUI(Player player) throws SQLException {
        player.openInventory(createGUI(player));
    }
}