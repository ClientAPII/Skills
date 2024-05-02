// Main.java
package de.clientapi.skills;

import de.clientapi.skills.commands.*;
import de.clientapi.skills.inventories.SkillsGUI;
import de.clientapi.skills.listener.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {
    private static Main instance;
    public static final String PREFIX = ChatColor.GOLD + "[Skills] " + ChatColor.RESET;
    public static void sendMessageWithPrefix(CommandSender sender, String message) {
        sender.sendMessage(Main.PREFIX + message);
    }
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        instance = this;
        databaseManager = new DatabaseManager();

        // Load config.yml
        saveDefaultConfig();

        // Get MySQL login credentials from config.yml
        String host = getConfig().getString("host", "localhost");
        String username = getConfig().getString("username", "username");
        String password = getConfig().getBoolean("usePassword", true) ? getConfig().getString("password", "password") : "";
        String databaseName = getConfig().getString("databasename", "SkillsPlugin");

        // Construct the database URL using the host value
        String url = "jdbc:mysql://" + host + ":3306";

        try {
            databaseManager.connect(url, username, password, databaseName);
        } catch (SQLException e) {
            getLogger().severe("Could not connect to the database.");
            e.printStackTrace();
        }

        // Register commands
        this.getCommand("lanze").setExecutor(new GiveLanzeCommand());
        getCommand("skills").setExecutor(new SkillsCommand(databaseManager));
        getServer().getPluginManager().registerEvents(new SkillsGUIListener(), this);
        SkillSetCommand skillSetCommand = new SkillSetCommand(databaseManager);
        getCommand("skillset").setExecutor(skillSetCommand);
        getCommand("skillset").setTabCompleter(skillSetCommand);
        //this.getCommand("sheath").setExecutor(new SheathCommand());
        new HitCommand(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new SkillsGUI(), this);
        getServer().getPluginManager().registerEvents(new SkillsCommand(databaseManager), this);
        getServer().getPluginManager().registerEvents(new SwordSkillsListener(databaseManager), this);
        getServer().getPluginManager().registerEvents(new SkillsListener(), this);
        getServer().getPluginManager().registerEvents(new BowSkillsListener(), this);
        getServer().getPluginManager().registerEvents(new LanzenListener(this), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(), this);
        getServer().getPluginManager().registerEvents(new CrossbowSkillListener(), this);
        getServer().getPluginManager().registerEvents(new AxeSkillListener(databaseManager, this), this);

    }

    public static Main getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}