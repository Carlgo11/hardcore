package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryClick implements Listener {

    public PlayerInventoryClick(Hardcore parent) {
        this.setWandInventoryItems();
    }

    public static Inventory wandInventory = Bukkit.createInventory(null, 9, "Choose Wand Effect");

    private void setWandInventoryItems() {
        wandInventory.setItem(0, new ItemStack(Material.ARROW));
        wandInventory.setItem(1, new ItemStack(Material.TNT));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        int slot = event.getSlot();

        if (inventory.equals(wandInventory)) {
            event.setCancelled(true);
            PlayerInteract.setPlayerWandAbility(player, slot);
            player.closeInventory();
        }
    }
}
