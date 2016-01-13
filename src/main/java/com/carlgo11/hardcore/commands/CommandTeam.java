package com.carlgo11.hardcore.commands;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class CommandTeam implements CommandExecutor {

    private final Hardcore hc;
    private final Teams teams;

    public CommandTeam(Hardcore parent)
    {
        this.hc = parent;
        this.teams = hc.teams;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("new")) {
                subCommandNew(sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("add")) {
                subCommandAdd(sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("accept")) {
                subCommandAccept(sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")) {
                subCommandLeave(sender, args);
                return true;
            }
        }
        return false;
    }

    private void subCommandNew(CommandSender sender, String[] args)
    {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            if (sender.hasPermission("hardcore.team." + args[0])) {
                Team team = hc.teams.registerTeam(args[1]);
                if (team != null) {
                    team.addPlayer(player);
                    hc.sendMessage(sender, ChatColor.GREEN + "Team \"" + args[1] + ChatColor.GREEN + "\" registered!");
                } else {
                    hc.sendMessage(sender, ChatColor.RED + "A team with that name already exists.");
                }
            } else {
                hc.badPermissions(sender);
            }
        } else {
            hc.sendMessage(sender, ChatColor.RED + "Only players can perform this command.");
        }
    }

    private void subCommandAdd(CommandSender sender, String[] args)
    {
        if (sender.hasPermission("hardcore.team." + args[0])) {
            if (args.length == 2) {
                Team team = hc.teams.inTeam(Bukkit.getPlayer(sender.getName()));
                if (team != null) {
                    Player invitee = Bukkit.getPlayer(args[1]);
                    if (invitee != null) {
                        teams.getTeam(team.getName()).addInvite(invitee);
                        hc.sendMessage(sender, ChatColor.GREEN + "You've invited " + args[1]);
                    } else {
                        hc.sendMessage(sender, ChatColor.RED + "Can't find player \"" + args[1] + "\"");
                    }
                } else {
                    hc.sendMessage(sender, ChatColor.RED + "You're not in a team.");
                }
            } else {

            }
        } else {
            hc.badPermissions(sender);
        }
    }

    private void subCommandLeave(CommandSender sender, String[] args)
    {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            if (sender.hasPermission("hardcore.team." + args[0])) {
                Team team = teams.inTeam(player);
                if (team != null) {
                    teams.getTeam(team.getName()).removePlayer(player);
                    hc.sendMessage(sender, ChatColor.GREEN + "You've left \"" + team.getDisplayName() + ChatColor.GREEN + "\"");
                } else {
                    hc.sendMessage(sender, ChatColor.RED + "You're not in a team.");
                }
            } else {
                hc.badPermissions(sender);
            }
        } else {
            hc.sendMessage(sender, ChatColor.RED + "Only players can perform this command.");
        }
    }

    private void subCommandAccept(CommandSender sender, String[] args)
    {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            Team team = teams.isInvited(player);
            if (team != null) {
                team.addPlayer(player);
                hc.sendMessage(sender, ChatColor.GREEN + "You've joined \"" + team.getDisplayName() + ChatColor.GREEN + "\"");
            } else {
                hc.sendMessage(sender, ChatColor.RED + "You're not invited to any team.");
            }
        } else {
            hc.sendMessage(sender, ChatColor.RED + "Only players can perform this command.");
        }
    }

    private boolean isPlayer(CommandSender sender)
    {
        return sender instanceof Player;
    }
}
