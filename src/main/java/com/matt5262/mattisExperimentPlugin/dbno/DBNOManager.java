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

    /**
     * Call this method to set a player into DBNO.
     * @param player the player to put in DBNO
     */

    public void setDowned(Player player) {
        UUID uuid  = player.getUniqueId();
        if (downedPlayers.containsKey(uuid)) return;
        /*
         "uuid is this player's UUID. Set them to be downed, here is the player.
         If the hashmap called downedPlayers already has this player's uuid
         leave this function."
        */
        downedPlayers.put(uuid, 60); // 60 seconds timer
        player.sendMessage("DEBUG: You are now DOWNED! You have 60 seconds to be revived."); // Debug chat

        // Prevent player from moving normally (simulate crawling)
        player.setWalkSpeed(0.1f); // Very slow
        player.setFoodLevel(6); // Optional: simulate weakness

        // Schedule a repeating task to count down every second
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !downedPlayers.containsKey(uuid)) {
                    this.cancel();
                    return;
                }
                int timeLeft = downedPlayers.get(uuid) - 1;
                downedPlayers.put(uuid, timeLeft);

                // Debug chat every 10 seconds
                if (timeLeft % 10 == 0 || timeLeft <= 5) {
                    player.sendMessage("DEBUG: DBNO timer: " + timeLeft + " seconds left");
                }
                // % is basically if we say 55/3 is 18,33... then we remove ,33... so we got 18. Then we say 18*3 is 54, how many is left for 55? 1. The answer is 1.

                if (timeLeft <= 0) {
                    downedPlayers.remove(uuid);
                    player.sendMessage("DEBUG: You have died in DBNO!"); // Debug chat
                    player.setHealth(0);
                    this.cancel();
                }
            }
        }.runTaskTimer(com.matt5262.mattisExperimentPlugin.MattisExperimentPlugin.getPluginInstance(), 20, 20); // 20 ticks = 1 second
        // getPluginInstance will be an error until you register it.
        /*
        "Give me a timer. Edit the run... If the player is not online or uuid not found in the
        hashmap then cancel this timer, go out of the timer and continue the script.

        But if it is then set timeLeft to (get the value of the player's uuid and retract 1).
        Then replace the current value with timeLeft."

        Also, if timeLeft is equal to or less than 0 then remove the player's
        uuid from the hashmap and set the players health to 0 (loses a life).
        Then cancel the timer.
         */
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
        if (!isDowned(player)) return;

        downedPlayers.remove(player.getUniqueId());
        player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).getValue());
        player.setWalkSpeed((float) Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED)).getBaseValue()); // Restore normal speed
        player.sendMessage("DEBUG: You have been revived!"); // Debug chat
        // TODO: add cooldown or potion effect here if needed
    }
}
