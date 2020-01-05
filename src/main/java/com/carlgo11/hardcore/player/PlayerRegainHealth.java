package com.carlgo11.hardcore.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class PlayerRegainHealth implements Listener {

    public PlayerRegainHealth() {
    }

    @EventHandler
    public void onPlayerHeal(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player)
            if (!e.getRegainReason().equals(RegainReason.MAGIC_REGEN))
                e.setCancelled(true);
    }
}
