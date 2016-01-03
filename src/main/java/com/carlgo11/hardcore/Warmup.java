package com.carlgo11.hardcore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Warmup implements Listener {

    private final Hardcore hc;

    public Warmup(Hardcore parent)
    {
        this.hc = parent;
    }

    /**
     * Game state is in "warmup" mode.
     * @return Warmup state
     */
    public boolean isWarmup()
    {
        if (hc.game().getGameState() == 0) {
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInteract(EntityInteractEvent e)
    {
        if (isWarmup()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetEvent e)
    {
        if (isWarmup()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent e)
    {
        if (isWarmup()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityFoodLevelChange(FoodLevelChangeEvent e)
    {
        if (isWarmup()) {
            e.setCancelled(true);
        }
    }
}
