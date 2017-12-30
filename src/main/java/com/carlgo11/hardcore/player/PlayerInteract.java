package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.api.*;
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
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteract implements Listener {

    private final Hardcore hc;
    final Players players;

    public PlayerInteract(Hardcore parent, Players players)
    {
        this.hc = parent;
        this.players = players;
    }

    public static Map<Player, Integer> findMap = new HashMap<>();
    public static Map<Player, Integer> wandability = new HashMap<>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        // TODO: This can become resource intensive and create NullPointers
        Material material = event.getMaterial();
        if (material.equals(Material.COMPASS)) {
            itemCompass(event.getPlayer(), event.getItem(), event.getAction());
        } else if (material.equals(Material.STICK)) {
            itemWand(event.getPlayer(), event.getAction());
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
        List<String> playerList = players.getPlayersAlive()
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

        }
    }

    private int getPlayerWandAbility(Player player)
    {
        if (wandability.containsKey(player)) {
            return wandability.get(player);
        } else {
            return -1;
        }
    }

    public static void setPlayerWandAbility(Player player, int i)
    {
        if (wandability.containsKey(player)) {
            if (wandability.get(player) == -1) {
                wandability.remove(player);
            } else {
                wandability.replace(player, i);
            }
        } else {
            wandability.put(player, i);
        }
    }

    /**
     * {@link Player} uses a wand.
     *
     * @param player {@link Player} who used the wand.
     * @param action Action taken.
     */
    private void itemWand(Player player, Action action)
    {
        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
            switch (getPlayerWandAbility(player)) {
                case 0:
                    Arrow arrow = player.launchProjectile(Arrow.class);
                    arrow.setBounce(false);
                    arrow.setGravity(false);
                    arrow.setKnockbackStrength(100);
                    arrow.setCritical(true);
                    break;
                case 1:
                    Block block = player.getTargetBlock(null, 100);
                    Location b = block.getLocation();
                    b.getWorld().createExplosion(b, 10);
                    break;
                default:
                    player.openInventory(PlayerInventoryClick.wandInventory);
                    break;
            }
        } else {
            player.openInventory(PlayerInventoryClick.wandInventory);
        }
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
     * Get next {@link Player} to point compass to.
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
