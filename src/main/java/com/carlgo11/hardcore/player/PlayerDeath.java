package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.api.Players;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    final Players players;

    public PlayerDeath(Players players)
    {
        this.players = players;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player player = e.getEntity().getPlayer();
        players.removePlayer(player);
        Location l = player.getLocation().getBlock().getLocation();
        l.getWorld().spawnEntity(l, EntityType.LIGHTNING);
    }
}
