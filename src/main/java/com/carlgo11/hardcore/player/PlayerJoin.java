package com.carlgo11.hardcore.player;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        int state = hc.game().getGameState();
        if (state == 0) {
            hc.sendMessage(player, "Waiting for more players...");
        } else if (state == 2) {
            hc.sendMessage(player, ChatColor.GOLD + "Game starting...");
        } else if (state == 1) {
            if(!player.hasPlayedBefore()){
            hc.game().addPlayer(player);
            player.setGameMode(GameMode.SURVIVAL);
            e.setJoinMessage(ChatColor.GREEN + player.getName() + " joined the game!");
            }else{
                player.setGameMode(GameMode.SPECTATOR);
                e.setJoinMessage(ChatColor.YELLOW + player.getName() + " is now spectating.");
            }
        }
    }
}
