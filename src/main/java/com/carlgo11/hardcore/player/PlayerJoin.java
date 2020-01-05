package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.api.Game;
import com.carlgo11.hardcore.api.Players;
import com.carlgo11.hardcore.gamestate.GameState;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    final Game game;
    final Players players;
    private final Hardcore hc;

    public PlayerJoin(Hardcore parent, Game game, Players players) {
        this.hc = parent;
        this.game = game;
        this.players = players;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        GameState state = game.getGameState();

        if (state == GameState.Warmup) {
            players.addPlayer(player);
            int plyrs = hc.getConfig().getInt("game.start-players") - players.getPlayersAlive().size();
            if (plyrs <= 0) {
                game.startGame();
            } else {
                player.setGameMode(GameMode.ADVENTURE);
                hc.sendMessage(player, "Waiting for " + plyrs + " more players...");
            }
        }

        if (state == GameState.Starting) hc.sendMessage(player, ChatColor.GOLD + "Game starting...");

        if (state == GameState.Running) {
            if (!player.hasPlayedBefore() && hc.getConfig().getBoolean("game.can-join-game")) {
                players.addPlayer(player);
                e.setJoinMessage(ChatColor.GREEN + player.getName() + " joined the game!");
            } else {
                player.setGameMode(GameMode.SPECTATOR);
                e.setJoinMessage(ChatColor.YELLOW + player.getName() + " is now spectating.");
            }
        }

    }
}
