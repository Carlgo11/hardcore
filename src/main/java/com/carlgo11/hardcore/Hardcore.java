package com.carlgo11.hardcore;

import com.carlgo11.hardcore.player.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Hardcore extends JavaPlugin {

    private transient Database database;
    private transient Config config;
    private transient Game game;
    private transient ItemDrop itemdrop;

    @Override
    public void onEnable()
    {
        config = new Config(this);
        database = new Database(this);
        game = new Game(this);
        itemdrop = new ItemDrop(this);
        setupDatabase();

        final PluginManager pm = getServer().getPluginManager();
        registerListeners(pm);
        game().startGame();
        setStartBorder();
    }

    @Override
    public void onDisable()
    {
        
    }

    private void registerListeners(PluginManager pm)
    {
        HandlerList.unregisterAll(this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerInteract(this), this);
        pm.registerEvents(new PlayerDamage(this), this);
        pm.registerEvents(new PlayerDeath(this), this);
        pm.registerEvents(new PlayerRegainHealth(this), this);
    }

    public Database getSettings()
    {
        return database;
    }

    public Config getConf()
    {
        return config;
    }

    private void setupDatabase()
    {
        String[] info = this.getConf().getDatabaseInfo();
        this.getSettings().updateConnection(info);
    }

    public Game game()
    {
        return game;
    }

    public void itemDrop()
    {
        itemdrop.dropItems();
    }

    public void broadcastMessage(String message)
    {
        String prefix = getConfig().getString("prefix");
        getServer().broadcastMessage(ChatColor.GREEN + prefix + ChatColor.YELLOW + message);
    }

    public void sendMessagef(Player player, String message)
    {
        String prefix = getConfig().getString("prefix");
        player.sendMessage(ChatColor.GREEN + prefix + ChatColor.YELLOW + message);
    }

    private void setStartBorder()
    {
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

            @Override
            public void run()
            {
                executeCommand("worldborder set " + getConfig().getString("border.start-distance") + " 1");
            }
        }, 40L); //Delay command 2 seconds to make sure the server is loaded.
    }

    public void executeCommand(String cmd)
    {
        System.out.println("CMD:" + cmd);
        getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }
}
