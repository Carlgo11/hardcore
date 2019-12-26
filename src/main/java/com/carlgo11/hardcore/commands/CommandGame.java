package com.carlgo11.hardcore.commands;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.api.Game;
import com.carlgo11.hardcore.api.Players;
import com.carlgo11.report.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGame implements CommandExecutor {

    private final Hardcore hc;
    final Game game;
    final Players players;

    public CommandGame(Hardcore parent, Game game, Players players) {
        this.hc = parent;
        this.game = game;
        this.players = players;
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
        }else if (args[0].equalsIgnoreCase("report")){
            return subCommandReport(sender, args, commandLabel, hc);
        }
        return false;
    }

    private boolean subCommandReport(CommandSender sender, String[] args, String commandLabel, Hardcore hc) {
        try {
            Report report = new Report(hc, "44407136b4a38be15b60a97da7ecc9d7", "174314164f147aa4a66d9608644139c2");
            String URL = report.makeReport();
            hc.sendMessage(sender, ChatColor.GREEN + "Include this link in your bug report: " + ChatColor.BLUE + URL);
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    private boolean subCommandStart(CommandSender sender, String[] args) {
        if (sender.hasPermission("hardcore.game." + args[0])) {
            game.startGame();
        } else {
            hc.badPermissions(sender);
        }
        return true;
    }

    private boolean subCommandEnd(CommandSender sender, String[] args) {
        if (sender.hasPermission("hardcore.game." + args[0])) {
            game.stopGame();
            hc.broadcastMessage(ChatColor.YELLOW + sender.getName() + " stopped the game.");
        } else {
            hc.badPermissions(sender);
        }
        return true;
    }

    private boolean subCommandDebug(CommandSender sender) {
        if (sender.hasPermission("hardcore.game.debug")) {
            hc.sendMessage(sender, "\n----- Debug Info -----\n\nGamestate: " + game.getGameState() + "\nPlayers alive:" + players.getPlayersAlive().size() + "\nDifficulty: " + game.getDifficulty());
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
                    if (!players.getPlayersAlive().contains(player)) {
                        players.addPlayer(player);
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
                    if (players.getPlayersAlive().contains(player)) {
                        players.removePlayer(player);
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
}
