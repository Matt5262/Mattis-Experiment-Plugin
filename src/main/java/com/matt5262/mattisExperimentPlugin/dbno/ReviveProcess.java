package com.matt5262.mattisExperimentPlugin.dbno;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ReviveProcess {

    private final Player medic;
    private final Player target;
    private int ticksRemaining;
    private final Location startLocation;

    public ReviveProcess(Player medic, Player target, int seconds) {
        this.medic = medic;
        this.target = target;
        this.ticksRemaining = seconds * 20;
        this.startLocation = medic.getLocation().clone();
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Player getMedic() {
        return medic;
    }

    public Player  getTarget() {
        return target;
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }

    public void tick() {
        ticksRemaining--;
    }

    public boolean isFinished() {
        return ticksRemaining <= 0;
    }
}
