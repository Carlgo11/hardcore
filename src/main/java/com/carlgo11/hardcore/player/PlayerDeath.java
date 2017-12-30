package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.api.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private final Hardcore hc;
    final Players players;

    public PlayerDeath(Hardcore parent, Players players)
    {
        this.hc = parent;
        this.players = players;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player player = e.getEntity().getPlayer();
        players.removePlayer(player);
    }
}
