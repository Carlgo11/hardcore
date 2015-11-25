package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private Hardcore hc;

    public PlayerJoin(Hardcore parent)
    {
        this.hc = parent;
    }

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        if (hc.game().getGameState() == 0) {
            hc.sendMessage(player, "Waiting for more players...");
        } else if (hc.game().getGameState() == 2) {
            hc.sendMessage(player, ChatColor.GOLD + "Game starting...");
        }
    }
}
