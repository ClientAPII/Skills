package de.clientapi.skills.listener;

import de.clientapi.skills.DatabaseManager;
import de.clientapi.skills.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class AxeSkillListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() != Material.IRON_AXE) {
            return;
        }

        String uuid = player.getUniqueId().toString();
        DatabaseManager dbManager = Main.getInstance().getDatabaseManager();

        try {
            int currentLevel = dbManager.getLevel(uuid, "axe");
            applyDamageModifiers(currentLevel, event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO Implement the different shield cooldowns

    private void applyDamageModifiers(int level, EntityDamageByEntityEvent event) {
        double damageMultiplier;
        boolean canBreakShield;

        switch (level) {
            case 1:
                damageMultiplier = 0.2;
                canBreakShield = false;
                break;
            case 2:
            case 3:
                damageMultiplier = 0.4;
                canBreakShield = false;
                break;
            case 4:
                damageMultiplier = 1;
                canBreakShield = true;
                break;
            case 5:
                damageMultiplier = 1.1;
                canBreakShield = true;
                break;
            default:
                return;
        }

        event.setDamage(event.getDamage() * damageMultiplier);

        if (!canBreakShield && event.getEntity() instanceof Player) {
            Player damagedPlayer = (Player) event.getEntity();
            ItemStack offhandItem = damagedPlayer.getInventory().getItemInOffHand();
            if (offhandItem.getType() == Material.SHIELD && event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                if (damager.getInventory().getItemInMainHand().getType() == Material.IRON_AXE) {
                    event.setCancelled(true);
                }
            }
        }
    }
}