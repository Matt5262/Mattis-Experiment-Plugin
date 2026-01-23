package com.matt5262.mattisExperimentPlugin.dbno;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class DBNOManager {

    // Stores downed players and their remaining time in seconds
    private final Map<UUID, Integer> downedPlayers = new HashMap<>();
    private final Map<UUID, Float> originalWalkSpeed = new HashMap<>();
    private final CrawlController crawlController = new CrawlController();


    /**
     * Call this method to set a player into DBNO.
     * @param player the player to put in DBNO
     */

    public void setDowned(Player player) {
        UUID uuid = player.getUniqueId();
        if (downedPlayers.containsKey(uuid)) return;

        downedPlayers.put(uuid, 60);
        crawlController.startCrawling(player);

        // Store original speed ONCE
        originalWalkSpeed.put(uuid, player.getWalkSpeed());

        player.sendMessage("DEBUG: You are now DOWNED! You have 60 seconds to be revived.");

        // DBNO restrictions
        player.setWalkSpeed(0.1f);
        player.setFoodLevel(6);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isDowned(player)) {
                    cancel();
                    return;
                }

                crawlController.update(player);
            }
        }.runTaskTimer(
                com.matt5262.mattisExperimentPlugin.MattisExperimentPlugin.getPluginInstance(),
                1,
                1
        );
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !downedPlayers.containsKey(uuid)) {
                    cancel();
                    return;
                }

                int timeLeft = downedPlayers.get(uuid) - 1;
                downedPlayers.put(uuid, timeLeft);

                if (timeLeft % 10 == 0 || timeLeft <= 5) {
                    player.sendMessage("DEBUG: DBNO timer: " + timeLeft + " seconds left");
                }

                if (timeLeft <= 0) {
                    downedPlayers.remove(uuid);
                    originalWalkSpeed.remove(uuid);
                    player.sendMessage("DEBUG: You have died in DBNO!");
                    crawlController.stopCrawling(player);
                    player.setHealth(0);
                    cancel();
                }
            }
        }.runTaskTimer(
                com.matt5262.mattisExperimentPlugin.MattisExperimentPlugin.getPluginInstance(),
                20, 20
        );
    }

    /**
     * Check if a player is currently downed.
     */
    public boolean isDowned(Player player) {
        return downedPlayers.containsKey(player.getUniqueId());
    }
    /**
     * Revive a downed player manually.
     * @param player the player to revive
     */

    public void revive(Player player) {
        UUID uuid = player.getUniqueId();
        if (!downedPlayers.containsKey(uuid)) return;

        downedPlayers.remove(uuid);

        Float speed = originalWalkSpeed.remove(uuid);
        if (speed != null) {
            player.setWalkSpeed(speed);
        }
        crawlController.stopCrawling(player);
        player.sendMessage("DEBUG: You have been revived!");
    }

}
