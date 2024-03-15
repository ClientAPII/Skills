package de.clientapi.skills.listener;

import de.clientapi.skills.DatabaseManager;
import de.clientapi.skills.Main;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class SwordSkillsListener implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.IRON_SWORD) {
            return;
        }

        DatabaseManager dbManager = Main.getInstance().getDatabaseManager();
        int level;
        try {
            level = dbManager.getLevel(player.getUniqueId().toString(), "sword");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        double noSwingChance;
        double speedReductionChance;
        double newAttackSpeed;

        switch (level) {
            case 1:
                noSwingChance = 0.25;
                speedReductionChance = 0.8;
                newAttackSpeed = 0.5;
                break;
            case 2:
                noSwingChance = 0.05;
                speedReductionChance = 0.5;
                newAttackSpeed = 0.75;
                break;
            case 3:
                noSwingChance = 0;
                speedReductionChance = 0.15;
                newAttackSpeed = 0.9;
                break;
            case 4:
                noSwingChance = 0;
                speedReductionChance = 0;
                newAttackSpeed = 1.0;
                break;
            case 5:
                noSwingChance = 0;
                speedReductionChance = 0;
                newAttackSpeed = 1.1;
                break;
            default:
                noSwingChance = 0;
                speedReductionChance = 0;
                newAttackSpeed = 1.0;
                break;
        }

        if (random.nextDouble() < noSwingChance) {
            event.setCancelled(true);
            return;
        }

        ItemMeta meta = item.getItemMeta();

        // Remove old modifiers
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);

        // Add new modifiers
        if (random.nextDouble() < speedReductionChance) {
            AttributeModifier speedModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", newAttackSpeed, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedModifier);
        }

        if (player.getInventory().getItemInOffHand().getType() == Material.SHIELD) {
            AttributeModifier damageModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 0.6, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);
        }

        item.setItemMeta(meta);
    }
}