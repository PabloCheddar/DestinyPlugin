package de.allwetterjacke.destinyplugin.listeners;

import de.allwetterjacke.destinyplugin.DestinyPlugin;
import de.allwetterjacke.destinyplugin.commands.HelpCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
import java.io.IOException;
import java.util.ArrayList;

public class InventoryListener implements Listener {
    @EventHandler
    public void OnInvClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory() == HelpCommand.helpInterface) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getType()) {
                case BLUE_DYE:
                    TextComponent message = new TextComponent("Click to become part of the Discord!");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/UaGnpDSbd8"));
                    message.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);
                    message.setUnderlined(true);
                    player.spigot().sendMessage(message);
                    player.closeInventory();
                    break;
                case ENDER_PEARL:
                    Inventory newInv =  Bukkit.createInventory(null, 3*9, "Choose a location to teleport to");

                    ItemStack spawnTeleport = new ItemStack(Material.GRASS_BLOCK);
                    ItemMeta spawnTeleportMeta = spawnTeleport.getItemMeta();
                    ArrayList<String> spawnLore = new ArrayList<String>();
                    spawnLore.add("Click to teleport to spawn!");
                    spawnTeleportMeta.setDisplayName("Teleport to Spawn");
                    spawnTeleportMeta.setLore(spawnLore);
                    spawnTeleport.setItemMeta(spawnTeleportMeta);
                    newInv.setItem(10, spawnTeleport);

                    ItemStack homeTeleport = new ItemStack(Material.RED_BED);
                    ItemMeta homeTeleportMeta = homeTeleport.getItemMeta();
                    ArrayList<String> homeLore = new ArrayList<String>();
                    homeLore.add("Click to teleport home!");
                    homeTeleportMeta.setDisplayName("Teleport Home");
                    homeTeleportMeta.setLore(homeLore);
                    homeTeleport.setItemMeta(homeTeleportMeta);
                    newInv.setItem(13, homeTeleport);

                    ItemStack arenaTeleport = new ItemStack(Material.DIAMOND_SWORD);
                    ItemMeta arenaTeleportMeta = arenaTeleport.getItemMeta();
                    ArrayList<String> arenaLore = new ArrayList<String>();
                    arenaLore.add("Click to teleport to the FFA arena!");
                    arenaTeleportMeta.setDisplayName("Teleport to the FFA arena");
                    arenaTeleportMeta.setLore(arenaLore);
                    arenaTeleport.setItemMeta(arenaTeleportMeta);
                    newInv.setItem(16, arenaTeleport);
                    event.getWhoClicked().closeInventory();
                    DestinyPlugin.getPlugin(DestinyPlugin.class).getServer().getScheduler().runTask(DestinyPlugin.getPlugin(DestinyPlugin.class), new Runnable() {
                        @Override
                        public void run() {
                           event.getWhoClicked().openInventory(newInv);
                        }
                    });
            }
        } else if (event.getView().getTitle().equals("Choose a location to teleport to")) {
            event.setCancelled(true);
            switch (event.getCurrentItem().getType()) {
                case GRASS_BLOCK:
                    Location spawnLoc = player.getWorld().getSpawnLocation();
                    player.teleport(spawnLoc);
                    player.closeInventory();
                    player.sendMessage("You've been teleported to the spawn!");
                    break;
                case RED_BED:
                    /*DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);
                    ConfigManager config = new ConfigManager(plugin, "homes" + File.separator + player.getUniqueId() + ".yml");
                    Location homeLoc = config.getConfig().getLocation("homeOne");

                    if (homeLoc == null) {
                        player.sendMessage(ChatColor.RED + "You must have a home set!");
                        player.closeInventory();
                        break;
                    }

                    player.teleport(homeLoc);
                    plugin.getLogger().info(event.getInventory().getType().toString());
                    player.sendMessage(ChatColor.GREEN + "Welcome home!");
                    plugin.getLogger().info(event.getInventory().getType().toString());*/
                    player.closeInventory();
                    player.performCommand("home");
                    break;
                case DIAMOND_SWORD:
                    FileConfiguration cfg = DestinyPlugin.getPlugin(DestinyPlugin.class).getConfig();
                    Double x = cfg.getDouble("locations.arena.x");
                    Double y = cfg.getDouble("locations.arena.y");
                    Double z = cfg.getDouble("locations.arena.z");
                    if (x == 0 && y == 0 && z == 0) {
                        player.sendMessage(ChatColor.RED + "The server didn't specify a location for the FFA Arena...");
                        player.closeInventory();
                        break;
                    }
                    Location arenaLoc = new Location(player.getWorld(), x, y, z);
                    player.teleport(arenaLoc);
                    player.closeInventory();
                    break;
            }
        }


    }
}
