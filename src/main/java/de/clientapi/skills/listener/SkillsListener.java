package de.clientapi.skills.listener;

import de.clientapi.skills.DatabaseManager;
import de.clientapi.skills.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;


public class SkillsListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        String name = event.getPlayer().getName();
        DatabaseManager dbManager = Main.getInstance().getDatabaseManager();

        try {
            if (!dbManager.doesPlayerExist(uuid)) {
                dbManager.createPlayer(uuid, name, 1, 1);
            } else {
                dbManager.updatePlayerName(uuid, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}