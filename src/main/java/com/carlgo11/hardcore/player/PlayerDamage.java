package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerDamage implements Listener {

    private Hardcore hc;

    public PlayerDamage(Hardcore parent)
    {
        this.hc = parent;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDamage(EntityDamageEvent e)
    {
        if (hc.game().getGameState() == 1) {
            DamageCause cause = e.getCause();
            if (!cause.equals(DamageCause.FALL)) {
                if (e.getEntity() instanceof Player) {
                    double damage = e.getDamage();
                    e.setDamage(damage * hc.game().getDifficulty());
                }
            }
        } else {
            e.setCancelled(true);
        }
    }

    /**
     * Disables player damage being multiplied with the difficulty. The function
     * only disables the multiplication if ignore-player-damage is set to true
     * in the config.
     *
     * @param e EntityDamageByEntityEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e)
    {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if (hc.getConfig().getBoolean("difficulty.ignore-player-damage")) {
                e.setDamage(e.getDamage() * 0.5);
            }
            if (hc.getConfig().getBoolean("difficulty.peaceful-first-level") && hc.game().getDifficulty() == hc.getConfig().getInt("difficulty.start-difficulty")) {
                e.setCancelled(true);
            }
        }
    }
}
