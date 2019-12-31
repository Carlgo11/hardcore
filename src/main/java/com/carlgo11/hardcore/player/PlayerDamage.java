package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.api.*;
import com.carlgo11.hardcore.gamestate.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerDamage implements Listener {

    private final Hardcore hc;
    final Game game;

    public PlayerDamage(Hardcore parent, Game game)
    {
        this.hc = parent;
        this.game = game;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDamage(EntityDamageEvent e)
    {
        if (game.getGameState() == GameState.Running) {
            DamageCause cause = e.getCause();
            if (!cause.equals(DamageCause.FALL) && !cause.equals(DamageCause.ENTITY_ATTACK)) {
                if (e.getEntity() instanceof Player) {
                    double damage = e.getDamage();
                    e.setDamage(damage * game.getDifficulty());
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
            if (hc.getConfig().getBoolean("difficulty.peaceful-first-level") && game.getDifficulty() == hc.getConfig().getInt("difficulty.start-difficulty")) {
                e.setCancelled(true);
            }
        } else if (e.getEntity() instanceof Player) {
            double damage = e.getDamage();
            e.setDamage(damage * game.getDifficulty());
        }
    }
}
