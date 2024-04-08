package de.clientapi.skills.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class GiveLanzeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Dieser Befehl kann nur von Spielern verwendet werden.");
            return true;
        }

        Player player = (Player) sender;

        // Überprüfen, ob der Befehl '/lanze' ist
        if (cmd.getName().equalsIgnoreCase("lanze")) {
            // Lanze erstellen
            ItemStack lance = new ItemStack(Material.IRON_SWORD);
            ItemMeta meta = lance.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + "Lanze");
            // Set lore
            List<String> lore = Arrays.asList(ChatColor.DARK_GRAY + "Lanze");
            meta.setLore(lore);

            lance.setItemMeta(meta);

            // Lanze dem Spieler geben
            player.getInventory().addItem(lance);
            player.sendMessage(ChatColor.GREEN + "Du hast die Lanze erhalten.");
            return true;
        }
        return false;
    }
}