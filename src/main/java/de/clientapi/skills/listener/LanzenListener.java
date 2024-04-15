package de.clientapi.skills.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.ChatColor;

import java.util.List;

public class LanzenListener implements Listener {

    @EventHandler
    public void onPlayerHitByLance(EntityDamageByEntityEvent event) {
        //System.out.println("EntityDamageByEntityEvent triggered"); // Debug message

        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            //System.out.println("Both entities are players"); // Debug message

            Player damaged = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            ItemStack itemInHand = damager.getInventory().getItemInMainHand();
            if (itemInHand.getType().equals(Material.IRON_SWORD)) {
                ItemMeta meta = itemInHand.getItemMeta();
                if (meta != null && meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    if (lore.contains(ChatColor.DARK_GRAY + "Lanze")) {
                        //System.out.println("Damager is holding a lance"); // Debug message

                        // Überprüfen, ob der beschädigte Spieler auf einem Pferd sitzt
                        if (damaged.getVehicle() instanceof Horse) {
                            //System.out.println("Damaged player is on a horse"); // Debug message

                            Horse horse = (Horse) damaged.getVehicle();

                            // beschädigten Spieler vom Pferd werfen
                            horse.eject();

                            // Titel-Nachricht senden
                            damaged.sendTitle(ChatColor.RED + "Du wurdest vom Pferd gestoßen", "", 10, 70, 20);

                            // Blindheit für 3 Sekunden hinzufügen
                            damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 1));
                            // Slowness für 7 Sekunden hinzufügen
                            damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 7 * 20, 2));
                        } else {
                            //System.out.println("Damaged player is not on a horse"); // Debug message
                        }
                    } else {
                        //System.out.println("Damager is not holding a lance"); // Debug message
                    }
                }
            } else {
                //System.out.println("One or both entities are not players"); // Debug message
            }
        }
    }
}