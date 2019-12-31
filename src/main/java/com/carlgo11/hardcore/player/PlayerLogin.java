package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.api.Game;
import com.carlgo11.hardcore.gamestate.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogin implements Listener {

    final Game game;

    public PlayerLogin(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (game.getGameState() == GameState.Ending) {
            e.setKickMessage("Game restarting...\n\nPlease try again in a few seconds.");
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        }
    }
}
