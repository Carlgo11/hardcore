package com.carlgo11.hardcore.commands;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGame implements CommandExecutor {

    private final Hardcore hc;

    public CommandGame(Hardcore parent) {
        this.hc = parent;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args[0].equalsIgnoreCase("start")) {
            return subCommandStart(sender, args);
        } else if (args[0].equalsIgnoreCase("end")) {
            return subCommandEnd(sender, args);
        } else if (args[0].equalsIgnoreCase("debug") || args[0].equalsIgnoreCase("info")) {
            return subCommandDebug(sender);
        } else if (args[0].equalsIgnoreCase("add")) {
            return subCommandAdd(sender, args, commandLabel);
        } else if (args[0].equalsIgnoreCase("remove")) {
            return subCommandRemove(sender, args, commandLabel);
        } else if (args[0].equalsIgnoreCase("set")) {
            return subCommandSet(sender, args, commandLabel);
        }
        return false;
    }

    {
    private boolean subCommandStart(CommandSender sender, String[] args) {
        if (sender.hasPermission("hardcore.game." + args[0])) {
            hc.game().startGame();
        } else {
            hc.badPermissions(sender);
        }
        return true;
    }

    private boolean subCommandEnd(CommandSender sender, String[] args) {
        if (sender.hasPermission("hardcore.game." + args[0])) {
            hc.game().stopGame();
            hc.broadcastMessage(ChatColor.YELLOW + sender.getName() + " stopped the game.");
        } else {
            hc.badPermissions(sender);
        }
        return true;
    }

    private boolean subCommandDebug(CommandSender sender) {
        if (sender.hasPermission("hardcore.game.debug")) {
            hc.sendMessage(sender, "\n----- Debug Info -----\n\nGamestate: " + hc.game().getGameState() + "\nPlayers alive:" +hc.players().getPlayersAlive().size() + "\nDifficulty: " + hc.game().getDifficulty());
        } else {
            hc.badPermissions(sender);
        }
        return true;
    }

    private boolean subCommandAdd(CommandSender sender, String[] args, String commandLabel) {

        if (sender.hasPermission("hardcore.game." + args[0])) {
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (!hc.players().getPlayersAlive().contains(player)) {
                        hc.players().addPlayer(player);
                        hc.broadcastMessage(ChatColor.GREEN + sender.getName() + " added " + player.getName());
                    } else {
                        hc.sendMessage(sender, ChatColor.RED + "Player is already alive");
                    }
                } else {
                    hc.sendMessage(sender, ChatColor.RED + "Player \"" + args[1] + "\" not found.");
                }
            } else {
                hc.sendMessage(sender, ChatColor.WHITE + "Usage: /" + commandLabel + " " + args[0] + " <player>");
            }
        } else {
            hc.badPermissions(sender);
        }
        return true;
    }

    private boolean subCommandRemove(CommandSender sender, String[] args, String commandLabel) {
        if (sender.hasPermission("hardcore.game." + args[0])) {
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (hc.players().getPlayersAlive().contains(player)) {
                        hc.players().removePlayer(player);
                        hc.broadcastMessage(ChatColor.YELLOW + sender.getName() + " removed " + player.getName());
                    } else {
                        hc.sendMessage(sender, ChatColor.RED + "Player is not alive");
                    }
                } else {
                    hc.sendMessage(sender, ChatColor.RED + "Player \"" + args[1] + "\" not found.");
                }
            } else {
                hc.sendMessage(sender, ChatColor.WHITE + "Usage: /" + commandLabel + " " + args[0] + " <player>");
            }
        } else {
            hc.badPermissions(sender);
        }
        return true;
    }

    private boolean subCommandSet(CommandSender sender, String[] args, String commandLabel) {
        if (args[1].equalsIgnoreCase("teamglow")) {
            if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                hc.getServer().getOnlinePlayers().forEach((p) -> {
                    p.setGlowing(Boolean.valueOf(args[2]));
                });
            }
        }
        return true;
    }
}
