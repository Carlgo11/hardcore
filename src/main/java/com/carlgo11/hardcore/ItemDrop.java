package com.carlgo11.hardcore;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ItemDrop {

    private final Hardcore hc;

    public ItemDrop(Hardcore plug)
    {
        this.hc = plug;
    }

    public void dropItems()
    {
        ArrayList<Player> players = hc.game().getPlayers();
        for (Player player : players) {
            String cmd = "give " + player.getName() + " " + getItem();
            hc.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    private String getItem()
    {
        String[] items = {"4 32", "5 20", "50 10", "297 5", "264 1", "264 2", "322 1", "322 1 1", "323 1", "332 10", "354 1", "368 1", "391 4", "393 1", "392 1", "319 1", "320 1", "261 1", "262 5"};
        int r = new Random().nextInt(items.length);
        return items[r];
    }

}
