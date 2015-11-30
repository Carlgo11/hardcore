package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {

    private Hardcore hc;

    public PlayerInteract(Hardcore parent)
    {
        this.hc = parent;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        Material material = e.getMaterial();
        Action action = e.getAction();
        Block block = e.getClickedBlock();

        if (material.equals(Material.COMPASS)) {
            itemCompass(player, item, material, action, block);
        } else if (material.equals(Material.WATCH)) {
            itemWatch(player, item, material, action, block);
        }
    }

    private void itemCompass(Player player, ItemStack item, Material material, Action action, Block block)
    {
        
    }

    private void itemWatch(Player player, ItemStack item, Material material, Action action, Block block)
    {

    }
}
