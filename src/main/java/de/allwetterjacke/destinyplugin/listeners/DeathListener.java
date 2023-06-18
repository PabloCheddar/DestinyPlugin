package de.allwetterjacke.destinyplugin.listeners;

import de.allwetterjacke.destinyplugin.DestinyPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import utils.ConfigManager;
import utils.ScoreHelper;

import java.io.File;

public class DeathListener implements Listener {
    DestinyPlugin plugin = DestinyPlugin.getPlugin(DestinyPlugin.class);

    @EventHandler
    public void onKill (PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer == null || killer == player) return;

        ConfigManager config = new ConfigManager(plugin, "bounty" + File.separator + "bounty.yml");
        ConfigManager killerConfig = new ConfigManager(plugin, "economy" + File.separator + killer.getUniqueId() + ".yml");
        Double money = killerConfig.getConfig().getDouble("money");
        Double bounty = config.getConfig().getDouble(player.getUniqueId() + ".price");
        if (config.getConfig().getString(String.valueOf(player.getUniqueId())) != null) {
            if (money == null) {
                money = bounty;
            } else {
                money += bounty;
            }

            killerConfig.getConfig().set("money", money);
            killerConfig.save();

            config.getConfig().set(String.valueOf(player.getUniqueId()), null);
            config.save();
        }

        // Scoreboard

        ScoreHelper deathHelper = ScoreHelper.createScore(player);
        ScoreHelper killerHelper = ScoreHelper.createScore(killer);

    }
}
