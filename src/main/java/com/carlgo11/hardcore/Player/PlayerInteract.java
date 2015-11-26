package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    private Hardcore hc;

    public PlayerInteract(Hardcore parent)
    {
        this.hc = parent;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {

    }
}
