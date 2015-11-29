package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnect implements Listener {

    private Hardcore hc;

    public PlayerDisconnect(Hardcore parent)
    {
        this.hc = parent;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e){
        Player player = e.getPlayer();
        hc.game().removePlayer(player);
    }
}
