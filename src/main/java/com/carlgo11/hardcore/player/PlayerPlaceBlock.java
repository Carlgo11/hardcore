package com.carlgo11.hardcore.player;

import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerPlaceBlock implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPlaceBlock(BlockPlaceEvent event)
    {
        if (!event.isCancelled()) {
            if (event.getBlock().getType().equals(Material.TNT)) {
                event.getBlock().setType(Material.AIR);
                event.getBlock().getWorld().spawn(event.getBlock().getLocation().add(0.5, 0.25, 0.5), TNTPrimed.class);
            }
        }
    }

}
