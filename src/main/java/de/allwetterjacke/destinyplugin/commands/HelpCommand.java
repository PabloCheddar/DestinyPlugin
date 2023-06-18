package de.allwetterjacke.destinyplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class HelpCommand implements CommandExecutor {
    public static Inventory helpInterface = Bukkit.createInventory(null, 3*9, "Help Interface");
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player player = ((Player) commandSender).getPlayer();
        // 10, 13 & 16
        ItemStack rulesBook = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta rulesBookMeta = rulesBook.getItemMeta();
        ArrayList<String> rulesBookLore = new ArrayList<String>();
        rulesBookLore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "Der Chat wird nicht ausgenutzt und es wird nicht beleidigt");
        rulesBookLore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "Jegliche Form von Cheaten ist untersagt");
        rulesBookLore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "Griefing ist erlaubt");
        rulesBookLore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "Töten ist erlaubt");
        rulesBookLore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "Es dürfen Fraktionen gebildet werden");
        rulesBookLore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "Stehlen ist erlaubt");
        rulesBookLore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "AFK-Farmen sind erlaubt");
        rulesBookLore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "Jeder darf sich sein Baugelände mit Schildern markieren, darf jedoch nicht übertreiben");
        rulesBookLore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "Das Baugelände anderer Mitspieler wird respektiert und nicht für sich selber verwendet");
        rulesBookLore.add(ChatColor.GREEN + "●" + ChatColor.WHITE + "Einfache Mods, wie zum Beispiel Minimap, Waypoints, etc. sind erlaubt");
        rulesBookMeta.setLore(rulesBookLore);
        rulesBookMeta.setDisplayName("§aRules");
        rulesBook.setItemMeta(rulesBookMeta);

        helpInterface.setItem(10, rulesBook);

        ItemStack joinDiscord = new ItemStack(Material.BLUE_DYE);
        ItemMeta discordMeta = joinDiscord.getItemMeta();
        ArrayList<String> discordMetaLore = new ArrayList<String>();
        discordMetaLore.add(ChatColor.GREEN + "Click the link in chat to become part of the Discord!");
        discordMeta.setLore(discordMetaLore);
        discordMeta.setDisplayName("§aJoin the Discord");
        joinDiscord.setItemMeta(discordMeta);

        helpInterface.setItem(13, joinDiscord);

        ItemStack teleport = new ItemStack(Material.ENDER_PEARL);
        ItemMeta teleportMeta = teleport.getItemMeta();
        ArrayList<String> teleportMetaLore = new ArrayList<String>();
        teleportMetaLore.add(ChatColor.GREEN + "Teleport home, to spawn or to the FFA Arena");
        teleportMeta.setLore(teleportMetaLore);
        teleportMeta.setDisplayName("§aTeleport");
        teleport.setItemMeta(teleportMeta);

        helpInterface.setItem(16, teleport);

        player.openInventory(helpInterface);

        return false;
    }
}
