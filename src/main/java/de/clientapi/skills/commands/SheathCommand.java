package de.clientapi.skills.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


//Dieses Feature existiert nicht mehr in der aktuellen Version des Plugins.

public class SheathCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();
        ItemStack itemInHand = inventory.getItemInMainHand();

        // Prüfen, ob der Spieler ein Item in der Hand hält
        if (itemInHand == null) {
            player.sendMessage("Du hältst kein Item in der Hand.");
            return true;
        }

        // Prüfen, ob der Beinlinge-Slot des Spielers leer ist
        if (inventory.getBoots() != null) {
            player.sendMessage("Du hast bereits ein Item in den Beinen.");
            return true;
        }

        // Das Item in der Hand des Spielers in den Beinlinge-Slot verschieben
        inventory.setItemInMainHand(null);
        inventory.setBoots(itemInHand);

        player.sendMessage("Du hast dein Schwert weggesteckt.");

        return true;
    }
}