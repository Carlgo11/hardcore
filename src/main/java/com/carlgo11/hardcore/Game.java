package com.carlgo11.hardcore;

import java.util.Timer;
import java.util.TimerTask;

public class Game {

    private Hardcore hc;
    private Timer timer = new Timer();
    private int gamestate;

    public Game(Hardcore plug)
    {
        this.hc = plug;
    }
    private int difficulty;
    
    public void startGame()
    {
        setPlayers(null); //TODO: set value
        loop();
    }

    private void loop()
    {
        

        timer.schedule(new TimerTask() {
            public void run()
            {
        nextDifficulty();         
            }
        }, 0, 5 * 60 * 1000); // M(5) * S(60) * MS(1000);
    }
    private void nextDifficulty(){
        difficulty=difficulty++;
    }

    public void stopGame()
    {
        timer.cancel();
    }

    public int getDifficulty()
    {
        return difficulty;
    }

    public String[] getPlayers()
    {
        return null; //TODO: set value
    }

    public void setPlayers(String[] players)
    {

    }
    public int getGameState(){
        return gamestate;
    }
}
