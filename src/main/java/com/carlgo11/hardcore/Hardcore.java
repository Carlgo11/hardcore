package com.carlgo11.hardcore;

import com.carlgo11.hardcore.commands.*;
import com.carlgo11.hardcore.player.*;
import com.carlgo11.hardcore.server.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getWorlds;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Hardcore extends JavaPlugin {

    private transient Database database;
    private transient Game game;
    private transient ItemDrop itemdrop;
    private Logger log;

    @Override
    public void onEnable()
    {
        log = getLogger();
        loadConfig();
        database = new Database(this);
        game = new Game(this);
        itemdrop = new ItemDrop(this);
        setupDatabase();
        loadCommands();

        final PluginManager pm = getServer().getPluginManager();
        registerListeners(pm);
        setStartBorder();
        setDifficulty();
    }

    @Override
    public void onDisable()
    {

    }

    private void registerListeners(PluginManager pm)
    {
        HandlerList.unregisterAll(this);
        pm.registerEvents(new PlayerDamage(this), this);
        pm.registerEvents(new PlayerDeath(this), this);
        pm.registerEvents(new PlayerDisconnect(this), this);
        pm.registerEvents(new PlayerInteract(this), this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerLogin(this), this);
        pm.registerEvents(new PlayerRegainHealth(this), this);
        pm.registerEvents(new ServerPing(this), this);
        pm.registerEvents(new Warmup(this), this);
    }

    public void loadConfig()
    {
        File config = new File(getDataFolder(), "config.yml");
        if (!config.exists()) {
            saveDefaultConfig();
            getConfig().options().copyHeader(true);
            String prefix = getConfig().getString("prefix");

            outputWarning("No config.yml detected, config.yml created");
        }
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

    private void loadCommands()
    {
        getCommand("difficulty").setExecutor(new CommandDifficulty(this));
        getCommand("game").setExecutor(new CommandGame(this));
    }

    public Database getSettings()
    {
        return database;
    }

    public Game game()
    {
        return game;
    }

    public void itemDrop()
    {
        itemdrop.dropItems();
    }

    public void executeCommand(String cmd)
    {
        getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    public void outputInfo(String msg)
    {
        log.log(Level.INFO, msg);
    }

    public void outputWarning(String msg)
    {
        log.log(Level.WARNING, msg);
    }

    public void badPermissions(CommandSender player)
    {
        sendMessage(player, ChatColor.RED + "You don't have permission to use that command");
    }

    private void setupDatabase()
    {
        String[] info = this.database.getDatabaseInfo();
        this.getSettings().updateConnection(info);
    }

    public void broadcastMessage(String message)
    {
        String prefix = getConfig().getString("prefix");
        getServer().broadcastMessage(ChatColor.GREEN + prefix + ChatColor.YELLOW + message);
    }

    public void sendMessage(Player player, String message)
    {
        String prefix = getConfig().getString("prefix");
        player.sendMessage(ChatColor.GREEN + prefix + ChatColor.YELLOW + message);
    }

    public void sendMessage(CommandSender player, String message)
    {
        String prefix = getConfig().getString("prefix");
        player.sendMessage(ChatColor.GREEN + prefix + ChatColor.YELLOW + message);
    }

    private void setDifficulty()
    {
        getWorlds().get(0).setDifficulty(Difficulty.getByValue(getConfig().getInt("difficulty.mode")));
        outputInfo("World difficulty set to " + getWorlds().get(0).getDifficulty());
    }
}
