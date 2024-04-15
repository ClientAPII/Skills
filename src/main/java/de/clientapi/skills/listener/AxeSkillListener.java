package de.clientapi.skills.listener;

import de.clientapi.skills.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


import java.sql.SQLException;
import java.util.Random;


//TODO: Hit abfangen bevor der Schaden angewendet wird

public class AxeSkillListener implements Listener {
    private final Random random = new Random();
    private final DatabaseManager dbManager;
    private final Plugin plugin;


    public AxeSkillListener(DatabaseManager dbManager, Plugin plugin) {
        this.dbManager = dbManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() != Material.IRON_AXE && item.getType() != Material.DIAMOND_AXE &&
                item.getType() != Material.GOLDEN_AXE && item.getType() != Material.STONE_AXE &&
                item.getType() != Material.WOODEN_AXE && item.getType() != Material.NETHERITE_AXE) {
            return;
        }

        try {
            applyDamageModifiers(player, event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void applyDamageModifiers(Player player, EntityDamageByEntityEvent event) throws SQLException {
        int level = dbManager.getLevel(player.getUniqueId().toString(), "axe");
        double noDamageChance;
        double reducedDamageChance;
        double reducedDamageMultiplier;
        int cooldownTicks;

        switch (level) {
            case 1:
                noDamageChance = 0;
                reducedDamageChance = 0.8;
                reducedDamageMultiplier = 0.5;
                cooldownTicks = 2;
                break;
            case 2:
                noDamageChance = 0;
                reducedDamageChance = 0.5;
                reducedDamageMultiplier = 0.7;
                cooldownTicks = 4;
                break;
            case 3:
                noDamageChance = 0;
                reducedDamageChance = 0.3;
                reducedDamageMultiplier = 0.85;
                cooldownTicks = 8;
                break;
            case 4:
                noDamageChance = 0;
                reducedDamageChance = 0;
                reducedDamageMultiplier = 1;
                cooldownTicks = 16;
                break;
            case 5:
                noDamageChance = 0;
                reducedDamageChance = -0.1; // Negative value indicates increased damage
                reducedDamageMultiplier = 1.1;
                cooldownTicks = 25;
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

        // Check if the damaged entity is a player and is blocking with a shield
        if (event.getEntity() instanceof Player) {
            Player damagedPlayer = (Player) event.getEntity();
            ItemStack mainHandItem = damagedPlayer.getInventory().getItemInMainHand();
            ItemStack offhandItem = damagedPlayer.getInventory().getItemInOffHand();
            if ((mainHandItem.getType() == Material.SHIELD || offhandItem.getType() == Material.SHIELD) && damagedPlayer.isBlocking()) {
                // Angriff stornieren, indem der Schaden auf 0 gesetzt wird
                event.setDamage(0);
                // Schild entfernen
                if (mainHandItem.getType() == Material.SHIELD) {
                    damagedPlayer.getInventory().setItemInMainHand(null);
                } else {
                    damagedPlayer.getInventory().setItemInOffHand(null);
                }
                // Schild nach einer kurzen Verzögerung wieder hinzufügen
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (mainHandItem.getType() == Material.SHIELD) {
                        damagedPlayer.getInventory().setItemInMainHand(mainHandItem);
                    } else {
                        damagedPlayer.getInventory().setItemInOffHand(offhandItem);
                    }
                    // Cooldown des Schildes basierend auf dem Level des angreifenden Spielers setzen
                    damagedPlayer.setCooldown(Material.SHIELD, cooldownTicks * 20);
                }, 2L); // 20 Ticks Verzögerung, entspricht 1 Sekunde

                // Cooldown des Schildes basierend auf dem Level des angreifenden Spielers setzen
                damagedPlayer.setCooldown(Material.SHIELD, cooldownTicks * 20);
            }
        }
    }
}