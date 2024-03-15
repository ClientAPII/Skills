package de.clientapi.skills.commands;

import de.clientapi.skills.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class SkillsCommand implements CommandExecutor {
    private final DatabaseManager dbManager;

    public SkillsCommand(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        try {
            int bowLevel = dbManager.getLevel(player.getUniqueId().toString(), "bow");
            int swordLevel = dbManager.getLevel(player.getUniqueId().toString(), "sword");
            player.sendMessage("Your bow skill level is: " + bowLevel);
            player.sendMessage("Your sword skill level is: " + swordLevel);
        } catch (SQLException e) {
            player.sendMessage("An error occurred while retrieving your skill levels.");
            e.printStackTrace();
        }

        return true;
    }
}