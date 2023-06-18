package de.allwetterjacke.destinyplugin.commands;

import de.allwetterjacke.destinyplugin.DestinyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import utils.ConfigManager;

import java.io.File;
import java.io.IOException;

public class Sell implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;

        Inventory sellInv = Bukkit.createInventory(null, 5*9, ChatColor.GRAY + "Sell your items");
        ((Player) commandSender).openInventory(sellInv);

        return false;
    }
}
