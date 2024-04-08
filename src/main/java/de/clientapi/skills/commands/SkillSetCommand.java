package de.clientapi.skills.commands;

import de.clientapi.skills.DatabaseManager;
import de.clientapi.skills.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
            Main.sendMessageWithPrefix(sender, "Dieser Befehl kann nur von einem Spieler verwendet werden.");
            return true;
        }

        if (!sender.hasPermission("skills.admin.set")) {
            Main.sendMessageWithPrefix(sender, "Dazu hast du keine Rechte");
            return true;
        }

        if (args.length != 3) {
            Main.sendMessageWithPrefix(sender, "Falsche Anzahl von Argumenten. Verwendung: /skillset <name> <skill> <level>");
            return true;
        }

        String playerName = args[0];
        String skill = args[1];
        if (!skill.equals("bow") && !skill.equals("sword") && !skill.equals("crossbow") && !skill.equals("axe"))  {
            Main.sendMessageWithPrefix(sender, "Skill kann nur bow oder sword sein.");
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            Main.sendMessageWithPrefix(sender, "Level muss eine Zahl sein.");
            return true;
        }

        if (level < 1 || level > 5) {
            Main.sendMessageWithPrefix(sender, "Skill muss zwischen 1 und 5 sein.");
            return true;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(playerName);
        if (targetPlayer == null) {
            Main.sendMessageWithPrefix(sender, "Spieler nicht gefunden.");
            return true;
        }

        UUID playerUUID = targetPlayer.getUniqueId();

        try {
            dbManager.setLevel(playerUUID.toString(), skill, level);
            Main.sendMessageWithPrefix(sender, "Das Level von " + playerName + " für " + skill + " wurde auf " + level + " gesetzt.");
        } catch (SQLException e) {
            Main.sendMessageWithPrefix(sender, "Ein Fehler ist aufgetreten beim Setzen des Skill-Levels.");
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("skills.admin.set")) {
                return null;
            }
        }

        if (args.length == 2) {
            return Arrays.asList("bow", "Sword","axe","crossbow");
        } else if (args.length == 3) {
            return Arrays.asList("1", "2", "3", "4", "5");
        }

        return null;
    }
}