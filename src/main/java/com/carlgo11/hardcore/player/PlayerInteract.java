package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.Utilities;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PlayerInteract implements Listener {

    private Hardcore hardCore;

    public PlayerInteract(Hardcore parent) {
        this.hardCore = parent;
    }

    public static Map<Player, Integer> findMap = new HashMap<Player, Integer>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // TODO: This can become resource intensive and create NullPointers
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Material material = event.getMaterial();
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        if (material.equals(Material.COMPASS)) {
            itemCompass(player, item, material, action, block);
        } else if (material.equals(Material.WATCH)) {
            itemWatch(player, item, material, action, block);
        }
    }

    @EventHandler
    public void onPlayerChangeItem(PlayerItemHeldEvent event) {
        findMap.remove(event.getPlayer());
    }

    /**
     * {@link Player} uses a compass.
     */
    private void itemCompass(Player player, ItemStack item, Material material, Action action, Block block) {
        List<String> playerList = hardCore.getServer().getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        Collections.sort(playerList);
        hardCore.getLogger().log(Level.WARNING, playerList.toString());

        int currentIndex = 0;
        if (findMap.containsKey(player)) currentIndex = findMap.get(player);

        // Get next player
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (playerList.size() >= currentIndex + 1 && playerList.size() > 1) {
                selectPlayer(player, item, hardCore.getServer().getPlayer(playerList.get(currentIndex + 1)));
                findMap.put(player, currentIndex + 1);
            }
        }

        // Get previous player
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            if (currentIndex - 1 >= 0) {
                selectPlayer(player, item, hardCore.getServer().getPlayer(playerList.get(currentIndex - 1)));
                findMap.put(player, currentIndex - 1);
            }
        }
    }

    /**
     * {@link Player} uses a watch.
     */
    private void itemWatch(Player player, ItemStack item, Material material, Action action, Block block) {

    }

    private void selectPlayer(Player currentPlayer, ItemStack item, Player facePlayer) {
        item.getItemMeta().setDisplayName(facePlayer.getDisplayName());
        currentPlayer.getLocation().setYaw(Utilities.lookAt(currentPlayer.getLocation(), facePlayer.getLocation()).getYaw());
    }
}
