package com.carlgo11.hardcore.commands;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandGame implements CommandExecutor {

    private Hardcore hc;

    public CommandGame(Hardcore parent)
    {
        this.hc = parent;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("start")) {
                if (sender.hasPermission("hardcore.game.start")) {
                    hc.game().startGame();
                } else {
                    hc.badPermissions(sender);
                }
            } else if (args[0].equalsIgnoreCase("end")) {
                if (sender.hasPermission("hardcore.game.end")) {
                    hc.game().stopGame();
                } else {
                    hc.badPermissions(sender);
                }
            }
        }
        return false;
    }
}
