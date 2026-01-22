package com.matt5262.mattisExperimentPlugin;

import com.matt5262.mattisExperimentPlugin.dbno.DBNOManager;
import com.matt5262.mattisExperimentPlugin.listeners.PlayerDamageListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MattisExperimentPlugin extends JavaPlugin {
    /*
      Called when the plugin is enabled on the server.
      This is essentially "startup logic".
     */
    private static MattisExperimentPlugin instance;
    private DBNOManager dbnoManager;

    @Override
    public void onEnable() {
        instance = this;

        dbnoManager = new DBNOManager();

        getServer().getPluginManager().registerEvents(new PlayerDamageListener(dbnoManager), this);

        getLogger().info("CivilizationExperiment has started!");

    }
    @Override
    public void onDisable() {
        getLogger().info("CivilizationExperiment has stopped.");
    }
    public static MattisExperimentPlugin getPluginInstance() {
        return instance;
    }
    public DBNOManager getDbnoManager() {
        return dbnoManager;
    }
}
