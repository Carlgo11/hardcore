package com.carlgo11.hardcore.commands;

import com.carlgo11.hardcore.Hardcore;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandVote implements CommandExecutor {

    private final Hardcore hc;

    public CommandVote(Hardcore parent)
    {
        this.hc = parent;
    }

    private Vote vote;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (sender.hasPermission("hardcore.vote")) {

                    if (args[0].equalsIgnoreCase("kick")) {
                        subCommandKick(sender, cmd, commandLabel, args);
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("start")) {
                        subCommandStart(sender, cmd, commandLabel, args);
                    }
                    if (args[0].equalsIgnoreCase("yes") || args[0].equalsIgnoreCase("no")) {
                        subCommandYesNo(sender, cmd, commandLabel, args);
                        return true;
                    }
                } else {
                    hc.badPermissions(sender);
                    return true;
                }
            }
        } else {
            hc.sendMessage(sender, ChatColor.RED + "Only players can use this command");
            return true;
        }
        return false;
    }

    private void subCommandKick(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (sender.hasPermission("hardcore.vote." + args[0])) {
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]).isOnline()) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (!player.hasPermission("hardcore.vote.unkickable")) {
                        startVoteKick(Bukkit.getPlayer(sender.getName()), player);
                    } else {
                        hc.sendMessage(sender, ChatColor.RED + "Player \"" + player.getName() + "\" is unkickable.");
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
    }

    private void subCommandStart(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (sender.hasPermission("hardcore.vote." + args[0])) {
            vote = new Vote("start", Bukkit.getPlayer(sender.getName()));
            vote.yes.add(Bukkit.getPlayer(sender.getName()));
            hc.broadcastMessage(ChatColor.GOLD + sender.getName() + " started a vote to start the game." + "\nVote /vote " + ChatColor.GREEN + ChatColor.BOLD + "yes" + ChatColor.RESET + ChatColor.GOLD + " or " + ChatColor.RED + ChatColor.BOLD + "no");

        } else {
            hc.badPermissions(sender);
        }
    }

    private void subCommandYesNo(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if (vote != null && vote.voteOn != null) {
            if (sender.hasPermission("hardcore.vote." + args[0])) {
                if (!vote.yes.contains(Bukkit.getPlayer(sender.getName())) && !vote.no.contains(Bukkit.getPlayer(sender.getName()))) {
                    onVote(Bukkit.getPlayer(sender.getName()), args[0]);
                } else {
                    hc.sendMessage(sender, ChatColor.RED + "You've already voted!");
                }
            } else {
                hc.badPermissions(sender);
            }
        } else {
            hc.sendMessage(sender, ChatColor.RED + "There's nothing to vote on yet.");
        }
    }

    /**
     * Add a new vote to an existing vote
     *
     * @param sender Player making the vote
     * @param vote What the vote is on
     */
    private void onVote(Player sender, String castvote)
    {
        if (castvote.equalsIgnoreCase("yes")) {
            vote.yes.add(sender);
            hc.broadcastMessage(ChatColor.GOLD + sender.getName() + " voted " + ChatColor.GREEN + ChatColor.BOLD + castvote.toUpperCase());
        } else if (castvote.equalsIgnoreCase("no")) {
            vote.no.add(sender);
            hc.broadcastMessage(ChatColor.GOLD + sender.getName() + " voted " + ChatColor.RED + ChatColor.BOLD + castvote.toUpperCase());
        }
        if ((vote.yes.size() / 2) > hc.getServer().getOnlinePlayers().size()) {
            vote.executeVote();
        }
    }

    /**
     * Start a vote to kick a player
     *
     * @param sender Player making the kick request.
     * @param player Player voted on being kicked.
     */
    private void startVoteKick(Player sender, Player player)
    {
        vote = new Vote("kick", sender);
        vote.kick = player;
        vote.yes.add(Bukkit.getPlayer(sender.getName()));
        vote.no.add(player);
        hc.broadcastMessage(ChatColor.GOLD + sender.getName() + " started a vote for kicking " + player.getName() + "\nVote /vote " + ChatColor.GREEN + ChatColor.BOLD + "yes" + ChatColor.RESET + ChatColor.GOLD + " or " + ChatColor.RED + ChatColor.BOLD + "no");
    }

    private class Vote {

        private final String voteOn;
        private final Player voteCaster;
        private ArrayList<Player> yes = new ArrayList<>();
        private ArrayList<Player> no = new ArrayList<>();
        private Player kick;

        private Vote(String voteon, Player votecaster)
        {
            this.voteOn = voteon;
            this.voteCaster = votecaster;
        }

        /**
         * Execute the vote. Should only be passed when the vote have passed
         */
        private void executeVote()
        {
            if (voteOn.equals("kick")) {
                kick.kickPlayer(ChatColor.GOLD + "You've been kicked by the other players.");
            }
            if (voteOn.equals("start")) {
                hc.game().startGame();
            }
        }
    }
}
