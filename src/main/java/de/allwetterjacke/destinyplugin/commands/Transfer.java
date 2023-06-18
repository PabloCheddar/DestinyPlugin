package de.allwetterjacke.destinyplugin.commands;

import de.allwetterjacke.destinyplugin.DestinyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import utils.ConfigManager;

import java.io.File;

public class Transfer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /transfer {playerName} {amount}");
            return false;
        }
        String playerName = strings[0];
        String price = strings[1];
        Double priceNum = Double.parseDouble(price);
        DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);

        Player player = plugin.getServer().getPlayer(playerName);
        if (player == null) {
            player = Bukkit.getOfflinePlayer(playerName).getPlayer();
            if (player != null && !player.hasPlayedBefore()) {
                commandSender.sendMessage(ChatColor.RED + "That player hasn't played before!");
                return false;
            }
        }

        if (playerName == null || priceNum == null) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /transfer {playerName} {amount}");
            return false;
        }

        ConfigManager creatorConfig = new ConfigManager(plugin, "economy" + File.separator + ((Player)commandSender).getUniqueId() + ".yml");
        ConfigManager targetConfig = new ConfigManager(plugin, "economy" + File.separator + player.getUniqueId() + ".yml");
        Double creatorMoney = creatorConfig.getConfig().getDouble("money");
        Double targetMoney = targetConfig.getConfig().getDouble("money");

        if (creatorMoney < priceNum) {
            commandSender.sendMessage(ChatColor.RED + "You don't have enough money!");
            return false;
        }
        creatorMoney -= priceNum;
        targetMoney += priceNum;

        creatorConfig.getConfig().set("money", creatorMoney);
        targetConfig.getConfig().set("money", targetMoney);

        creatorConfig.save();
        targetConfig.save();

        commandSender.sendMessage(ChatColor.GREEN + "You have successfully transfered " + price + "€ to " + player.getName() + ".");

        return false;
    }
}
