package com.carlgo11.hardcore.commands;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandItemDrop implements CommandExecutor {
    Hardcore hc;

    public CommandItemDrop(Hardcore hc) {
        this.hc = hc;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            int needed_xp = hc.getConfig().getInt("itemdrops.needed-xp");
            if (player.getLevel() >= needed_xp) {
                hc.itemDrop().dropItem(player);
                player.setLevel(player.getLevel() - needed_xp);
            } else hc.sendMessage(sender, "You need more XP to call in an item drop.");
        } else hc.sendMessage(sender, "Command can only be used by players.");
        return true;
    }
}
