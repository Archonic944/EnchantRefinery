package me.ench.main;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Objects;

import static me.ench.main.RefineryUtils.isBook;
import static me.ench.main.RefineryUtils.isHammer;
//!!MISTAKE MADE WITH ableToRefine, max level ISN'T ALWAYS 8, PLEASE DON'T USE ABLETOREFINE = 3.
public class RefineryInventory implements Listener {
    public boolean specialGuaranteed = false;
    private Inventory refineryInventory;
    public int ableToRefine = 0;
    boolean hasHammer = false;
    boolean hasBook = false;
    public ItemStack hammer;
    public ItemStack book;
    ItemStack buttonItem;
    ItemMeta buttonMeta;
    ItemStack borderItem;
    ItemMeta bordermeta;
    NBTItem hammerNBT;
    NBTCompound hammerCompound;
    NBTItem bookNBT;
    NBTCompound bookCompound;
    Plugin plugin = Bukkit.getPluginManager().getPlugin("Enchant Refinery");


    @EventHandler
    public void invClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory i = event.getClickedInventory();
        if(RefineryUtils.isRefineryOpen.containsKey(player.getUniqueId().toString())) {
            if (RefineryUtils.isRefineryOpen.get(player.getUniqueId().toString()).equals(Boolean.TRUE)) {
                ItemStack item = i.getItem(event.getSlot());
                if (event.getClick().equals(ClickType.DOUBLE_CLICK) || event.getClick().equals(ClickType.LEFT) || event.getClick().equals(ClickType.MIDDLE) || event.getClick().equals(ClickType.RIGHT)) {
                    if (Objects.requireNonNull(i.getTitle()).equalsIgnoreCase("Enchant Refinery")) {
                        if (item != null) {
                            if (!item.getType().equals(Material.AIR)) {
                                if (event.getSlot() == 12) {
                                    if (hasBook) {
                                        player.playNote(player.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.C));
                                    } else {
                                        player.playNote(player.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.A));
                                    }
                                    player.getInventory().addItem(item);
                                    hasHammer = false;
                                    hammer = new ItemStack(Material.AIR);
                                    ableToRefine = 0;
                                    event.setCancelled(true);
                                    refineryInventory = event.getClickedInventory();
                                    openRefineryInventory(player, true);

                                } else if (event.getSlot() == 14) {
                                    if (hasHammer) {
                                        player.playNote(player.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.C));
                                    } else {
                                        player.playNote(player.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.A));
                                    }
                                    player.getInventory().addItem(item);
                                    hasBook = false;
                                    book = new ItemStack(Material.AIR);
                                    ableToRefine = 0;
                                    event.setCancelled(true);
                                    refineryInventory = event.getClickedInventory();
                                    openRefineryInventory(player, true);
                                } else if (event.getSlot() == 22) {
                                    if (ableToRefine == 1) {
                                        event.setCancelled(true);
                                        i.setItem(12, new ItemStack(Material.AIR));
                                        i.setItem(14, new ItemStack(Material.AIR));

                                        refineryInventory.setItem(12, new ItemStack(Material.AIR));
                                        refineryInventory.setItem(14, new ItemStack(Material.AIR));
                                        if(player.getItemInHand() != null) player.getInventory().addItem(player.getItemInHand());
                                        player.setItemInHand(RefineryUtils.refine(book, hammer, specialGuaranteed, player));
                                        book = new ItemStack(Material.AIR);
                                        hammer = new ItemStack(Material.AIR);
                                        hasBook = false;
                                        hasHammer = false;
                                        ableToRefine = 0;
                                        specialGuaranteed = false;
                                    } else {
                                        player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.flat(1, Note.Tone.G));
                                        event.setCancelled(true);
                                        return;
                                    }
                                } else {
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                event.setCancelled(true);
                                return;
                            }
                        } else {
                            event.setCancelled(true);
                            return;
                        }

                    } else {
                        if (!isHammer(item)) {

                            if (isBook(item)) {
                                if (!hasBook) {
                                    if (!hasHammer) {
                                        player.playNote(player.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.D));
                                    } else {
                                        player.playNote(player.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.E));
                                    }
                                    book = item;
                                    hasBook = true;
                                    event.setCancelled(true);
                                    i.setItem(event.getSlot(), new ItemStack(Material.AIR));
                                } else {
                                    event.setCancelled(true);
                                    return;
                                }
                            } else {
                                event.setCancelled(true);
                                return;
                            }
                        } else {
                            if (!hasHammer) {
                                if (!hasBook) {
                                    player.playNote(player.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.D));
                                } else {
                                    player.playNote(player.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.E));
                                }
                                hasHammer = true;
                                hammer = item;
                                event.setCancelled(true);
                                i.setItem(event.getSlot(), new ItemStack(Material.AIR));
                            } else {
                                event.setCancelled(true);
                                return;
                            }

                        }

                        refineryInventory = player.getOpenInventory().getTopInventory();
                        if (hasBook && hasHammer) {
                            int realLevel = new NBTItem(book).getCompound("CustomAttributes").getInteger("REAL_LEVEL");
                            int baseLevel = (new NBTItem(book).getCompound("CustomAttributes").getInteger("BASE_LEVEL"));

                            if (new NBTItem(book).getCompound("CustomAttributes").getInteger("REAL_LEVEL") <= new NBTItem(book).getCompound("CustomAttributes").getInteger("BASE_LEVEL")) {
                                ableToRefine = 1;
                            } else if (new NBTItem(hammer).getCompound("CustomAttributes").getInteger("MAX_LEVELS_TO_UPGRADE") == 5 && realLevel - baseLevel == 5 && new NBTItem(book).getCompound("CustomAttributes").getCompound("Special") == null) {
                                ableToRefine = 1;
                                specialGuaranteed = true;
                            } else if (new NBTItem(hammer).getCompound("CustomAttributes").getInteger("MAX_LEVELS_TO_UPGRADE") > (new NBTItem(book).getCompound("CustomAttributes").getInteger("REAL_LEVEL") - new NBTItem(book).getCompound("CustomAttributes").getInteger("BASE_LEVEL"))) {
                                ableToRefine = 1;
                            } else {
                                if (new NBTItem(book).getCompound("CustomAttributes").getCompound("Special") != null && realLevel - baseLevel == 5) {
                                    ableToRefine = 4;
                                } else if (new NBTItem(hammer).getCompound("CustomAttributes").getInteger("MAX_LEVELS_TO_UPGRADE") <= (realLevel - baseLevel)) {
                                    ableToRefine = 2;
                                }

                            }

                        }
                        openRefineryInventory(player, true);

                    }


                } else {
                    event.setCancelled(true);
                    return;
                }

            } else {
                return;
            }
        }

    }





    public void openRefineryInventory(Player p, boolean resetInventory) {
        RefineryUtils.isRefineryOpen.put(p.getUniqueId().toString(), true);
        if (ableToRefine == 0) {
            buttonItem = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
            buttonMeta = buttonItem.getItemMeta();
            buttonMeta.setDisplayName(ChatColor.RED + "Can't refine!");
            ArrayList<String> buttonLore = new ArrayList<String>();
            buttonLore.add(ChatColor.RED + "Please put your hammer to the slot on the left, and");
            buttonLore.add(ChatColor.RED + "your book that you would like to refine on the right.");
            buttonMeta.setLore(buttonLore);
            buttonItem.setItemMeta(buttonMeta);
            borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            bordermeta = borderItem.getItemMeta();
            bordermeta.setDisplayName(" ");
            borderItem.setItemMeta(bordermeta);
        }
        if (ableToRefine == 1) {
            hammerNBT = new NBTItem(hammer);
            hammerCompound = hammerNBT.getCompound("CustomAttributes");
            Integer downchance = hammerCompound.getInteger("DOWNGRADE_CHANCE");
            Integer upchance = 100 - downchance;
            bookNBT = new NBTItem(book);
            bookCompound = bookNBT.getCompound("CustomAttributes");
            Integer baselevel = bookCompound.getInteger("BASE_LEVEL");
            int maxlevel = hammerCompound.getInteger("MAX_LEVELS_TO_UPGRADE") + baselevel;
            buttonItem = new ItemStack(Material.STAINED_GLASS, 1, (short) 5);
            buttonMeta = buttonItem.getItemMeta();
            buttonMeta.setDisplayName(ChatColor.GREEN + "Click to refine!");
            ArrayList<String> buttonLore = new ArrayList<String>();

            buttonLore.add(ChatColor.YELLOW + "Details:");
            borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            bordermeta = borderItem.getItemMeta();
            bordermeta.setDisplayName(" ");
            borderItem.setItemMeta(bordermeta);

            if(specialGuaranteed) {
                buttonLore.add(ChatColor.LIGHT_PURPLE + "✨ Special enchant guaranteed, nothing else!");
                buttonMeta.setLore(buttonLore);
                buttonItem.setItemMeta(buttonMeta);
            }else {
                buttonLore.add(ChatColor.YELLOW + "Chance to be downgraded: " + ChatColor.BLUE + downchance.toString() + "%");
                buttonLore.add(ChatColor.YELLOW + "Chance to be upgraded: " + ChatColor.BLUE + upchance + "%");
                buttonLore.add(ChatColor.YELLOW + "Max level to go up to: " + ChatColor.BLUE + maxlevel);
                buttonLore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Number of levels to go up or down is determined randomly and based on the hammer.");
                buttonMeta.setLore(buttonLore);
                buttonItem.setItemMeta(buttonMeta);


            }
        }
        if (ableToRefine == 2) {
            buttonItem = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
            buttonMeta = buttonItem.getItemMeta();
            buttonMeta.setDisplayName(ChatColor.RED + "Use a better hammer!");
            ArrayList<String> buttonLore = new ArrayList<String>();
            buttonLore.add(ChatColor.RED + "It appears that your book has reached the max level for your hammer.");
            buttonLore.add(ChatColor.RED + "Please try again with a better hammer.");
            buttonMeta.setLore(buttonLore);
            buttonItem.setItemMeta(buttonMeta);

            borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            bordermeta = borderItem.getItemMeta();
            bordermeta.setDisplayName(" ");
            borderItem.setItemMeta(bordermeta);
        }
        if (ableToRefine == 3) {
            buttonItem = new ItemStack(Material.STAINED_GLASS, 1, (short) 5);
            buttonMeta = buttonItem.getItemMeta();
            buttonMeta.setDisplayName(ChatColor.YELLOW + "Maxed!");
            ArrayList<String> buttonLore = new ArrayList<String>();
            buttonLore.add(ChatColor.YELLOW + "This book is the max level (8)! If you would like to ");
            buttonLore.add(ChatColor.YELLOW + "get a special enchant, please use a special hammer.");
            buttonMeta.setLore(buttonLore);

            borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
            bordermeta = borderItem.getItemMeta();
            bordermeta.setDisplayName(" ");
            borderItem.setItemMeta(bordermeta);
        }
        if (ableToRefine == 4) {
            buttonItem = new ItemStack(Material.STAINED_GLASS, 1, (short) 4);
            buttonMeta = buttonItem.getItemMeta();
            buttonMeta.setDisplayName(ChatColor.YELLOW + "Why are you here?!");
            ArrayList<String> buttonLore = new ArrayList<String>();
            buttonLore.add(ChatColor.YELLOW + "This book is already maxed, WITH a special enchant!");
            buttonLore.add(ChatColor.YELLOW + "If you really want to use this hammer, go get a different book!");
            buttonMeta.setLore(buttonLore);
            buttonItem.setItemMeta(buttonMeta);

            borderItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
            bordermeta = borderItem.getItemMeta();
            bordermeta.setDisplayName(" ");
            borderItem.setItemMeta(bordermeta);
        }


        if(refineryInventory == null){
            resetInventory = false;
        }


        if(!resetInventory){
            refineryInventory = plugin.getServer().createInventory(p, 36, "Enchant Refinery");
        }

        for (int inc = 0; inc<36; inc++) {
            ItemStack empty = new ItemStack( Material.STAINED_GLASS_PANE, 1, (short) 7);
            ItemMeta em = empty.getItemMeta();
            em.setDisplayName(" ");
            empty.setItemMeta(em);
            refineryInventory.setItem(inc, empty);
        }
        if(hasHammer) {
            refineryInventory.setItem(12, hammer);
        }else {
            refineryInventory.setItem(12, new ItemStack(Material.AIR));
        }
        if (hasBook) {
            refineryInventory.setItem(14, book);
        }else {
            refineryInventory.setItem(14, new ItemStack(Material.AIR));
        }


        refineryInventory.setItem(22, buttonItem);

        for (int in = 0;in<36;in+=9){
            refineryInventory.setItem(in, borderItem);
        }
        for (int in = 8; in<36;in+=9) {
            refineryInventory.setItem(in, borderItem);
        }
        refineryInventory.setItem(26, borderItem);

        if(!resetInventory) {
            p.openInventory(refineryInventory);
        }
        RefineryUtils.isRefineryOpen.put(p.getUniqueId().toString(), true);
    }


}
