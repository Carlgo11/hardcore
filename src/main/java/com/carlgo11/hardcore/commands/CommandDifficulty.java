package com.carlgo11.hardcore.commands;

import com.carlgo11.hardcore.Hardcore;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandDifficulty implements CommandExecutor {

    private Hardcore hc;

    public CommandDifficulty(Hardcore parent)
    {
        this.hc = parent;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (sender.hasPermission("Hardcore.difficulty")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("get")) {
                    return subCommandGet(sender, cmd, commandLabel, args);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    return subCommandSet(sender, cmd, commandLabel, args);
                }
            }
        } else {
            hc.badPermissions(sender);
            return true;
        }
        return false;
    }

    private boolean subCommandGet(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (sender.hasPermission("Hardcore.difficulty.get")) {
            sender.sendMessage(ChatColor.GREEN + "Difficulty: " + hc.game().getDifficulty());
            return true;
        } else {
            hc.badPermissions(sender);
            return true;
        }
    }

    private boolean subCommandSet(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (sender.hasPermission("Hardcore.difficulty.set")) {
            if (StringUtils.isNumeric(args[1])) {
                hc.game().difficulty = Integer.valueOf(args[1]);
                hc.sendMessage(sender, ChatColor.GREEN + "Difficulty set to: " + hc.game().getDifficulty());
                return true;
            }
        } else {
            hc.badPermissions(sender);
            return true;
        }
        return false;
    }
}
