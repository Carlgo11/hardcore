package com.carlgo11.hardcore;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

public class Game {
    
    private final Hardcore hc;
    private int gamestate; // 0 = warmup, 1 = running, 2 starting, 3 ending
    private ArrayList<Player> players = new ArrayList<>();
    public int difficulty;
    private int loop;
    private final AlivePlayers getplayers;
    
    public Game(Hardcore parent)
    {
        this.hc = parent;
        this.getplayers = new AlivePlayers();
    }
    
    public AlivePlayers alivePlayers()
    {
        return getplayers;
    }

    /**
     * Set the game state to 1 = Running and start the {@link #loop loop}.
     */
    public void startGame()
    {
        ArrayList<Player> plyrs = new ArrayList<>(Bukkit.getOnlinePlayers());
        alivePlayers().setPlayers(plyrs);
        for (Player p : plyrs) {
            p.setGameMode(GameMode.SURVIVAL);
            p.setFlying(false);
            p.getInventory().clear();
            p.getInventory().addItem(new ItemStack(Material.COMPASS, 1));
            resetPlayerHealth(p);
        }
        setGameState(1);
        hc.getServer().getWorlds().get(0).setTime(hc.getConfig().getLong("game.start-time"));
        loop();
    }

    /**
     * Change the difficulty and give {@link #alivePlayers() alive players} an
     * item every x minutes
     */
    private void loop()
    {
        hc.broadcastMessage(ChatColor.GREEN + "Game started!");
        long time = 20 * 60 * hc.getConfig().getInt("difficulty.delay");
        loop = hc.getServer().getScheduler().scheduleSyncRepeatingTask(hc, () -> {
            if (gamestate == 1) {
                int max = hc.getConfig().getInt("difficulty.max");
                if (max == -1 || difficulty <= max) {
                    hc.itemDrop();
                    if (getDifficulty() == 0) {
                        difficulty = hc.getConfig().getInt("difficulty.start-difficulty");
                        hc.broadcastMessage(ChatColor.GOLD + "Next difficulty: " + getDifficulty());
                    } else {
                        nextDifficulty();
                        hc.broadcastMessage(ChatColor.GOLD + "Next difficulty: " + getDifficulty());
                    }
                }
            } else {
                Bukkit.getScheduler().cancelTask(loop);
            }
        }, 20L, time);
    }

    /**
     * Change the difficulty to the next level
     */
    private void nextDifficulty()
    {
        difficulty = difficulty + (hc.getConfig().getInt("difficulty.addition"));
    }

    /**
     * Stop the game, kick all players and stop the server.
     */
    public void stopGame()
    {
        setGameState(3);
        Bukkit.getScheduler().cancelTask(loop);
        Bukkit.getScheduler().scheduleSyncDelayedTask(hc, new Runnable() {
            @Override
            public void run()
            {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.kickPlayer("Game restarting...");
                }
                Bukkit.getServer().shutdown();
            }
        }, (hc.getConfig().getInt("game.end-delay") * 20));
    }

    /**
     * Returns current game difficulty
     *
     * @return Current difficulty in int value
     * @see #difficulty
     */
    public int getDifficulty()
    {
        return this.difficulty;
    }

    /**
     * Get the current game state.
     *
     * @return Current game state. 0 = Warmup, 1 = Running, 2 = Starting, 3 =
     * Ending
     */
    public int getGameState()
    {
        return gamestate;
    }

    /**
     * Set the current game state.
     *
     * @param newstate The new game state
     */
    public void setGameState(int newstate)
    {
        gamestate = newstate;
    }
    
    private void getGameEndMessage(ArrayList<Player> players, Player player)
    {
        if (players.size() >= 1) {
            String aliveplayers = null;
            for (Player p : players) {
                if (aliveplayers == null) {
                    aliveplayers += p.getName();
                } else {
                    aliveplayers += ", " + p.getName();
                }
            }
            hc.broadcastMessage(ChatColor.GREEN + "GAME ENDED! The Winners are " + aliveplayers + "!");
        } else {
            hc.broadcastMessage(ChatColor.GREEN + "GAME ENDED! The Winner is " + player.getName() + "!");
        }
    }

    /**
     * A player might sometimes not have max health when the game starts. This
     * function resets all possible health parameters.
     *
     * @param player Player to reset health to
     */
    private void resetPlayerHealth(Player player)
    {
        player.setHealth(player.getMaxHealth()); //Player might have extra lifes due to custom server settings.
        player.setFoodLevel(20);
    }

    public class AlivePlayers {

        /**
         * Get the players that are currently alive.
         *
         * @return The alive players. If non are alive then null.
         */
        public ArrayList<Player> getPlayers()
        {
            return players;
        }

        /**
         * Add a new player to the game.
         *
         * @param player New player
         */
        public void addPlayer(Player player)
        {
            ArrayList<Player> plyrs = getPlayers();
            plyrs.add(player);
            setPlayers(plyrs);
            if (getGameState() == 1) {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }

        /**
         * Remove a single player from the game.
         *
         * @param player Player to remove from the game
         */
        public void removePlayer(Player player)
        {
            if (players.contains(player)) {
                player.setGameMode(GameMode.SPECTATOR);
                ArrayList<Player> plyrs = getPlayers();
                plyrs.remove(player);
                Team team = hc.teams.inTeam(player);
                if (team != null) {
                    hc.teams.getTeam(team.getName()).removePlayer(player);
                }
                if (plyrs.size() > (hc.getConfig().getInt("game.game-end") + 1)) {
                    setPlayers(plyrs);
                    hc.broadcastMessage(ChatColor.YELLOW + "Only " + plyrs.size() + " players left!");
                } else if (gamestate == 1) {
                    getGameEndMessage(plyrs, player);
                    stopGame();
                }
            }
        }

        /**
         * Set alive players. Should not be used for adding or removing a single
         * player!
         *
         * @param aliveplayers Alive players
         */
        public void setPlayers(ArrayList<Player> aliveplayers)
        {
            players = aliveplayers;
        }
        
    }
}
