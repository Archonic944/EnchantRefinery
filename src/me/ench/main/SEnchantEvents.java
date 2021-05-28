package me.ench.main;

import me.ench.main.RefineryUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static me.ench.main.RefineryUtils.specialScan;

public class SEnchantEvents implements Listener {
    HashMap<UUID, Integer> spikedCrouched = new HashMap<>();
    ArrayList<UUID> spikedCD = new ArrayList<>();
    @EventHandler
    public void spiked(PlayerToggleSneakEvent e){
        ItemStack item = e.getPlayer().getItemInHand();
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        boolean contains = spikedCrouched.containsKey(uuid);
        if(contains || specialScan(p, "SPIKED")){
            if(!contains) spikedCrouched.put(uuid, 0);
            spikedCrouched.put(uuid, spikedCrouched.get(uuid) + 1);
            if(spikedCrouched.get(uuid) >= 14){
                p.getVelocity().multiply(p.getLocation().getDirection().multiply(2));
                spikedCrouched.remove(uuid);
            }
            if(!contains) {
                new BukkitRunnable() {
                    public void run() {
                        spikedCrouched.remove(uuid);
                    }
                }.runTaskLater(Bukkit.getPluginManager().getPlugin("Enchant_Refinery"), 90);
            }
        }
    }
}
