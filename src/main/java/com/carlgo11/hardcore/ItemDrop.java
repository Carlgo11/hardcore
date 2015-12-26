package com.carlgo11.hardcore;

import com.carlgo11.hardcore.ItemDrop.ItemObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemDrop {

    private final Hardcore hc;

    public ItemDrop(Hardcore parent)
    {
        this.hc = parent;
    }

    public void dropItems()
    {
        ArrayList<Player> players = hc.game().getPlayers();
        for (Player player : players) {
            player.getInventory().addItem(getItem(player));
        }
    }

    /**
     * Return random item to give to a specific player.
     *
     * @TODO Move to external file. (Json, YML, XML, SQL?)
     * @return Random item. returns Minecraft Item ID.
     */
    private ItemStack getItem(Player player)
    {
        ArrayList<List> items = new ArrayList(hc.getConfig().getList("items"));
        int r = new Random().nextInt(items.size());
        ItemObject i = new ItemObject(items.get(r));
        ItemStack itemstack;
        if (i.getData() == 0) {
            itemstack = new ItemStack(i.getMaterial(), i.getSize());
        } else {
            itemstack = new ItemStack(i.getMaterial(), i.getSize(), i.getData());
        }
        return itemstack;
    }

    public class ItemObject {

        private Material material;
        private int size;
        private short data=0;

        public ItemObject(List list){
            this.material=Material.getMaterial(String.valueOf(list.get(0)));
            this.size=(int) list.get(1);
            if(list.size() >= 3){
                int n = (int) list.get(2);
            this.data= n > Short.MAX_VALUE ? Short.MAX_VALUE : n < Short.MIN_VALUE ? Short.MIN_VALUE : (short)n;
            }
        }
        public void setMaterial(Material material)
        {
            this.material = material;
        }

        public Material getMaterial()
        {
            return this.material;
        }

        public void setSize(int size)
        {
            this.size = size;
        }

        public int getSize()
        {
            return this.size;
        }

        public void setData(short data)
        {
            this.data = data;
        }

        public short getData()
        {
            return this.data;
        }
    }
}
