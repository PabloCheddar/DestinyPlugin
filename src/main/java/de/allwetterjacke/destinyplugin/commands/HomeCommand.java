package de.allwetterjacke.destinyplugin.commands;

import de.allwetterjacke.destinyplugin.DestinyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import utils.ConfigManager;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Supplier;

public class HomeCommand implements Listener, CommandExecutor {

    public HomeCommand(DestinyPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Home management")) return;
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        ItemMeta itemMeta = clickedItem.getItemMeta();
        String onlyNum = null;
        for (String string : itemMeta.getLore()) {
            onlyNum = string.replaceAll("[^0-9]", "");
        }
        DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);
        ConfigManager config = new ConfigManager(plugin, "homes" + File.separator + event.getWhoClicked().getUniqueId() + ".yml");

        switch (event.getCurrentItem().getType()) {
            case RED_BED:
                Location homeloc = config.getConfig().getLocation("home" + onlyNum);
                if (homeloc == null) {
                    event.getWhoClicked().sendMessage(ChatColor.RED + "Home N° " + onlyNum + " hasn't been set yet! Cancelling teleport...");
                    event.getWhoClicked().closeInventory();
                    break;
                }

                event.getWhoClicked().teleport(homeloc);
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Welcome to home N° " + onlyNum + "!");
                break;
            case BLACK_STAINED_GLASS_PANE:
                Location location = event.getWhoClicked().getLocation();
                config.getConfig().set("home" + onlyNum, location);
                config.save();
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Successfully set Home N° " + onlyNum + "!");
                event.getWhoClicked().closeInventory();
                break;
            case GREEN_STAINED_GLASS_PANE:
                Location location1 = event.getWhoClicked().getLocation();
                config.getConfig().set("home" + onlyNum, location1);
                config.save();
                event.getWhoClicked().sendMessage(ChatColor.GREEN + "Successfully set Home N° " + onlyNum + "!");
                event.getWhoClicked().closeInventory();
                break;
        }
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        /*DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);
        ConfigManager config = new ConfigManager(plugin, "homes" + File.separator + ((Player) commandSender).getUniqueId() + ".yml");
        Location homeLoc = config.getConfig().getLocation("homeOne");

        if (homeLoc == null) {
            commandSender.sendMessage(ChatColor.RED + "You must have a home set!");
            return false;
        }

        ((Player) commandSender).teleport(homeLoc);
        commandSender.sendMessage(ChatColor.GREEN + "Welcome home!");*/

        Inventory homeInv = Bukkit.createInventory(null, 4*9, "Home management");
        DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);
        ConfigManager config = new ConfigManager(plugin, "homes" + File.separator + ((Player) commandSender).getUniqueId() + ".yml");

        int x = 0;
        int y = 0;
        for (int i = 0; i < 36; i++) {
            if ((i > 8 && i < 18) || (i > 26 && i < 36)) {
                x++;
                ItemStack glassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                ItemStack existingPane = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GREEN + "Set home N° " + x);
                Location homeLoc = config.getConfig().getLocation("home" + x);
                if (homeLoc == null) {
                    ItemMeta meta = glassPane.getItemMeta();
                    meta.setDisplayName("Create home");
                    meta.setLore(lore);
                    glassPane.setItemMeta(meta);
                    homeInv.setItem(i, glassPane);
                } else {
                    ItemMeta meta = existingPane.getItemMeta();
                    meta.setDisplayName("Overwrite home");
                    meta.setLore(lore);
                    existingPane.setItemMeta(meta);
                    homeInv.setItem(i, existingPane);
                }

            } else {
                y++;
                Location homeLoc = config.getConfig().getLocation("home" + x);
                ItemStack redBed = new ItemStack(Material.RED_BED);
                ArrayList<String> lore = new ArrayList<>();
                if (homeLoc == null) {
                    lore.add(ChatColor.GREEN + "Teleport to home N° " + y);
                    ItemMeta meta = redBed.getItemMeta();
                    meta.setDisplayName("Teleport to home N° " + y);
                    meta.setLore(lore);
                    redBed.setItemMeta(meta);
                } else {
                    lore.add(ChatColor.GREEN + "Teleport to home N° " + y);
                    ItemMeta meta = redBed.getItemMeta();
                    meta.setDisplayName("Teleport to home N° " + y);
                    meta.setLore(lore);
                    redBed.setItemMeta(meta);
                }
                homeInv.setItem(i, redBed);
            }
        }

        ((Player) commandSender).openInventory(homeInv);


        return false;
    }
}
