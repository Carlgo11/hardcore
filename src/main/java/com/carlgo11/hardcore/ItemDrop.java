package com.carlgo11.hardcore;

import com.carlgo11.hardcore.api.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemDrop {

    private final Hardcore hc;
    final Players players;

    public ItemDrop(Hardcore parent, Players players)
    {
        this.hc = parent;
        this.players = players;
    }

    public void dropItems()
    {
        ArrayList<Player> plyers = players.getPlayersAlive();
        for (Player player : players.getPlayersAlive()) {
            ItemStack item = getItem(player);
            player.getInventory().addItem(item);
            hc.sendMessage(player, ChatColor.YELLOW + "You got " + item.getAmount() + " " + item.getType().name() + ".");
        }
    }

    /**
     * Return random item to give to a specific player.
     *
     * @TODO Move to external file. (Maybe JSON, YML, XML or SQL?)
     * @return Random item. returns Minecraft Item ID.
     */
    private ItemStack getItem(Player player)
    {
        YamlConfiguration itemlist = YamlConfiguration.loadConfiguration(new File(hc.getDataFolder() + "/items.yml"));
        ArrayList<List> items = new ArrayList(itemlist.getList("items"));
        int r = new Random().nextInt(items.size());
        ItemObject i = new ItemObject(items.get(r));
        ItemStack itemstack;
        if (i.data == 0) {
            itemstack = new ItemStack(i.material, i.size);
        } else {
            itemstack = new ItemStack(i.material, i.size, i.data);
        }
        return itemstack;
    }

    private class ItemObject {

        private final Material material;
        private final int size;
        private short data;

        private ItemObject(List list)
        {
            this.material = Material.getMaterial(String.valueOf(list.get(0)));
            this.size = (int) list.get(1);
            if (list.size() >= 3) {
                int n = (int) list.get(2);
                this.data = n > Short.MAX_VALUE ? Short.MAX_VALUE : n < Short.MIN_VALUE ? Short.MIN_VALUE : (short) n;
            }
        }
    }
}
