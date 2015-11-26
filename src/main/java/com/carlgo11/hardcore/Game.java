package com.carlgo11.hardcore;

import java.util.ArrayList;
import java.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Game {

    private final Hardcore hc;
    private final Timer timer = new Timer();
    private int gamestate; // 0 = warmup, 1 = running, 2 starting
    private ArrayList<Player> players = new ArrayList<>();
    public int difficulty;

    public Game(Hardcore plug)
    {
        this.hc = plug;
    }

    public void startGame()
    {
        ArrayList<Player> plyrs = new ArrayList<>(Bukkit.getOnlinePlayers());
        setPlayers(plyrs);
        setGameState(1);
        loop();
    }

    private void loop()
    {
        int time = hc.getConfig().getInt("difficulty.delay");
        long t = 20 * 60 * time;
        hc.getServer().getScheduler().scheduleSyncRepeatingTask(hc, new Runnable() {
            @Override
            public void run()
            {
                int max = hc.getConfig().getInt("difficulty.max");
                if (max == -1 || difficulty < max) {
                    nextDifficulty();
                    hc.itemDrop();
                    if (getDifficulty() != 1) {
                        hc.broadcastMessage(ChatColor.GOLD + "Next difficulty: " + getDifficulty());
                    }
                }
            }
        }, 20L, t);
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
        return difficulty;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public void setPlayers(ArrayList<Player> players)
    {
        this.players = players;
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
