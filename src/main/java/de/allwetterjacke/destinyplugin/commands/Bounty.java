package de.allwetterjacke.destinyplugin.commands;

import de.allwetterjacke.destinyplugin.DestinyPlugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import utils.ConfigManager;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class Bounty implements Listener, CommandExecutor {

    public Bounty (DestinyPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvClick (InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Available Bounties"))
        event.setCancelled(true);
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);
        ConfigManager bountyConfig = new ConfigManager(plugin, "bounty" + File.separator + "bounty.yml");
        ConfigManager creatorConfig = new ConfigManager(plugin, "economy" + File.separator + ((Player)commandSender).getUniqueId() + ".yml");
        Double creatorMoney = creatorConfig.getConfig().getDouble("money");

        if (strings.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /bounty set or get");
            return false;
        }

        switch (strings[0].toLowerCase()) {
            case "set":
                if (strings.length < 3) {
                    commandSender.sendMessage(ChatColor.RED + "Usage: /bounty set {playerName} {price}");
                    break;
                }
                String playerName = strings[1];
                String price = strings[2];
                Double priceNum = Double.parseDouble(price);
                Player player = plugin.getServer().getPlayer(playerName);
                OfflinePlayer offline;
                if (player == null) {
                    offline = Bukkit.getOfflinePlayer(playerName);
                    if (!offline.hasPlayedBefore()) {
                        commandSender.sendMessage(ChatColor.RED + "That player hasn't played before!");
                        break;
                    }
                }

                if (player == null || price == null || priceNum == 0) {
                    commandSender.sendMessage(ChatColor.RED + "Usage: /bounty set {playerName} {price}");
                    break;
                }

                if (creatorMoney == null) {
                    creatorMoney = priceNum - priceNum*2;
                } else if (creatorMoney >= priceNum) {
                    creatorMoney -= priceNum;
                } else {
                    commandSender.sendMessage(ChatColor.RED + "You don't have enough money to place this bounty!");
                    break;
                }

                creatorConfig.getConfig().set("money", creatorMoney);
                creatorConfig.save();

                if (bountyConfig.getConfig().getDouble(player.getUniqueId() + ".price") == 0.0) {
                    bountyConfig.getConfig().set(player.getUniqueId() + ".price", priceNum);
                    player.sendTitle(ChatColor.RED + "A bounty has been placed on you!", ChatColor.RED + "Your location will now be public each join.");
                } else {
                    Double prePrice = bountyConfig.getConfig().getDouble(player.getUniqueId() + ".price");
                    prePrice += priceNum;
                    bountyConfig.getConfig().set(player.getUniqueId() + ".price", prePrice);
                    player.sendTitle(ChatColor.RED + "Your bounty has been raised!", ChatColor.RED + "Current bounty: " + new BigDecimal(prePrice).toPlainString());
                }

                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
                bountyConfig.getConfig().set(player.getUniqueId() + ".location", player.getLocation());
                bountyConfig.save();


                commandSender.sendMessage(ChatColor.GREEN + "Successfully set bounty on " + ChatColor.WHITE + playerName + ChatColor.GREEN + "!");

                bountyConfig.getConfig().set(((Player) commandSender).getUniqueId().toString(), "");

                break;
            case "get":
                Inventory inv = Bukkit.createInventory(null, 5*9, "Available Bounties");

                for (String key : bountyConfig.getConfig().getKeys(false)) {
                    ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

                    OfflinePlayer skullOwner = plugin.getServer().getPlayer(UUID.fromString(key));
                    if (skullOwner == null) {
                        skullOwner = Bukkit.getOfflinePlayer(UUID.fromString(key));
                        if (skullOwner != null && !skullOwner.hasPlayedBefore()) {
                            continue;
                        }
                    }
                    plugin.getLogger().info(key);
                    if (skullOwner == null) continue;
                    plugin.getLogger().info(String.valueOf(skullOwner.getUniqueId()));
                    skullMeta.setOwningPlayer(skullOwner);
                    ArrayList<String> lore = new ArrayList<>();
                    Location loc = bountyConfig.getConfig().getLocation(skullOwner.getUniqueId() + ".location");
                    lore.add(ChatColor.GREEN + "Bounty: " + new BigDecimal(bountyConfig.getConfig().getDouble(skullOwner.getUniqueId() + ".price")).toPlainString());
                    if (bountyConfig.getConfig().getDouble(skullOwner.getUniqueId() + ".price") >= 10000) {
                        lore.add(ChatColor.GREEN + "Last seen at: " + Math.round(loc.getX()) + " " + Math.round(loc.getY()) + " " + Math.round(loc.getZ()));
                    }
                    skullMeta.setLore(lore);

                    skull.setItemMeta(skullMeta);
                    inv.addItem(skull);

                }

                ((Player) commandSender).openInventory(inv);
                break;
            default:
                commandSender.sendMessage(ChatColor.RED + "Usage: /bounty set or get");
                break;
        }

        return false;
    }
}
