package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.api.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnect implements Listener {

    private final Hardcore hc;
    final Players players;

    public PlayerDisconnect(Hardcore parent, Players players)
    {
        this.hc = parent;
        this.players = players;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        players.removePlayer(player);
    }
}
