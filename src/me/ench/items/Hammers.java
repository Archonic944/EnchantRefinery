package me.ench.items;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Hammers {

    public static ItemStack getSpecialHammer() {
        ItemStack sphammeritem = new ItemStack(Material.GOLD_HOE);
        ItemMeta sphammermeta = sphammeritem.getItemMeta();
        sphammermeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Special Hammer");
        sphammermeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        sphammermeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        sphammermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> sphammerlore = new ArrayList<String>();
        sphammerlore.add("");
        sphammerlore.add(ChatColor.GOLD + "A special hammer obtained from XP milestones. It can be used");
        sphammerlore.add(ChatColor.GOLD + "at the " + ChatColor.RED + "Enchant Refinery " + ChatColor.GOLD + "to level up enchanted books!");
        sphammerlore.add(ChatColor.DARK_GRAY + "This hammer can grant your book a maximum of " + ChatColor.BLUE + "5 " + ChatColor.DARK_GRAY + "levels.");
        sphammermeta.setLore(sphammerlore);
        sphammeritem.setItemMeta(sphammermeta);
        NBTItem NBTItem = new NBTItem(sphammeritem);
        NBTCompound NBTComp = NBTItem.addCompound("CustomAttributes");
        NBTComp.setInteger("MIN_LEVELS_TO_UPGRADE", 4);
        NBTComp.setInteger("MAX_LEVELS_TO_UPGRADE", 5);
        NBTComp.setInteger("DOWNGRADE_CHANCE", 5);
        return NBTItem.getItem().clone();
    }


    public static ItemStack getDiamondHammer() {
        ItemStack dhammeritem = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta dhammermeta = dhammeritem.getItemMeta();
        dhammermeta.setDisplayName(ChatColor.AQUA + "Diamond Hammer");
        ArrayList<String> dhammerlore = new ArrayList<String>();
        dhammerlore.add("");
        dhammerlore.add(ChatColor.WHITE + "A hammer obtained from a rare drop from killing players, by completing quests, or from daily rewards.");
        dhammerlore.add(ChatColor.WHITE + "It can be used at the " + ChatColor.RED + "Enchant Refinery " + ChatColor.WHITE + "to level up enchanted books!");
        dhammerlore.add(ChatColor.DARK_GRAY + "This hammer can grant your book a maximum of " + ChatColor.BLUE + "4 " + ChatColor.DARK_GRAY + "levels.");
        dhammermeta.setLore(dhammerlore);
        dhammeritem.setItemMeta(dhammermeta);
        NBTItem NBTItem = new NBTItem(dhammeritem);
        NBTCompound NBTComp = NBTItem.addCompound("CustomAttributes");
        NBTComp.setInteger("MIN_LEVELS_TO_UPGRADE", 3);
        NBTComp.setInteger("MAX_LEVELS_TO_UPGRADE", 4);
        NBTComp.setInteger("DOWNGRADE_CHANCE", 10);
        return NBTItem.getItem().clone();
    }

    public static ItemStack getIronHammer() {
        ItemStack ihammeritem = new ItemStack(Material.IRON_HOE);
        ItemMeta ihammermeta = ihammeritem.getItemMeta();
        ihammermeta.setDisplayName(ChatColor.AQUA + "Iron Hammer");
        ArrayList<String> ihammerlore = new ArrayList<String>();
        ihammerlore.add("");
        ihammerlore.add(ChatColor.WHITE + "A hammer obtained as a rare drop from killing players, by completing quests, or from daily rewards.");
        ihammerlore.add(ChatColor.WHITE + "It can be used at the " + ChatColor.RED + "Enchant Refinery " + ChatColor.WHITE + "to level up enchanted books!");
        ihammerlore.add(ChatColor.DARK_GRAY + "This hammer can grant your book a maximum of " + ChatColor.BLUE + "3 " + ChatColor.DARK_GRAY + "levels.");
        ihammermeta.setLore(ihammerlore);
        ihammeritem.setItemMeta(ihammermeta);
        NBTItem NBTItem = new NBTItem(ihammeritem);
        NBTCompound NBTComp = NBTItem.addCompound("CustomAttributes");
        NBTComp.setInteger("MIN_LEVELS_TO_UPGRADE", 2);
        NBTComp.setInteger("MAX_LEVELS_TO_UPGRADE", 3);
        NBTComp.setInteger("DOWNGRADE_CHANCE", 15);
        return NBTItem.getItem().clone();
    }


    public static ItemStack getStoneHammer() {
        ItemStack sthammeritem = new ItemStack(Material.STONE_HOE);
        ItemMeta sthammermeta = sthammeritem.getItemMeta();
        sthammermeta.setDisplayName(ChatColor.AQUA + "Stone Hammer");
        ArrayList<String> sthammerlore = new ArrayList<String>();
        sthammerlore.add("");
        sthammerlore.add(ChatColor.WHITE + "A hammer obtained as a rare drop from killing players, by completing quests, or from daily rewards.");
        sthammerlore.add(ChatColor.WHITE + "It can be used at the " + ChatColor.RED + "Enchant Refinery " + ChatColor.WHITE + "to level up enchanted books!");
        sthammerlore.add(ChatColor.DARK_GRAY + "This hammer can grant your book a maximum of " + ChatColor.BLUE + "2 " + ChatColor.DARK_GRAY + "levels.");
        sthammermeta.setLore(sthammerlore);
        sthammeritem.setItemMeta(sthammermeta);
        NBTItem NBTItem = new NBTItem(sthammeritem);
        NBTCompound NBTComp = NBTItem.addCompound("CustomAttributes");
        NBTComp.setInteger("MIN_LEVELS_TO_UPGRADE", 1);
        NBTComp.setInteger("MAX_LEVELS_TO_UPGRADE", 2);
        NBTComp.setInteger("DOWNGRADE_CHANCE", 25);
        return NBTItem.getItem().clone();
    }
    

    public static ItemStack getWoodHammer() {
        ItemStack wdhammeritem = new ItemStack(Material.WOOD_HOE);
        ItemMeta wdhammermeta = wdhammeritem.getItemMeta();
        wdhammermeta.setDisplayName(ChatColor.AQUA + "Wood Hammer");
        ArrayList<String> wdhammerlore = new ArrayList<String>();
        wdhammerlore.add("");
        wdhammerlore.add(ChatColor.WHITE + "A hammer obtained as a rare drop from killing players, by completing quests, or from daily rewards.");
        wdhammerlore.add(ChatColor.WHITE + "It can be used at the " + ChatColor.RED + "Enchant Refinery " + ChatColor.WHITE + "to level up enchanted books!");
        wdhammerlore.add(ChatColor.DARK_GRAY + "This hammer can grant your book a maximum of " + ChatColor.BLUE + "1 " + ChatColor.DARK_GRAY + "levels.");
        wdhammermeta.setLore(wdhammerlore);
        wdhammeritem.setItemMeta(wdhammermeta);
        NBTItem NBTItem = new NBTItem(wdhammeritem);
        NBTCompound NBTComp = NBTItem.addCompound("CustomAttributes");
        NBTComp.setInteger("MIN_LEVELS_TO_UPGRADE", 1);
        NBTComp.setInteger("MAX_LEVELS_TO_UPGRADE", 1);
        NBTComp.setInteger("DOWNGRADE_CHANCE", 40);

        return NBTItem.getItem().clone();

    }




}
