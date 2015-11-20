package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import java.util.ArrayList;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    private Hardcore hc;

    public PlayerDeath(Hardcore parent)
    {
        this.hc = parent;
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player player = e.getEntity().getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        ArrayList<Player> players = hc.game().getPlayers();
        players.remove(player);
        hc.game().setPlayers(players);
    }
}
