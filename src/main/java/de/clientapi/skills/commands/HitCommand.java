package de.clientapi.skills.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import de.clientapi.skills.Main;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HitCommand implements CommandExecutor {

    private Main plugin;

    public HitCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("hit").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }

        Player player = (Player) sender;

        // Simulieren Sie das Verhalten eines Lanzenangriffs
        // Sie müssen diese Methode entsprechend Ihrer Implementierung von Lanzenangriffen anpassen
        simulateLanceAttack(player);

        return true;
    }

    private void simulateLanceAttack(Player player) {
        // Simulieren Sie das Verhalten eines Lanzenangriffs
        // Dieser Code ist basierend auf Ihrem LanzenListener

        if (player.getVehicle() instanceof Horse) {
            Horse horse = (Horse) player.getVehicle();

            horse.eject();

            ArmorStand armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setGravity(true);
            armorStand.setInvulnerable(true);

            armorStand.addPassenger(player);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                armorStand.remove();
            }, 20L * 5);

            player.sendTitle(ChatColor.RED + "Du wurdest vom Pferd gestoßen", "", 10, 70, 20);

            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 7 * 20, 2));
        }
    }
}