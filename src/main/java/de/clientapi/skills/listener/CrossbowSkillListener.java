package de.clientapi.skills.listener;

import de.clientapi.skills.DatabaseManager;
import de.clientapi.skills.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.sql.SQLException;
import java.util.Random;

public class CrossbowSkillListener implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onShootCrossbow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        // Überprüfen, ob das verwendete Material eine Armbrust ist
        if (event.getBow().getType() != Material.CROSSBOW) {
            return;
        }

        Player player = (Player) event.getEntity();
        DatabaseManager dbManager = Main.getInstance().getDatabaseManager();
        int level;
        try {
            level = dbManager.getLevel(player.getUniqueId().toString(), "crossbow");
            System.out.println("Crossbow skill level: " + level); // Debug-Ausgabe
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

            // Reduce the player's health by half a heart
            player.setHealth(Math.max(0, player.getHealth() - 1));

            // Generate a random number to determine which message to display
            double rand = random.nextDouble();

            // Send a red message to the player's action bar
            if (rand < 0.4) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Du hast deine Hand an der Sehne verletzt"));
            } else if (rand < 0.8) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Du dir deine Finger verletzt"));
            } else {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Du hast dir den Pfeil in den Fuß geschossen"));
            }

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