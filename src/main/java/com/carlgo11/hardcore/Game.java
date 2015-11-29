package com.carlgo11.hardcore;

import java.util.ArrayList;
import java.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class Game {

    private final Hardcore hc;
    private final Timer timer = new Timer();
    private int gamestate; // 0 = warmup, 1 = running, 2 starting, 3 ending
    private ArrayList<Player> players = new ArrayList<>();
    public int difficulty;

    public Game(Hardcore parent)
    {
        this.hc = parent;
    }

    public void startGame()
    {
        ArrayList<Player> plyrs = new ArrayList<>(Bukkit.getOnlinePlayers());
        setPlayers(plyrs);
        for(Player p: plyrs){
            p.setGameMode(GameMode.SURVIVAL);
        }
        setGameState(1);
        loop();
    }

    private void loop()
    {
        hc.broadcastMessage(ChatColor.GREEN+"Game started!");
        long time = 20 * 60 * hc.getConfig().getInt("difficulty.delay");
        hc.getServer().getScheduler().scheduleSyncRepeatingTask(hc, new Runnable() {
            @Override
            public void run()
            {
                if (gamestate == 1) {
                    int max = hc.getConfig().getInt("difficulty.max");
                    if (max == -1 || difficulty < max) {
                        nextDifficulty();
                        hc.itemDrop();
                        if (getDifficulty() != 1) {
                            hc.broadcastMessage(ChatColor.GOLD + "Next difficulty: " + getDifficulty());
                        }
                    }
                }
            }
        }, 20L, time);
    }

    private void nextDifficulty()
    {
        difficulty = difficulty + (hc.getConfig().getInt("difficulty.addition"));
    }

    public void stopGame()
    {
        setGameState(0);
        timer.cancel();
    }

    public int getDifficulty()
    {
        return this.difficulty;
    }

    public ArrayList<Player> getPlayers()
    {
        return this.players;
    }

    public void setPlayers(ArrayList<Player> players)
    {
        this.players = players;
    }

    public void removePlayer(Player player)
    {
        if (this.players.contains(player)) {
            ArrayList<Player> p = getPlayers();
            if ((p.size()-1) >= hc.getConfig().getInt("game.game-end")) {
                ArrayList<Player> plyrs = getPlayers();
                plyrs.remove(player);
                this.setPlayers(plyrs);
                hc.broadcastMessage(ChatColor.YELLOW + "Only " + p.size() + " players left!");
            } else if (gamestate == 1) {
                hc.broadcastMessage(ChatColor.GREEN + "GAME ENDED! The Winner is " + player.getName() + "!");
                gamestate = 3;
                Bukkit.getScheduler().scheduleSyncDelayedTask(hc, new Runnable() {
                    @Override
                    public void run()
                    {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.kickPlayer("Game restarting...");
                        }
                    }
                }, (hc.getConfig().getInt("game.end-delay") * 20));
            }
        }
    }

    public void addPlayer(Player player)
    {
        ArrayList<Player> plyrs = getPlayers();
        plyrs.add(player);
        this.setPlayers(plyrs);
    }

    public int getGameState()
    {
        return gamestate;
    }

    public void setGameState(int newstate)
    {
        gamestate = newstate;
    }
}
