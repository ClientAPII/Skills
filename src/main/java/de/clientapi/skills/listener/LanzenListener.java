package de.clientapi.skills.listener;

import de.clientapi.skills.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanzenListener implements Listener {
    private final Main plugin; // replace YourPluginClass with your main plugin class
    private final Map<Player, ArmorStand> playerArmorStands = new HashMap<>();

    public LanzenListener(Main plugin) { // replace YourPluginClass with your main plugin class
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();
        ItemStack itemInHand = damager.getInventory().getItemInMainHand();

        if (itemInHand.getType().equals(Material.IRON_SWORD)) {
            ItemMeta meta = itemInHand.getItemMeta();
            if (meta != null && meta.hasLore()) {
                List<String> lore = meta.getLore();
                if (lore.contains(ChatColor.DARK_GRAY + "Lanze")) {
                    if (damaged.getVehicle() instanceof Horse) {
                        Horse horse = (Horse) damaged.getVehicle();

                        horse.eject();

                        Location location = damaged.getLocation();
                        location.setY(location.getBlockY() - 1.98);

                        ArmorStand armorStand = damaged.getWorld().spawn(location, ArmorStand.class);
                        armorStand.setVisible(false);
                        armorStand.setGravity(true);
                        armorStand.setInvulnerable(true);

                        armorStand.addPassenger(damaged);

                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            armorStand.remove();
                        }, 20L * 5);

                        damaged.sendTitle(ChatColor.RED + "Du wurdest vom Pferd gestoßen", "", 10, 70, 20);

                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 1));
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 2));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ArmorStand armorStand = playerArmorStands.get(player);

        if (armorStand != null && !armorStand.getPassengers().contains(player)) {
            armorStand.addPassenger(player);
        }
    }
}