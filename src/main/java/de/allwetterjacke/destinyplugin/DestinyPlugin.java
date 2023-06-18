package de.allwetterjacke.destinyplugin;

import de.allwetterjacke.destinyplugin.commands.*;
import de.allwetterjacke.destinyplugin.listeners.DeathListener;
import de.allwetterjacke.destinyplugin.listeners.InventoryCloseListener;
import de.allwetterjacke.destinyplugin.listeners.InventoryListener;
import de.allwetterjacke.destinyplugin.listeners.JoinQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.FileUtil;
import org.yaml.snakeyaml.Yaml;
import utils.ConfigManager;
import utils.ScoreHelper;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public final class DestinyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        final FileConfiguration defaultConfig = this.getConfig();
        defaultConfig.addDefault("locations.arena.x", null);
        defaultConfig.addDefault("locations.arena.y", null);
        defaultConfig.addDefault("locations.arena.z", null);
        defaultConfig.addDefault("shop.items.0.material", "DIAMOND");
        defaultConfig.addDefault("shop.items.0.price", 100);
        defaultConfig.addDefault("shop.items.1.material", "ELYTRA");
        defaultConfig.addDefault("shop.items.1.price", 100000);
        saveDefaultConfig();

        File homesFolder= new File(this.getDataFolder(), "homes");
        if (!homesFolder.exists()) {
            homesFolder.mkdirs();
        }


        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("shop").setExecutor(new Shop(this));
        getCommand("sell").setExecutor(new Sell());
        getCommand("bounty").setExecutor(new Bounty(this));
        getCommand("transfer").setExecutor(new Transfer());

        final PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new JoinQuitListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new DeathListener(), this);
        pluginManager.registerEvents(new InventoryCloseListener(), this);

        new BukkitRunnable() {

            @Override
            public void run() {

                for(Player player : Bukkit.getOnlinePlayers()) {
                    updateScoreboard(player);
                }

            }

        }.runTaskTimer(this, 20L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void updateScoreboard(Player player) {
        ConfigManager config = new ConfigManager(this, "economy" + File.separator + player.getUniqueId() + ".yml");
        Double money = config.getConfig().getDouble("money");

        if(ScoreHelper.hasScore(player)) {
            ScoreHelper helper = ScoreHelper.getByPlayer(player);
            helper.setSlot(1, ChatColor.AQUA + "⚔ Kills: " + player.getStatistic(Statistic.PLAYER_KILLS));
            helper.setSlot(2, ChatColor.AQUA + "☠ Deaths: " + player.getStatistic(Statistic.DEATHS));
            helper.setSlot(3, ChatColor.AQUA + "€ Money: " + new BigDecimal(money).toPlainString());
        }
    }
}
