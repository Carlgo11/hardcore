package com.carlgo11.hardcore.server;

import com.carlgo11.hardcore.api.Game;
import com.carlgo11.hardcore.gamestate.GameState;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPing implements Listener {

    final Game game;

    public ServerPing(Game game) {
        this.game = game;
    }

    /**
     * Display the server's MOTD. This is done every time someone pings the
     * server.
     *
     * @param event ServerListPingEvent
     */
    @EventHandler
    public void onMOTD(ServerListPingEvent event) {
        GameState state = game.getGameState();
        String motd = event.getMotd() + "";
        if (state == GameState.Warmup) event.setMotd(motd + ChatColor.GREEN + "Warmup...");
        else if (state == GameState.Running) event.setMotd(motd + ChatColor.RED + "Running...");
        else if (state == GameState.Starting) event.setMotd(motd + ChatColor.GOLD + "Starting...");
        else if (state == GameState.Ending) event.setMotd(motd + ChatColor.YELLOW + "Ending...");
    }
}

