package de.allwetterjacke.destinyplugin.listeners;

import de.allwetterjacke.destinyplugin.DestinyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import utils.ConfigManager;

import java.io.File;
import java.io.IOException;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void OnInvClose (InventoryCloseEvent event) {
        DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        InventoryView inventoryView = event.getView();
        ItemStack[] items = inventory.getContents();

        if (!inventoryView.getTitle().equals(ChatColor.GRAY + "Sell your items")) { return; }
        plugin.getLogger().info("0");

        for (int i = 0; i < 50; i++) {
            if (items[i].getType() == Material.AIR) { continue; }

            ItemStack itemSold = items[i];
            Double price = null;

            ConfigManager config = new ConfigManager(plugin, "economy" + File.separator + player.getUniqueId() + ".yml");
            ConfigManager itemWorthConfig = new ConfigManager(plugin, "economy" + File.separator + "itemWorth.yml");

            if (!itemWorthConfig.getFile().exists()) {
                player.sendMessage(ChatColor.RED + "No items can be sold!");
                try {
                    itemWorthConfig.getConfig().save(itemWorthConfig.getFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            Inventory inv = player.getInventory();

            for (int u = 0; u < 1373; u++) {
                plugin.getLogger().info("1");
                if (itemWorthConfig.getConfig().getString("item." + u + ".material") == null) {
                    player.sendMessage(ChatColor.RED + "Item cannot be sold!");
                    plugin.getLogger().info("2");
                    return;
                }
                Material material = Material.matchMaterial(itemWorthConfig.getConfig().getString("item." + u + ".material"));
                price = itemWorthConfig.getConfig().getDouble("item." + u + ".price");
                Double money = config.getConfig().getDouble("money");

                if (material == null) {
                    plugin.getLogger().info("3");
                    player.sendMessage(ChatColor.RED + "Item cannot be sold!");
                    return;
                }

                if (material == itemSold.getType()) {
                    plugin.getLogger().info("3");
                    inv.removeItem(itemSold);

                    if (money == null) {
                        money = price * itemSold.getAmount();
                    } else {
                        money += price * itemSold.getAmount();
                    }
                    config.getConfig().set("money", money);
                    config.save();
                    player.sendTitle(ChatColor.GREEN + "+" + price * itemSold.getAmount() + "€", "");
                    return;
                }
            }
        }
    }
}
