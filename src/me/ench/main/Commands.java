package me.ench.main;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.ench.items.Hammers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player p = (Player) commandSender;
        if (command.getName().equalsIgnoreCase("testinv") && p.hasPermission("admin")) {
            if(!RefineryUtils.instance.containsKey( ((Player) commandSender).getUniqueId())){
                RefineryInventory instance = new RefineryInventory();
                RefineryUtils.instance.put(((Player) commandSender).getUniqueId(), instance);
            }

            RefineryUtils.instance.get(((Player) commandSender).getUniqueId()).openRefineryInventory((Player) commandSender, false);

        }
        if (command.getName().equalsIgnoreCase("givehammer") && p.hasPermission("admin")) {
            HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
            map.put("1", Hammers.getWoodHammer());
            map.put("2", Hammers.getStoneHammer());
            map.put("3", Hammers.getIronHammer());
            map.put("4", Hammers.getDiamondHammer());
            map.put("5", Hammers.getSpecialHammer());
            if (map.containsKey(args[0])) {
                p.getInventory().addItem(map.get(args[0]));
                return true;
            } else {
                p.sendMessage(ChatColor.RED + "Invalid Usage! Usage: /givehammer <hammer level>");
                return false;
            }
        }
        if (command.getName().equalsIgnoreCase("debugnbt") && p.hasPermission("admin")) {
            p.sendMessage(new NBTItem(p.getInventory().getItemInHand()).toString());
            return true;
        }

        if (command.getName().equalsIgnoreCase("givebookdummy") && p.hasPermission("admin")) {

            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta bookmeta = book.getItemMeta();
            bookmeta.setDisplayName(ChatColor.BLUE + "Dummy " + Integer.parseInt(args[1]));
            ArrayList<String> booklore = new ArrayList<>();
            booklore.add("just a dummy");
            bookmeta.setLore(booklore);
            book.setItemMeta(bookmeta);
            NBTItem bookNBT = new NBTItem(book);

            bookNBT.addCompound("CustomAttributes");
            NBTCompound bookCompound = bookNBT.getCompound("CustomAttributes");

            bookCompound.setString("ID", "ENCHANTED_BOOK");
            bookCompound.setString("ENCH_ID", "DUMMY");


            bookCompound.setInteger("BASE_LEVEL", Integer.parseInt(args[0]));
            bookCompound.setInteger("REAL_LEVEL", Integer.parseInt(args[1]));
            p.getInventory().addItem(bookNBT.getItem());

            return true;






        } else {
            return false;
        }

    }
}