package me.ench.main;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

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
        if(new NBTItem(item).getCompound("CustomAttributes") == null) {
            return false;
        }else return new NBTItem(item).getCompound("CustomAttributes").getInteger("BASE_LEVEL") != null;
    }

    public static int random(int min, int max) {
        return (int)(Math.random()*((max-min)+1))+min;
    }


    public static void addSpecialEnch(NBTItem book) {
        int i = random(1, 3);
        if(isBook(book.getItem())) {
            switch (i) {
                case 1:
                    book.getCompound("CustomAttributes").addCompound("Special").setString("ENCH_ID", "SELF_DESTRUCT");
                    book.getCompound("CustomAttributes").getCompound("Special").setString("ENCH_NAME", "Self Destruct");
                    ItemMeta boMeta = book.getItem().getItemMeta();
                    ArrayList<String> specialLore = new ArrayList<>();
                    specialLore.add("");
                    specialLore.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Self Destruct");
                    specialLore.add(ChatColor.LIGHT_PURPLE + "If you are 5hp or under, you can hold down right click");
                    specialLore.add(ChatColor.LIGHT_PURPLE + "to explode, which will kill you, and will deal 10hp of");
                    specialLore.add(ChatColor.LIGHT_PURPLE + "damage (armor ignored) to anyone within 12 blocks.");
                    ArrayList<String> bookLore = (ArrayList<String>) book.getItem().getItemMeta().getLore();
                    bookLore.addAll(specialLore);
                    boMeta.setLore(bookLore);
                    book.getItem().setItemMeta(boMeta);
                    break;




                case 2:
                    book.getCompound("CustomAttributes").addCompound("Special").setString("ENCH_ID", "GRAND_ESCAPE");
                    book.getCompound("CustomAttributes").getCompound("Special").setString("ENCH_NAME", "Grand Escape");
                    ItemMeta bmeta = book.getItem().getItemMeta();
                    ArrayList<String> specialL = new ArrayList<>();
                    specialL.add("");
                    specialL.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Grand Escape");
                    specialL.add(ChatColor.LIGHT_PURPLE + "Crouch, jump, and right click at the same time to render");
                    specialL.add(ChatColor.LIGHT_PURPLE + "yourself invisible until you attack or get attacked. Used once per life.");
                    ArrayList<String> bookL = (ArrayList<String>) book.getItem().getItemMeta().getLore();
                    bookL.addAll(specialL);
                    bmeta.setLore(bookL);
                    book.getItem().setItemMeta(bmeta);
                    break;




                case 3:

                    ArrayList<String> speciallor = new ArrayList<>();
                    book.getCompound("CustomAttributes").addCompound("Special").setString("ENCH_ID", "SPIKED");
                    book.getCompound("CustomAttributes").getCompound("Special").setString("ENCH_NAME", "Spiked");
                    speciallor.add("");
                    speciallor.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Spiked");
                    speciallor.add(ChatColor.LIGHT_PURPLE + "Crouch repeatedly and quickly to propel yourself in the");
                    speciallor.add(ChatColor.LIGHT_PURPLE + "direction you are looking. Can be used once every 17 seconds.");
                    ArrayList<String> booklor = (ArrayList<String>) book.getItem().getItemMeta().getLore();
                    ItemMeta meta = book.getItem().getItemMeta();
                    booklor.addAll(speciallor);
                    meta.setLore(booklor);
                    book.getItem().setItemMeta(meta);
                    break;



            }
        }
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
        NBTItem newNBT = new NBTItem(newBook);

        if(randomP >= 75 && hammer.getType().equals(Material.GOLD_HOE)) {
           specialGuaranteed = true;
        }
        if(newNBT.getCompound("CustomAttributes").getCompound("Special") != null) specialGuaranteed = false;

        if(specialGuaranteed){
            addSpecialEnch(newNBT);
            if(maxed){
                p.sendMessage(ChatColor.LIGHT_PURPLE + "-----------" + ChatColor.YELLOW + "☆" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "SPECIAL ENCHANT!" + ChatColor.YELLOW + "☆" + ChatColor.LIGHT_PURPLE + "------------" + "\n\n" + ChatColor.GRAY + "Name: " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + newNBT.getCompound("CustomAttributes").getCompound("Special").getString("ENCH_NAME"));
                p.sendMessage(ChatColor.LIGHT_PURPLE + "------------------------------------------");
                p.closeInventory();
                return newNBT.getItem();
            }
            else newMeta = newNBT.getItem().getItemMeta();
        }
        if(randomP <= hammerCompound.getInteger("DOWNGRADE_CHANCE") && realLevel != 1) {

            newNBT.getCompound("CustomAttributes").setInteger("REAL_LEVEL", (newNBT.getCompound("CustomAttributes").getInteger("REAL_LEVEL") - 1));
            newMeta.setDisplayName(newMeta.getDisplayName().replaceAll("" + realLevel,  "" + (realLevel - 1)));
            newNBT.getItem().setItemMeta(newMeta);
            p.closeInventory();
            p.sendMessage(ChatColor.RED + "-------------↓ " + ChatColor.BOLD + "BOOK DOWNGRADED..." + ChatColor.RED + " ↓-------------\n\n" + ChatColor.DARK_GRAY + "• Input: " + hammer.getItemMeta().getDisplayName() + ChatColor.GRAY + " + " + book.getItemMeta().getDisplayName() + "\n" + ChatColor.DARK_GRAY + "• Output: " + newNBT.getItem().getItemMeta().getDisplayName());
            if(specialGuaranteed) p.sendMessage(ChatColor.YELLOW +  "☆" + ChatColor.LIGHT_PURPLE + " Special Enchant Acquired: " + ChatColor.BOLD + newNBT.getCompound("CustomAttributes").getCompound("Special").getString("ENCH_NAME") + "!");
            p.sendMessage(ChatColor.RED + "-----------------------------------------------");

            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.A));
                }
            };
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(0, Note.Tone.F));
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("Enchant_Refinery"), 5);
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.F));
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("Enchant_Refinery"), 5);
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(0, Note.Tone.D));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.C));

                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("Enchant_Refinery"), 9);
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(0, Note.Tone.D));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.C));

                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("Enchant_Refinery"), 7);


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
            if(specialGuaranteed) p.sendMessage(ChatColor.YELLOW +  "☆" + ChatColor.LIGHT_PURPLE + " Special Enchant Acquired: " + ChatColor.BOLD + newNBT.getCompound("CustomAttributes").getCompound("Special").getString("ENCH_NAME") + "!");
            p.sendMessage(ChatColor.GREEN + "--------------------------------------------");

            p.closeInventory();
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(0, Note.Tone.D));
                }
            };
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.F));
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("Enchant_Refinery"), 5);
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.G));
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("Enchant_Refinery"), 5);
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.A));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.D));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("Enchant_Refinery"), 9);
            new BukkitRunnable(){
                @Override
                public void run() {
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.A));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.D));
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("Enchant_Refinery"), 7);

            return newNBT.getItem();

        }
    }

    public static boolean specialScan(Player player, String enchId){
        List<ItemStack> toScan = Arrays.asList(player.getInventory().getArmorContents());
        toScan.add(player.getItemInHand());
        for(ItemStack item : toScan){
            try{
                NBTCompound customAttributes = new NBTItem(item).getCompound("CustomAttributes");
                if(customAttributes.getCompound("Special").getBoolean(enchId)) return true;
            }catch(NullPointerException ignored){}
        }
        return false;
    }
}
