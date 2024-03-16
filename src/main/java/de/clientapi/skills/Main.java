// Main.java
package de.clientapi.skills;

import de.clientapi.skills.commands.SkillSetCommand;
import de.clientapi.skills.commands.SkillsCommand;
import de.clientapi.skills.listener.BowSkillsListener;
import de.clientapi.skills.listener.SkillsGUIListener;
import de.clientapi.skills.listener.SkillsListener;
import de.clientapi.skills.listener.SwordSkillsListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {
    private static Main instance;
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
        getCommand("skills").setExecutor(new SkillsCommand(databaseManager));
        getServer().getPluginManager().registerEvents(new SkillsGUIListener(), this);
        SkillSetCommand skillSetCommand = new SkillSetCommand(databaseManager);
        getCommand("skillset").setExecutor(skillSetCommand);
        getCommand("skillset").setTabCompleter(skillSetCommand);

        // Register listeners
        getServer().getPluginManager().registerEvents(new SwordSkillsListener(databaseManager), this);
        getServer().getPluginManager().registerEvents(new SkillsListener(), this);
        getServer().getPluginManager().registerEvents(new BowSkillsListener(), this);
    }

    public static Main getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}