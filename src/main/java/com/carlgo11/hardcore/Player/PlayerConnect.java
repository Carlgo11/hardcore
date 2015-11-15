package com.carlgo11.hardcore.Player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnect implements Listener {
    private Hardcore hc;

    public PlayerConnect(Hardcore parent) {
        this.hc = parent;
    }
    
    public void onPlayerConnect(PlayerJoinEvent e){
       
    }

}
