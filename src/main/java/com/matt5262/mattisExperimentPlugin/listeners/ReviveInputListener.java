package com.matt5262.mattisExperimentPlugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.UUID;

public class ReviveInputListener implements Listener {

    private final Map<UUID, Boolean> holdingRightClick;

    public ReviveInputListener(Map<UUID, Boolean> holdingRightClick) {
        this.holdingRightClick = holdingRightClick;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        holdingRightClick.put(player.getUniqueId(), true);
    }

    @EventHandler
    public void onRelease(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        holdingRightClick.put(player.getUniqueId(), false);
    }

}
