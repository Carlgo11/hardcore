package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.CraftingRecipe;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class PlayerCraftItem implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCraftItem(CraftItemEvent event)
    {
        if (!event.isCancelled()) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (event.getRecipe().getResult().isSimilar(CraftingRecipe.setCraftingRecipeTNT().getResult())) {
                    if (player.getLevel() >= (3*event.getRecipe().getResult().getAmount())) {
                        player.setLevel((player.getLevel() - (3*event.getRecipe().getResult().getAmount())));
                    } else {
                        event.setResult(Event.Result.DENY);
                    }
                }
            }
        }
    }
}
