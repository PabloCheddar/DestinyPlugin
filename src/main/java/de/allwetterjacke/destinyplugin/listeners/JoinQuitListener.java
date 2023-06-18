package de.allwetterjacke.destinyplugin.listeners;

import de.allwetterjacke.destinyplugin.DestinyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import utils.ConfigManager;
import utils.ScoreHelper;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class JoinQuitListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);
        if (!player.hasPlayedBefore()) {
            event.setJoinMessage(ChatColor.GOLD + player.getName() + " has joined the server for the first time!");
            ArrayList<ItemStack> chainmailArmor = new ArrayList<ItemStack>();
            chainmailArmor.add(new ItemStack(Material.CHAINMAIL_BOOTS));
            chainmailArmor.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
            chainmailArmor.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
            chainmailArmor.add(new ItemStack(Material.CHAINMAIL_HELMET));

            for (ItemStack armorPart : chainmailArmor) {
                armorPart.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                player.getInventory().addItem(armorPart);
            }

            ArrayList<ItemStack> otherStarterItems = new ArrayList<ItemStack>();
            otherStarterItems.add(new ItemStack(Material.IRON_SWORD));
            otherStarterItems.add(new ItemStack(Material.STONE_AXE));
            otherStarterItems.add(new ItemStack(Material.COOKED_BEEF, 24));

            for (ItemStack itemStack : otherStarterItems) {
                player.getInventory().addItem(itemStack);
            }
        } else {
            event.setJoinMessage(ChatColor.GOLD + "Welcome back, " + ChatColor.DARK_RED +  player.getName() + ChatColor.GOLD + "!");
        }

        /*
        * Saving location of players with bounties to config
        * */

        ConfigManager config = new ConfigManager(plugin, "bounty" + File.separator + "bounty.yml");
        if (config.getConfig().getString(String.valueOf(player.getUniqueId())) != null) {
            config.getConfig().set(player.getUniqueId() + ".location", player.getLocation());
            config.save();
            player.sendTitle(ChatColor.RED + "You have a bounty placed on you!", ChatColor.RED + "Your current location is public.");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
        }

        ConfigManager moneyConfig = new ConfigManager(plugin, "economy" + File.separator + player.getUniqueId() + ".yml");
        Double money = moneyConfig.getConfig().getDouble("money");

        ScoreHelper helper = ScoreHelper.createScore(player);
        helper.setTitle(player.getName());
        helper.setSlot(1, ChatColor.AQUA + "⚔ Kills: " + player.getStatistic(Statistic.PLAYER_KILLS));
        helper.setSlot(2, ChatColor.AQUA + "☠ Deaths: " + player.getStatistic(Statistic.DEATHS));
        helper.setSlot(3, ChatColor.AQUA + "€ Money: " + new BigDecimal(money).toPlainString());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(ChatColor.AQUA + "Bye bye, " + player.getName() + "!");

        if(ScoreHelper.hasScore(player)) {
            ScoreHelper.removeScore(player);
        }
    }
}
