package com.carlgo11.hardcore;

import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Game {

    private Hardcore hc;
    private Timer timer = new Timer();
    private int gamestate;
    private Player[] players;

    public Game(Hardcore plug)
    {
        this.hc = plug;
    }
    private int difficulty;

    public void startGame()
    {
        int size = Bukkit.getOnlinePlayers().size();
        Player[] Players = Bukkit.getOnlinePlayers().toArray(new Player[size]);
        setPlayers(Players);
        loop();
    }

    private void loop()
    {
        timer.schedule(new TimerTask() { //TODO: Change to bukkit's "scheduled task"
            public void run()
            {
                nextDifficulty();
                hc.itemDrop();
                System.out.println("Next difficulty: " + getDifficulty());
            }
        }, 0, 5 * 60 * 1000); // M(5) * S(60) * MS(1000);
    }

    private void nextDifficulty()
    {
        difficulty = difficulty + 1;
    }

    public void stopGame()
    {
        timer.cancel();
    }

    public int getDifficulty()
    {
        return difficulty;
    }

    public Player[] getPlayers()
    {
        return players;
    }

    public void setPlayers(Player[] players)
    {
        this.players = players;
    }

    public int getGameState()
    {
        return gamestate;
    }
}
