package com.carlgo11.hardcore;

import com.carlgo11.hardcore.api.*;
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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Hardcore extends JavaPlugin {

    private transient Game game;
    private transient ItemDrop itemdrop;
    public transient Teams teams;
    private static Logger log;
    private transient Players players;

    @Override
    public void onEnable()
    {
        log = getLogger();
        loadConfigFile("config.yml");
        loadConfigFile("items.yml");
        game = new Game(this);
        teams = new Teams(this);
        players = new Players(this, game);
        itemdrop = new ItemDrop(this, players);
        Game.minPlayers = getConfig().getInt("game.game-end");
        loadCommands();

        final PluginManager pm = getServer().getPluginManager();
        registerListeners(pm);
        setStartBorder();
        setDifficulty();
        getServer().addRecipe(CraftingRecipe.setCraftingRecipeTNT());
        getServer().getWorlds().get(0).setDifficulty(Difficulty.PEACEFUL);
    }

    @Override
    public void onDisable()
    {

    }

    private void registerListeners(PluginManager pm)
    {
        HandlerList.unregisterAll(this);
        pm.registerEvents(new PlayerDamage(this, game), this);
        pm.registerEvents(new PlayerDeath(players), this);
        pm.registerEvents(new PlayerDisconnect(this, players), this);
        pm.registerEvents(new PlayerInteract(this, players), this);
        pm.registerEvents(new PlayerJoin(this, game, players), this);
        pm.registerEvents(new PlayerLogin(game), this);
        pm.registerEvents(new PlayerRegainHealth(this), this);
        pm.registerEvents(new ServerPing(game), this);
        pm.registerEvents(new Warmup(this, game), this);
        pm.registerEvents(new PlayerCraftItem(), this);
        pm.registerEvents(new PlayerPlaceBlock(), this);
        pm.registerEvents(new PlayerInventoryClick(this), this);
    }

    /**
     * Check if a file exist in the plugin's folder. If not then create it.
     *
     * @param filename Name of the file.
     */
    private void loadConfigFile(String filename)
    {
        File file = new File(getDataFolder(), filename);
        if (!file.exists()) {
            YamlConfiguration configfile = YamlConfiguration.loadConfiguration(new File(getDataFolder() + "/" + filename));
            this.saveResource(filename, false);
            configfile.options().copyHeader(true);
            outputWarning("No " + filename + " detected, file created.");
        }
    }

    private void setStartBorder()
    {
        getServer().getWorlds().get(0).getWorldBorder().setSize(getConfig().getInt("border.start-distance"));
        getServer().getWorlds().get(0).getWorldBorder().setWarningDistance(10);
        getServer().getWorlds().get(0).getWorldBorder().setWarningTime(30);
        getServer().getWorlds().get(0).getWorldBorder().setDamageAmount(0.1);
        getServer().getWorlds().get(0).getWorldBorder().setDamageBuffer(10);
    }

    public void setEndBorder()
    {
        getServer().getWorlds().get(0).getWorldBorder().setSize(getConfig().getInt("border.end-distance"), getConfig().getInt("border.time"));
    }

    private void loadCommands()
    {
        getCommand("difficulty").setExecutor(new CommandDifficulty(this, game));
        getCommand("game").setExecutor(new CommandGame(this, game, players));
        getCommand("vote").setExecutor(new CommandVote(this, game));
        getCommand("team").setExecutor(new CommandTeam(this, teams));
    }

    public Players players()
    {
        return players;
    }

    /**
     * Execute a command as the console.
     *
     * @param cmd Command to execute
     */
    public void executeCommand(String cmd)
    {
        getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    /**
     * Give all the alive players a new random item.
     */
    public void itemDrop()
    {
        itemdrop.dropItems();
    }

    public void outputInfo(String msg)
    {
        log.log(Level.INFO, msg);
    }

    public void outputWarning(String msg)
    {
        log.log(Level.WARNING, msg);
    }

    /**
     * Send a "no permissions" error-message to a player
     *
     * @param player Player to send error-message to.
     */
    public void badPermissions(CommandSender player)
    {
        sendMessage(player, ChatColor.RED + "You don't have permission to use that command");
    }

    /**
     * Broadcast a message to all players on the server using the official
     * styling of the plugin.
     *
     * @param message Message to broadcast
     */
    public void broadcastMessage(String message)
    {
        String prefix = getConfig().getString("prefix");
        getServer().broadcastMessage(ChatColor.GREEN + prefix + ChatColor.YELLOW + message);
    }

    /**
     * Send a message to a specific player.
     *
     * @param player Player to send message to.
     * @param message Message to send.
     */
    public void sendMessage(Player player, String message)
    {
        String prefix = getConfig().getString("prefix");
        player.sendMessage(ChatColor.GREEN + prefix + ChatColor.YELLOW + message);
    }

    /**
     * Send a message to a specific player.
     *
     * @param player Player to send message to.
     * @param message Message to send.
     */
    public void sendMessage(CommandSender player, String message)
    {
        String prefix = getConfig().getString("prefix");
        player.sendMessage(ChatColor.GREEN + prefix + ChatColor.YELLOW + message);
    }

    /**
     * Set <a href="http://minecraft.gamepedia.com/Difficulty">Minecraft
     * difficulty</a>. Not the same as game difficulty!
     */
    private void setDifficulty()
    {
        getWorlds().get(0).setDifficulty(Difficulty.valueOf(getConfig().getString("difficulty.mode").toUpperCase()));
        outputInfo("World difficulty set to " + getWorlds().get(0).getDifficulty());
    }
}
