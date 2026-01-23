package com.matt5262.mattisExperimentPlugin.dbno;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReviveManager {

    private final DBNOManager dbnoManager;
    private final Map<UUID, ReviveProcess> activeRevives = new HashMap<>();
    private final Map<UUID, Boolean> holdingRightClick = new HashMap<>();


    public ReviveManager(DBNOManager dbnoManager) {
        this.dbnoManager = dbnoManager;
    }

    public Map<UUID, Boolean> getHoldingRightClickMap() {
        return holdingRightClick;
    }

    public void startRevive(Player medic, Player target) {
        if (activeRevives.containsKey(medic.getUniqueId())) return;

        ReviveProcess process = new ReviveProcess(medic, target, 5);
        holdingRightClick.put(medic.getUniqueId(), true);

        activeRevives.put(medic.getUniqueId(), process);

        medic.sendMessage("DEBUG: Hold right click to revive...");
        target.sendMessage("DEBUG: A medic is reviving you...");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!validate(process) || !holdingRightClick.getOrDefault(medic.getUniqueId(), false)) {
                    cancelRevive(medic, "Revive cancelled.");
                    cancel();
                    return;
                }

                process.tick();

                if (process.isFinished()) {
                    dbnoManager.revive(target);

                    medic.sendMessage("DEBUG: Revive successful!");
                    target.sendMessage("DEBUG: You have been revived!");

                    activeRevives.remove(medic.getUniqueId());
                    holdingRightClick.remove(medic.getUniqueId()); // ← HERE

                    cancel();
                }

            }
        }.runTaskTimer(com.matt5262.mattisExperimentPlugin.MattisExperimentPlugin.getPluginInstance(), 0, 1);
    }

    private boolean validate(ReviveProcess process){
        Player medic = process.getMedic();
        Player target = process.getTarget();

        if (!medic.isOnline() || !target.isOnline()) return false;
        if (!dbnoManager.isDowned(target)) return false;

        if (medic.getLocation().distanceSquared(target.getLocation()) > 4) return false;

        // Cancel if medic moved
        if (medic.getLocation().distanceSquared(process.getStartLocation()) > 0.01) {
            return false;
        }


        return true;
    }

    public void cancelRevive(Player medic, String reason) {
        activeRevives.remove(medic.getUniqueId());
        holdingRightClick.remove(medic.getUniqueId()); // ← HERE
        medic.sendMessage("DEBUG: " + reason);
    }


}

