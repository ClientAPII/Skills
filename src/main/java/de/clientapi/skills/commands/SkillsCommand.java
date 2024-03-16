package de.clientapi.skills.commands;

import de.clientapi.skills.DatabaseManager;
import de.clientapi.skills.inventories.SkillsGUI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        Player viewer = (Player) sender;
        OfflinePlayer target;

        if (viewer.hasPermission("skills.admin") && args.length > 0) {
            target = Bukkit.getOfflinePlayer(args[0]);
        } else {
            target = viewer;
        }

        try {
            skillsGUI.openGUI(viewer, target);
        } catch (SQLException e) {
            viewer.sendMessage("An error occurred while opening the skills GUI.");
            e.printStackTrace();
        }

        return true;
    }
}