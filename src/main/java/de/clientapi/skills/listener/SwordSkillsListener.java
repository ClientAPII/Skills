package de.clientapi.skills.listener;

import de.clientapi.skills.DatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Random;

public class SwordSkillsListener implements Listener {
    private final Random random = new Random();
    private final DatabaseManager dbManager;

    public SwordSkillsListener(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.IRON_SWORD && item.getType() != Material.DIAMOND_SWORD &&
                item.getType() != Material.GOLDEN_SWORD && item.getType() != Material.STONE_SWORD &&
                item.getType() != Material.WOODEN_SWORD && item.getType() != Material.NETHERITE_SWORD) {
            return;
        }

        try {
            applyDamageModifiers(player, event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void applyDamageModifiers(Player player, EntityDamageByEntityEvent event) throws SQLException {
        int level = dbManager.getLevel(player.getUniqueId().toString(), "sword");
        double noDamageChance;
        double reducedDamageChance;
        double reducedDamageMultiplier;

        switch (level) {
            case 1:
                noDamageChance = 0.25;
                reducedDamageChance = 0.8;
                reducedDamageMultiplier = 0.5;
                break;
            case 2:
                noDamageChance = 0.05;
                reducedDamageChance = 0.5;
                reducedDamageMultiplier = 0.7;
                break;
            case 3:
                noDamageChance = 0.01;
                reducedDamageChance = 0.3;
                reducedDamageMultiplier = 0.85;
                break;
            case 4:
                noDamageChance = 0;
                reducedDamageChance = 0;
                reducedDamageMultiplier = 1;
                break;
            case 5:
                noDamageChance = 0;
                reducedDamageChance = -0.1; // Negative value indicates increased damage
                reducedDamageMultiplier = 1.1;
                break;
            default:
                return;
        }

        double rand = random.nextDouble();

        if (rand < noDamageChance) {
            event.setDamage(0);
        } else if (rand < noDamageChance + Math.abs(reducedDamageChance)) {
            event.setDamage(event.getDamage() * reducedDamageMultiplier);
        }

        // Check if the player has a shield in their offhand
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        if (offhandItem.getType() == Material.SHIELD) {
            // If they do, apply a 40% damage reduction
            event.setDamage(event.getDamage() * 0.8);
        }
    }
}