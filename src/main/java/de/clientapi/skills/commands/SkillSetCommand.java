package de.clientapi.skills.commands;

import de.clientapi.skills.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SkillSetCommand implements CommandExecutor, TabCompleter {
    private final DatabaseManager dbManager;

    public SkillSetCommand(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler verwendet werden.");
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage("Falsche Anzahl von Argumenten. Verwendung: /skillset <name> <skill> <level>");
            return true;
        }

        String playerName = args[0];
        String skill = args[1];
        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Level muss eine Zahl sein.");
            return true;
        }

        Player targetPlayer = Bukkit.getServer().getPlayer(playerName);
        if (targetPlayer == null) {
            sender.sendMessage("Spieler nicht gefunden.");
            return true;
        }

        UUID playerUUID = targetPlayer.getUniqueId();

        try {
            dbManager.setLevel(playerUUID.toString(), skill, level);
            sender.sendMessage("Das Level von " + playerName + " für " + skill + " wurde auf " + level + " gesetzt.");
        } catch (SQLException e) {
            sender.sendMessage("Ein Fehler ist aufgetreten beim Setzen des Skill-Levels.");
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("bow", "sword");
        }

        return null;
    }
}