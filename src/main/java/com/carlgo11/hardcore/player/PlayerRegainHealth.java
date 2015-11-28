package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class PlayerRegainHealth implements Listener {

    private Hardcore hc;

    public PlayerRegainHealth(Hardcore parent)
    {
        this.hc = parent;
    }

    @EventHandler
    public void onPlayerHeal(EntityRegainHealthEvent e)
    {
        if (e.getEntity() instanceof Player) {
            if (!e.getRegainReason().equals(RegainReason.MAGIC_REGEN)) {
                e.setCancelled(true);
            }
        }
    }
}
