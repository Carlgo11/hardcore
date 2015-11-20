package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerDamage implements Listener {

    private Hardcore hc;

    public PlayerDamage(Hardcore parent)
    {
        this.hc = parent;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e)
    {
        DamageCause cause = e.getCause();
        if (!cause.equals(DamageCause.FALL)) {
            if (e.getEntity() instanceof Player) {
                double damage = e.getDamage();
                e.setDamage(damage * hc.game().getDifficulty());
            }
        }
    }
}
