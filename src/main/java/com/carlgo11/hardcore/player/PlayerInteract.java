package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.Material;
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
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteract implements Listener {

    private final Hardcore hc;

    public PlayerInteract(Hardcore parent)
    {
        this.hc = parent;
    }

    public static Map<Player, Integer> findMap = new HashMap<Player, Integer>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        // TODO: This can become resource intensive and create NullPointers
        Material material = event.getMaterial();
        if (material.equals(Material.COMPASS)) {
            itemCompass(event.getPlayer(), event.getItem(), event.getAction());
        } else if (material.equals(Material.WATCH)) {
            itemWatch(event.getPlayer(), event.getItem(), event.getAction());
        }
    }

    @EventHandler
    public void onPlayerChangeItem(PlayerItemHeldEvent event)
    {
        findMap.remove(event.getPlayer());
    }

    /**
     * {@link Player} uses a compass.
     */
    private void itemCompass(Player player, ItemStack item, Action action)
    {
        List<String> playerList = hc.game().alivePlayers().getPlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        Collections.sort(playerList);

        int currentIndex = 0;
        if (findMap.containsKey(player)) {
            currentIndex = findMap.get(player);
        }

        // Get next player
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (playerList.size() >= currentIndex + 1 && playerList.size() > 1) {
                int nextPlayer = nextPlayer(player, currentIndex, playerList);
                selectPlayer(player, item, hc.getServer().getPlayer(playerList.get(nextPlayer)));
                findMap.put(player, nextPlayer);
            }
        }

        // Get previous player
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            int prevPlayer = prevPlayer(player, currentIndex, playerList);
            selectPlayer(player, item, hc.getServer().getPlayer(playerList.get(prevPlayer)));
            findMap.put(player, prevPlayer);

            // System.out.println("(left not >= 0) currentIndex: "+currentIndex);
        }
    }

    /**
     * {@link Player} uses a watch.
     */
    private void itemWatch(Player player, ItemStack item, Action action)
    {

    }

    private void selectPlayer(Player currentPlayer, ItemStack item, Player facePlayer)
    {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + facePlayer.getName());
        item.setItemMeta(im);
        //currentPlayer.getLocation().setYaw(Utilities.lookAt(currentPlayer.getLocation(), facePlayer.getLocation()).getYaw());
        currentPlayer.setCompassTarget(facePlayer.getLocation());
    }

    /**
     * Get next {@link Player}
     *
     * @return next player
     */
    private int nextPlayer(Player player, int currentIndex, List<String> playerList)
    {
        int newIndex = currentIndex + 1;
        if (playerList.size() > newIndex) {
            if (playerList.get(newIndex).equals(player.getName())) {
                if (playerList.size() > newIndex + 1) {
                    return newIndex + 1;
                }
            } else {
                return newIndex;
            }
        }
        if (playerList.get(0).equals(player.getName())) {
            return 1;
        }
        return 0;
    }

    /**
     * Get previous {@link Player}
     *
     * @return previous player
     */
    private int prevPlayer(Player player, int currentIndex, List<String> playerList)
    {
        int newIndex = currentIndex - 1;
        int size = playerList.size();
        if (newIndex >= 0) {
            if (playerList.get(newIndex).equals(player.getName())) {
                if (newIndex - 1 >= 0) {
                    return newIndex - 1;
                }
            } else {
                return newIndex;
            }
        }
        if (playerList.get(size - 1).equals(player.getName())) {
            return size - 2;
        }
        return size - 1;
    }
}
