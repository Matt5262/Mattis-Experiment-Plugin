package com.matt5262.mattisExperimentPlugin.dbno;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrawlController {

    private final Map<UUID, Location> crawlBlocks = new HashMap<>();

    /**
     * Forces a player into crawling by placing a barrier above their head
     */
    public void startCrawling(Player player) {
        UUID uuid = player.getUniqueId();

        if (crawlBlocks.containsKey(uuid)) return;

        Location headlocation = player.getLocation().clone().add(0, 1.8, 0);
        Block block = headlocation.getBlock();

        // Only place if empty
        if (block.getType() != Material.AIR) return;

        block.setType(Material.BARRIER);
        crawlBlocks.put(uuid, block.getLocation());

        player.sendMessage("DEBUG: Crawling forced.");
    }

    /**
     * Removes the crawling restriction
     */
    public void stopCrawling(Player player) {
        UUID uuid = player.getUniqueId();

        Location loc = crawlBlocks.remove(uuid);
        if (loc == null) return;

        Block block = loc.getBlock();
        if (block.getType() == Material.BARRIER) {
            block.setType(Material.AIR);
        }

        player.sendMessage("DEBUG: Crawling released.");
    }

    public void update(Player player) {
        UUID uuid = player.getUniqueId();

        Location oldLoc = crawlBlocks.get(uuid);
        if (oldLoc == null) return;

        Location newLoc = player.getLocation().clone().add(0, 1.8, 0);

        if (!oldLoc.getBlock().equals(newLoc.getBlock())) {
            // Remove old block
            if (oldLoc.getBlock().getType() == Material.BARRIER) {
                oldLoc.getBlock().setType(Material.AIR);
            }

            // Place new block
            Block newBlock = newLoc.getBlock();
            if (newBlock.getType() == Material.AIR) {
                newBlock.setType(Material.BARRIER);
                crawlBlocks.put(uuid, newBlock.getLocation());
            }
        }
    }

}
