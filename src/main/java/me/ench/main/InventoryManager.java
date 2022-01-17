package me.ench.main;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import static me.ench.main.RefineryUtils.instance;
import static me.ench.main.RefineryUtils.isRefineryOpen;

public class InventoryManager implements Listener {
    @EventHandler
    public void invEvent(InventoryClickEvent e){
        if(!isRefineryOpen.containsKey(e.getWhoClicked().getUniqueId().toString())) isRefineryOpen.put(e.getWhoClicked().getUniqueId().toString(), false);
        if(!instance.containsKey(e.getWhoClicked().getUniqueId())) instance.put(e.getWhoClicked().getUniqueId(), new RefineryInventory());
        instance.get(e.getWhoClicked().getUniqueId()).invClick(e);
    }

    @EventHandler
    public void DCset(PlayerQuitEvent event) {
        RefineryUtils.isRefineryOpen.put(event.getPlayer().getUniqueId().toString(), false);
    }

    @EventHandler
    public void noPickup(PlayerPickupItemEvent e) {
        if(RefineryUtils.isRefineryOpen.containsKey(e.getPlayer().getUniqueId().toString())) {
            if (RefineryUtils.isRefineryOpen.get(e.getPlayer().getUniqueId().toString())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void InventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase("Enchant Refinery")) {
            if(!instance.containsKey(event.getPlayer().getUniqueId())){
                instance.put(event.getPlayer().getUniqueId(), new RefineryInventory());
            }
            RefineryUtils.isRefineryOpen.put(event.getPlayer().getUniqueId().toString(), false);
            if(event.getInventory().getItem(12) != null) {
                event.getPlayer().getInventory().addItem(event.getInventory().getItem(12));
                instance.get(event.getPlayer().getUniqueId()).ableToRefine = 0;
                instance.get(event.getPlayer().getUniqueId()).hasHammer = false;
                instance.get(event.getPlayer().getUniqueId()).hammer = new ItemStack(Material.AIR);
            }

            if(event.getInventory().getItem(14) != null) {
                event.getPlayer().getInventory().addItem(event.getInventory().getItem(14));
                instance.get(event.getPlayer().getUniqueId()).ableToRefine = 0;
                instance.get(event.getPlayer().getUniqueId()).hasBook = false;
                instance.get(event.getPlayer().getUniqueId()).book = new ItemStack(Material.AIR);
            }

        }
    }

}
