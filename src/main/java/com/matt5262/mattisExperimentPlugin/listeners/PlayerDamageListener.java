package com.matt5262.mattisExperimentPlugin.listeners;

import com.matt5262.mattisExperimentPlugin.dbno.DBNOManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {
    // implements listener!!

    private final DBNOManager dbnoManager;

    public PlayerDamageListener(DBNOManager manager) {
        this.dbnoManager = manager;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        // If the entity that got damaged isn't a player then cancel this function.

        // If player is already downed, ignore normal damage
        if (dbnoManager.isDowned(player)) {
            event.setCancelled(true);
            return;
        }

        // Check if damage would kill the player
        double newHealth = player.getHealth() - event.getFinalDamage();
        if (newHealth <= 0) {
            event.setCancelled(true); // Cancel death
            dbnoManager.setDowned(player); // Put into DBNO
        }
    }

}
