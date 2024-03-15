package de.clientapi.skills.commands;

import de.clientapi.skills.DatabaseManager;
import de.clientapi.skills.inventories.SkillsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.sql.SQLException;

public class SkillsCommand implements CommandExecutor {
    private final SkillsGUI skillsGUI;

    public SkillsCommand(DatabaseManager dbManager) {
        this.skillsGUI = new SkillsGUI(dbManager);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        try {
            skillsGUI.openGUI(player);
        } catch (SQLException e) {
            player.sendMessage("An error occurred while opening the skills GUI.");
            e.printStackTrace();
        }

        return true;
    }
}