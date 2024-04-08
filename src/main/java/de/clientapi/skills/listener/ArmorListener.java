package de.clientapi.skills.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack[] armor = player.getInventory().getArmorContents();

        if (isWearingFullArmorOfType(armor, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS)) {
            player.setWalkSpeed(0.16f);
        } else if (isWearingFullArmorOfType(armor, Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS)) {
            player.setWalkSpeed(0.24f);
        } else {
            player.setWalkSpeed(0.2f); // Normal speed
        }
    }

    private boolean isWearingFullArmorOfType(ItemStack[] armor, Material helmet, Material chestplate, Material leggings, Material boots) {
        return armor[3] != null && armor[3].getType() == helmet &&
                armor[2] != null && armor[2].getType() == chestplate &&
                armor[1] != null && armor[1].getType() == leggings &&
                armor[0] != null && armor[0].getType() == boots;
    }
}