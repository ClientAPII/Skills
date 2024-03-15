package de.clientapi.skills.listener;

import de.clientapi.skills.DatabaseManager;
import de.clientapi.skills.Main;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class SwordSkillsListener implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() != Material.IRON_SWORD) {
            return;
        }

        // Check if the player is equipping or unequipping the sword
        if (event.getHand() == EquipmentSlot.HAND) {
            updateSwordAttributes(player, item);
        } else {
            // Revert to default sword attributes
            resetSwordAttributes(item);
        }
    }

    private void updateSwordAttributes(Player player, ItemStack sword) {
        DatabaseManager dbManager = Main.getInstance().getDatabaseManager();
        int level;
        try {
            level = dbManager.getLevel(player.getUniqueId().toString(), "sword");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        double newAttackSpeed;
        double damageMultiplier;

        switch (level) {
            case 1:
                newAttackSpeed = 0.5; // 50% of the normal speed
                damageMultiplier = 1.0; // Normal damage
                break;
            case 2:
                newAttackSpeed = 0.75;
                damageMultiplier = 0.7;
                break;
            case 3:
                newAttackSpeed = 0.9;
                damageMultiplier = 0.8;
                break;
            case 4:
                newAttackSpeed = 1.0;
                damageMultiplier = 0.9;
                break;
            case 5:
                newAttackSpeed = 1.1;
                damageMultiplier = 1.0;
                break;
            default:
                newAttackSpeed = 1.0;
                damageMultiplier = 1.0;
                break;
        }

        ItemMeta meta = sword.getItemMeta();
        // Remove old modifiers
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);

        // Always modify attack speed according to player's level
        AttributeModifier speedModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", newAttackSpeed - 4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedModifier);

        // Always modify damage according to player's level and whether they are holding a shield
        damageMultiplier = player.getInventory().getItemInOffHand().getType() == Material.SHIELD ? damageMultiplier * 0.6 : damageMultiplier;
        AttributeModifier damageModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", damageMultiplier, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);

        sword.setItemMeta(meta);
    }

    private void resetSwordAttributes(ItemStack sword) {
        ItemMeta meta = sword.getItemMeta();
        // Remove all attribute modifiers
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        sword.setItemMeta(meta);
    }
}
