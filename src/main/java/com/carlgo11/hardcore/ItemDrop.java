package com.carlgo11.hardcore;

import com.carlgo11.hardcore.api.Players;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemDrop {

    final Players players;
    private final Hardcore hc;
    List itemlist;

    public ItemDrop(Hardcore parent, Players players) {
        this.hc = parent;
        this.players = players;
        itemlist = YamlConfiguration.loadConfiguration(new File(hc.getDataFolder() + "/items.yml")).getList("items");
    }

    public void dropItems() {
        for (Player player : players.getPlayersAlive()) {
            dropItem(player);
        }
    }

    public void dropItem(Player player) {
        ItemStack item = getItem();
        player.getInventory().addItem(item);
        hc.sendMessage(player, ChatColor.YELLOW + "You got " + item.getAmount() + " " + item.getType().name() + ".");
    }

    /**
     * Return random item to give to a player.
     *
     * @return Random item. returns Minecraft Item ID.
     * @TODO Move to external file. (Maybe JSON, YML, XML or SQL?)
     */
    private ItemStack getItem() {
        ArrayList<List> items = new ArrayList(itemlist);
        int r = new Random().nextInt(items.size());
        ItemObject i = new ItemObject(items.get(r));
        ItemStack itemstack;
        if (i.data == 0) itemstack = new ItemStack(i.material, i.size);
        else itemstack = new ItemStack(i.material, i.size, i.data);
        return itemstack;
    }

    private static class ItemObject {

        private final Material material;
        private final int size;
        private short data;

        private ItemObject(List list) {
            this.material = Material.getMaterial(String.valueOf(list.get(0)));
            this.size = (int) list.get(1);
            if (list.size() >= 3) {
                int n = (int) list.get(2);
                this.data = n > Short.MAX_VALUE ? Short.MAX_VALUE : n < Short.MIN_VALUE ? Short.MIN_VALUE : (short) n;
            }
        }
    }
}
