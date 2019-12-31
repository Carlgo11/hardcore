package com.carlgo11.hardcore;

import com.carlgo11.hardcore.api.*;
import com.carlgo11.hardcore.gamestate.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Warmup implements Listener {

    final Game game;

    public Warmup(Hardcore parent, Game game)
    {
        this.game = game;
    }

    /**
     * Game state is in "warmup" mode.
     *
     * @return Warmup state
     */
    public boolean isWarmup()
    {
        return game.getGameState() == GameState.Warmup;
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
