package me.ench.main;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.zach.DesertMC.GameMechanics.EXPMilesstones.MilestonesUtil;
import me.zach.DesertMC.Utils.nbt.NBTUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RefineryUtils {
    public static HashMap<String,Boolean> isRefineryOpen = new HashMap<>();
    public static HashMap<UUID, RefineryInventory> instance = new HashMap<>();
    public static boolean isHammer(ItemStack item) {
        if(item.getItemMeta() == null) return false;
        String name = item.getItemMeta().getDisplayName();
        if(name == null){
            return false;
        }
        return name.equalsIgnoreCase(ChatColor.AQUA + "Wood Hammer") || name.equalsIgnoreCase(ChatColor.AQUA + "Stone Hammer") || name.equalsIgnoreCase(ChatColor.AQUA + "Iron Hammer") || name.equalsIgnoreCase(ChatColor.AQUA + "Diamond Hammer") || name.equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Special Hammer");
    }

    public static boolean isBook(ItemStack item) {
        return NBTUtil.getCustomAttrString(item, "ID").equals("ENCHANTED_BOOK");
    }

    public static int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static ItemStack refine(ItemStack book, ItemStack hammer, boolean specialGuaranteed, Player p){
        NBTCompound bookCompound = new NBTItem(book).getCompound("CustomAttributes");
        NBTCompound hammerCompound = new NBTItem(hammer).getCompound("CustomAttributes");

        int realLevel = bookCompound.getInteger("REAL_LEVEL");
        int baseLevel = bookCompound.getInteger("BASE_LEVEL");
        boolean maxed = realLevel - baseLevel == 5;
        int minLevel = hammerCompound.getInteger("MIN_LEVELS_TO_UPGRADE");
        int maxLevel = hammerCompound.getInteger("MAX_LEVELS_TO_UPGRADE");
        int remainingLevels = maxLevel - (realLevel - baseLevel);
        ItemStack newBook = book.clone();
        ItemMeta newMeta = newBook.getItemMeta();
        int randomP = random(1, 100);

        if(randomP >= 75 && maxLevel == 5) {
           specialGuaranteed = true;
        }
        NBTItem newNBT = new NBTItem(newBook);
        if(NBTUtil.hasCustomKey(newNBT, "SPECIAL_ENCH_ID")) specialGuaranteed = false;
        SpecialEnchant enchant = null;
        if(specialGuaranteed){
            SpecialEnchant[] enchants = SpecialEnchant.values();
            enchant = enchants[random(1, enchants.length)];
            newBook = enchant.applyTo(newBook);
            newNBT = new NBTItem(newBook);
            if(maxed){
                p.sendMessage(ChatColor.LIGHT_PURPLE + "-----------" + ChatColor.YELLOW + "☆" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "SPECIAL ENCHANT!" + ChatColor.YELLOW + "☆" + ChatColor.LIGHT_PURPLE + "------------" + "\n\n" + ChatColor.GRAY + "Name: " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + enchant.name);
                p.sendMessage(ChatColor.LIGHT_PURPLE + "------------------------------------------");
                p.closeInventory();
                return newBook;
            }else{
                newMeta = newBook.getItemMeta();
            }
        }
        if(randomP <= hammerCompound.getInteger("DOWNGRADE_CHANCE") && realLevel != 1) {

            newNBT.getCompound("CustomAttributes").setInteger("REAL_LEVEL", (newNBT.getCompound("CustomAttributes").getInteger("REAL_LEVEL") - 1));
            newMeta.setDisplayName(newMeta.getDisplayName().replaceAll("" + realLevel,  "" + (realLevel - 1)));
            newNBT.getItem().setItemMeta(newMeta);
            p.closeInventory();
            p.sendMessage(ChatColor.RED + "-------------↓ " + ChatColor.BOLD + "BOOK DOWNGRADED..." + ChatColor.RED + " ↓-------------\n\n" + ChatColor.DARK_GRAY + "• Input: " + hammer.getItemMeta().getDisplayName() + ChatColor.GRAY + " + " + book.getItemMeta().getDisplayName() + "\n" + ChatColor.DARK_GRAY + "• Output: " + newNBT.getItem().getItemMeta().getDisplayName() + "\n");
            if(specialGuaranteed) p.sendMessage(ChatColor.YELLOW +  "☆" + ChatColor.LIGHT_PURPLE + " Special Enchant Acquired: " + ChatColor.BOLD + enchant.name + "!");
            p.sendMessage(ChatColor.RED + "-----------------------------------------------");
            Plugin pl = Bukkit.getPluginManager().getPlugin("Enchant_Refinery");
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(0, Note.Tone.F));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.F));
                }
            }.runTaskLater(pl, 5);
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(0, Note.Tone.D));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.C));

                }
            }.runTaskLater(pl, 9);
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(0, Note.Tone.D));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.C));

                }
            }.runTaskLater(pl, 7);


            return newNBT.getItem();
        }else {
            int newLevel = 1;
            if(minLevel >= remainingLevels){
                newLevel = realLevel + remainingLevels;
            }else{
                newLevel = realLevel + random(minLevel, remainingLevels);
            }
            newMeta.setDisplayName(newMeta.getDisplayName().replaceAll("" + realLevel, "" + newLevel));
            String previousName = book.getItemMeta().getDisplayName();
            book = newNBT.getItem();
            book.setItemMeta(newMeta);
            newNBT = new NBTItem(book);
            newNBT.getCompound("CustomAttributes").setInteger("REAL_LEVEL", newLevel);
            p.sendMessage(ChatColor.GREEN + "-------------↑ " + ChatColor.BOLD + "BOOK UPGRADED!" + ChatColor.GREEN + " ↑-------------\n\n" + ChatColor.DARK_GRAY + "• Input: " + hammer.getItemMeta().getDisplayName() + ChatColor.GRAY + " + " + previousName + "\n" + ChatColor.DARK_GRAY + "• Output: " + newNBT.getItem().getItemMeta().getDisplayName());
            if(specialGuaranteed) p.sendMessage(ChatColor.YELLOW +  "☆" + ChatColor.LIGHT_PURPLE + " Special Enchant Acquired: " + ChatColor.BOLD + enchant.name + "!");
            p.sendMessage(ChatColor.GREEN + "--------------------------------------------");
            p.closeInventory();
            
            return newNBT.getItem();
        }
    }

    public static int specialScan(Player player, String enchId){
        PlayerInventory inv = player.getInventory();
        ItemStack[] toScan = new ItemStack[]{inv.getItemInHand(), inv.getHelmet(), inv.getChestplate(), inv.getLeggings(), inv.getBoots()};
        int occurrences = 0;
        for(ItemStack item : toScan){
            if(item != null && item.getType() != Material.AIR){
                NBTCompound customAttributes = new NBTItem(item).getCompound("CustomAttributes");
                if(customAttributes != null){
                    NBTCompound enchants = customAttributes.getCompound("enchantments");
                    if(enchants != null){
                        List<String> special = enchants.getStringList("Special");
                        if(special != null){
                            if(special.contains(enchId)) occurrences++;
                        }
                    }
                }
            }
        }
        return occurrences;
    }
}
