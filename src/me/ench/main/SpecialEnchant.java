package me.ench.main;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.zach.DesertMC.GameMechanics.Events;
import me.zach.DesertMC.Utils.ActionBar.ActionBar;
import me.zach.DesertMC.Utils.ActionBar.ActionBarUtils;
import me.zach.DesertMC.Utils.MiscUtils;
import me.zach.DesertMC.Utils.Particle.ParticleEffect;
import me.zach.DesertMC.Utils.PlayerUtils;
import me.zach.DesertMC.Utils.StringUtils.StringUtil;
import me.zach.DesertMC.Utils.ench.CustomEnch;
import me.zach.DesertMC.Utils.invisible.PlayerInvisible;
import me.zach.DesertMC.events.FallenDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static me.ench.main.RefineryUtils.specialScan;

public enum SpecialEnchant implements Listener {
    SPIKED("Spiked", "Crouch repeatedly and quickly to propel yourself in the direction you are looking. Can be used once every 15 seconds.", "Quicker activation", true){
        HashMap<UUID, Integer> spikedCrouched = new HashMap<>();
        @EventHandler
        public void spiked(PlayerToggleSneakEvent e){
            Player p = e.getPlayer();
            UUID uuid = p.getUniqueId();
            int occurrences = specialScan(p, name());
            if(occurrences > 0){
                int spikedCount = spikedCrouched.getOrDefault(uuid, 0);
                if(spikedCount > -1){
                    spikedCrouched.put(uuid, spikedCount++);
                    if(spikedCount == 1){
                        Bukkit.getScheduler().runTaskLater(EnchantRefineryMain.instancethis, () -> spikedCrouched.remove(uuid), 100);
                    }else if(spikedCount >= (8 - occurrences) * 2){
                        p.getVelocity().multiply(p.getLocation().getDirection().multiply(2));
                        spikedCrouched.put(uuid, -1);
                        Bukkit.getScheduler().runTaskLater(EnchantRefineryMain.instancethis, () -> spikedCrouched.remove(uuid), 300);
                    }
                }
            }else spikedCrouched.remove(uuid);
        }
    },
    GRAND_ESCAPE("Grand Escape", "Crouch, jump, and right click at the same time to render yourself invisible until you attack or start getting attacked. Can be used once per life.", "+1 hit taken before expiring", true){
        HashMap<UUID, GrandEscape> active = new HashMap<>();
        @EventHandler
        public void grandEscape(PlayerInteractEvent event){
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            if(!active.containsKey(uuid)){
                int occurrences = specialScan(player, name());
                if(occurrences > 0){
                    Action action = event.getAction();
                    if((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && player.isSneaking() && !((Entity) player).isOnGround()){
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "You made your " + ChatColor.BOLD + "GRAND ESCAPE!" + "\n" + ChatColor.LIGHT_PURPLE + "You'll be invisible until you hit a player, or until you get hit.");
                        active.put(uuid, new GrandEscape(player, occurrences));
                    }
                }
            }
        }
        @EventHandler
        public void playerDeath(FallenDeathEvent event){
            if(active.containsKey(event.getPlayer().getUniqueId()))
                active.get(event.getPlayer().getUniqueId()).playerDeath(event);
        }

        @EventHandler
        public void playerLeave(PlayerQuitEvent event){
            if(active.containsKey(event.getPlayer().getUniqueId()))
                active.get(event.getPlayer().getUniqueId()).playerLeave();
        }

        @EventHandler
        public void playerDamageOtherPlayer(EntityDamageByEntityEvent event){
            UUID uuid = event.getDamager().getUniqueId();
            if(active.containsKey(uuid))
                active.get(uuid).onPlayerAttack();
        }

        @EventHandler
        public void playerDamage(EntityDamageByEntityEvent event){
            UUID uuid = event.getEntity().getUniqueId();
            if(active.containsKey(uuid))
                active.get(uuid).onPlayerDamage();
        }

        class GrandEscape {
            private int hitsTaken = 0;
            final PlayerInvisible invisible;
            private final int occurrences;

            GrandEscape(Player player, int occurrences){
                this.invisible = new PlayerInvisible(player, EnchantRefineryMain.instancethis);
                this.occurrences = occurrences;
                invisible.setInvisible(true);
            }

            public void onPlayerDamage(){
                if(isActive()){
                    hitsTaken++;
                    if(hitsTaken >= occurrences){
                        renderInactive();
                    }
                }
            }

            public void onPlayerAttack(){
                if(isActive()){
                    renderInactive();
                }
            }

            public void playerDeath(FallenDeathEvent event){
                if(!event.isCancelled() && isActive()) renderInactive();
            }

            public void playerLeave(){
                if(isActive()) renderInactive();
            }

            public boolean isActive(){
                return invisible.isInvisible();
            }

            public void renderInactive(){
                invisible.setInvisible(false);
                active.remove(invisible.getPlayerUUID());
            }
        }
    },
    SELF_DESTRUCT("Self Destruct", "Hold down block (with sword) to unleash a giant explosion, killing you, and dealing 14 hp of damage (armor ignored) to anyone within 12 blocks.", "+1 damage", true){
        public static final int SECONDS = 3;
        @EventHandler
        public void interact(PlayerInteractEvent event){
            Player player = event.getPlayer();
            int occurrences = specialScan(player, name());
            if(occurrences > 0){
                UUID uuid = player.getUniqueId();
                float blockTime = Events.getBlockingTime(uuid);
                boolean hasBar = ActionBarUtils.getActionBar(uuid) instanceof SDActionBar;
                if(blockTime > 0.5f){
                    if(blockTime > SECONDS){
                        int damage = 14 + occurrences;
                        //self destruct!
                        ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 1f, 5, player.getLocation(), 50);
                        List<String> names = new ArrayList<>();
                        for(Damageable damageable : MiscUtils.getNearbyDamageables(player, 12)){
                            PlayerUtils.trueDamage(damageable, damage, player);
                            damageable.sendMessage(ChatColor.YELLOW + "You were dealt " + ChatColor.RED + damage + " damage by " + player.getName() + "'s " + ChatColor.LIGHT_PURPLE + " Self-Destruct!");
                            names.add(MiscUtils.getRankColor(damageable.getUniqueId()) + damageable.getName());
                        }
                        StringBuilder message = new StringBuilder(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "BOOM " + ChatColor.LIGHT_PURPLE + "You self destructed, damaging ");
                        for(int i = 0, size = names.size(); i<size; i++){
                            String name = names.get(i);
                            if(i < size - 1) message.append(ChatColor.LIGHT_PURPLE).append(", ").append(name);
                            else message.append(ChatColor.LIGHT_PURPLE).append(", and ").append(name).append(ChatColor.LIGHT_PURPLE).append("!");
                        }
                        player.sendMessage(message.toString());
                    }else if(hasBar){
                        ActionBarUtils.setActionBar(player, new SDActionBar(blockTime));
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 10, blockTime + 0.5f);
                    }
                }else if(blockTime == 0 && hasBar) ActionBarUtils.clearActionBar(player);
            }
        }

        class SDActionBar extends ActionBar {
            final float blockTime;
            final String message;
            private static final char PROGRESS_TICK = '\u258A';
            public SDActionBar(float blockTime){
                this.blockTime = blockTime;
                int barBlockSeconds = (int) Math.floor(blockTime * 3f);
                int barBlockSecondsToReach = SECONDS * 3;
                StringBuilder builder = new StringBuilder(barBlockSeconds % 2 == 0 ? ChatColor.LIGHT_PURPLE.toString() : ChatColor.RED + ChatColor.BOLD.toString() + "SELF-DESTRUCT " + ChatColor.GREEN);
                for(int i = 0; i<barBlockSeconds; i++){
                    builder.append(PROGRESS_TICK);
                }
                builder.append(ChatColor.RED);
                for(int i = 0; i<barBlockSecondsToReach - barBlockSeconds; i++){
                    builder.append(PROGRESS_TICK);
                }
                this.message = builder.toString();
            }

            public String getMessage(){
                return message;
            }
        }
    };

    public ItemStack applyTo(ItemStack book){
        ItemMeta meta = book.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add("");
        lore.add(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + name);
        lore.addAll(desc);
        meta.setLore(lore);
        book.setItemMeta(meta);
        NBTItem nbt = new NBTItem(book);
        NBTCompound customAttr = nbt.getCompound("CustomAttributes");
        if(customAttr == null) customAttr = nbt.addCompound("CustomAttributes");
        NBTCompound ench = customAttr.getCompound("enchantments");
        if(ench == null) ench = customAttr.addCompound("enchantments");
        NBTCompound special = ench.addCompound("Special");
        special.setString("ENCH_ID", name());
        return book;
    }

    List<String> desc;
    String name;

    SpecialEnchant(String name, String desc, String perOccurrence, boolean registerEvents){
        this.desc = StringUtil.wrapLore(desc + "\n" + ChatColor.GRAY + "Per occurrence in weapon and armor: " + ChatColor.LIGHT_PURPLE + perOccurrence);
        this.name = ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + name;
        if(registerEvents) Bukkit.getPluginManager().registerEvents(this, EnchantRefineryMain.instancethis);
    }
}
