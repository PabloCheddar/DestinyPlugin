package de.allwetterjacke.destinyplugin.commands;

import de.allwetterjacke.destinyplugin.DestinyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

public class Shop implements Listener, CommandExecutor {

    public Shop (DestinyPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnInventoryClick (InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Shop")) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);
        ConfigManager config = new ConfigManager(plugin, "economy" + File.separator + player.getUniqueId() + ".yml");

        ItemStack clickedItem = event.getCurrentItem();

        for (int i = 0; i < 17; i++) {
            String material = plugin.getConfig().getString("shop.items." + i + ".material");
            if (material == null) break;
            ItemStack item = new ItemStack(Material.matchMaterial(material));
            Material mat = item.getType();

            if (clickedItem.getType() == mat) {
                Double price = plugin.getConfig().getDouble("shop.items." + i + ".price");
                Double money = config.getConfig().getDouble("money");

                if (price > money) {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "You don't have enough money to buy this item!");
                    break;
                } else {
                    money = money - price;
                    config.getConfig().set("money", money);
                    player.getInventory().addItem(item);
                    config.save();
                    player.sendMessage(ChatColor.GREEN + "Successfully bought " + mat + "!");
                    break;
                }
            }
        }

    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Inventory shopInv = Bukkit.createInventory(null, 2*9, "Shop");
        DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);

        for (int i = 0; i < 17; i++) {
            String material = plugin.getConfig().getString("shop.items." + i + ".material");
            if (material == null) break;
            ItemStack item = new ItemStack(Material.matchMaterial(material));
            Double price = plugin.getConfig().getDouble("shop.items." + i + ".price");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "Price: " + price);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            shopInv.addItem(item);
        }

        ((Player) commandSender).openInventory(shopInv);

        return false;
    }
}
