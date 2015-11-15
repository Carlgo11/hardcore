package com.carlgo11.hardcore;

import com.carlgo11.hardcore.Player.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Hardcore extends JavaPlugin {

    private transient Database database;
    private transient Config config;
    
    public void onEnable()
    {
        config = new Config(this);
        database = new Database(this);
        
        final PluginManager pm = getServer().getPluginManager();
        registerListeners(pm);
    }

    public void onDisable()
    {

    }

    private void registerListeners(PluginManager pm)
    {
        HandlerList.unregisterAll(this);
        pm.registerEvents(new PlayerConnect(this), this);
    }
    
    
    public Database getSettingsd(){
        return database;
    }
    public Config getConf(){
        return config;
    }
}
