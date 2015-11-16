package com.carlgo11.hardcore;

import com.carlgo11.hardcore.Player.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Hardcore extends JavaPlugin {

    private transient Database database;
    private transient Config config;
    private transient Game game;
    

    public void onEnable()
    {
        config = new Config(this);
        database = new Database(this);
        game = new Game(this);
        setupDatabase();
        
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
    
    public Database getSettings(){
        return database;
    }

    public Config getConf(){
        return config;
    }

    private void setupDatabase(){
        String[] info = this.getConf().getDatabaseInfo();
        this.getSettings().updateConnection(info);
    }
    
    public Game game(){
        return game;
    }
}
