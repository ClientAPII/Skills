package de.clientapi.skills.listener;

import de.clientapi.skills.DatabaseManager;
import de.clientapi.skills.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.sql.SQLException;
import java.util.Random;

public class BowSkillsListener implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        DatabaseManager dbManager = Main.getInstance().getDatabaseManager();
        int level;
        try {
            level = dbManager.getLevel(player.getUniqueId().toString(), "bow");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        double noShootChance;
        double deviationFactor;

        switch (level) {
            case 1:
                noShootChance = 0.25;
                deviationFactor = 1.0; // High deviation
                break;
            case 2:
                noShootChance = 0.05;
                deviationFactor = 0.5; // Medium deviation
                break;
            case 3:
                noShootChance = 0.01;
                deviationFactor = 0.25; // Low deviation
                break;
            case 4:
                noShootChance = 0;
                deviationFactor = 0.05; // Very low deviation
                break;
            default:
                noShootChance = 0;
                deviationFactor = 0; // No deviation
                break;
        }

        if (random.nextDouble() < noShootChance) {
            event.setCancelled(true);
            return;
        }

        if (random.nextDouble() < deviationFactor) {
            // Create a random deviation vector
            Vector deviation = new Vector(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5).multiply(deviationFactor);

            // Add the deviation to the arrow's velocity
            event.getProjectile().setVelocity(event.getProjectile().getVelocity().add(deviation));
        }
    }
}