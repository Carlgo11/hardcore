package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private final Hardcore hc;

    public PlayerDeath(Hardcore parent)
    {
        this.hc = parent;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player player = e.getEntity().getPlayer();
        hc.players().removePlayer(player);
    }
}
