package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.api.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogin implements Listener {

    private final Hardcore hc;
    final Game game;

    public PlayerLogin(Hardcore parent, Game game)
    {
        this.hc = parent;
        this.game = game;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e)
    {
        if (game.getGameState() == 2) {
            e.setKickMessage("Game restarting...\n\nPlease try again in a few seconds.");
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }
}
