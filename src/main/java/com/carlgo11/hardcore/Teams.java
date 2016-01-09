package com.carlgo11.hardcore;

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class Teams {

    private final Hardcore hc;
    public Scoreboard sc;
    public ScoreboardManager manager;

    public Teams(Hardcore parent)
    {
        this.hc = parent;
        manager = Bukkit.getScoreboardManager();
        sc = manager.getMainScoreboard();
    }

    public Scoreboard getScoreBoard()
    {
        return sc;
    }

    public GetTeam getTeam(String name)
    {
        return new GetTeam(name);
    }

    public Team registerTeam(String name)
    {
        for (Team teams : sc.getTeams()) {
            if (teams.getName().equalsIgnoreCase(name)) {
                return null;
            }
        }
        Team t = sc.registerNewTeam(name);
        ChatColor teamColorCode = getRandomTeamColor();
        String teamColor;
        if (teamColorCode != null) {
            teamColor = teamColorCode + "";
        } else {
            teamColor = "";
        }
        t.setDisplayName(teamColor + ChatColor.GREEN + name);
        t.setPrefix(teamColor);
        t.setAllowFriendlyFire(hc.getConfig().getBoolean("teams.friendly-fire"));

        return t;
    }

    /**
     * Get a random team-color.
     *
     * @return random team color code; Returns null if no color is available.
     */
    private ChatColor getRandomTeamColor()
    {
        ArrayList<String> teamcolors = new ArrayList<>();
        for (Team teams : sc.getTeams()) {
            teamcolors.add(teams.getPrefix());
        }
        if (teamcolors.size() == (ChatColor.values().length - 6)) {
            return null;
        }
        ArrayList<ChatColor> chatcolors = new ArrayList<>();
        chatcolors.addAll(Arrays.asList(ChatColor.values()));
        ChatColor color = null;
        while (color == null || teamcolors.contains("" + color)) {
            color = chatcolors.get(0 + (int) (Math.random() * (chatcolors.size() - 7)));
        }
        return color;
    }

    /**
     * Check if a player is in a team or not.
     *
     * @param player
     * @return Return the player's current team; Returns null if player isn't in
     * a team.
     */
    public Team inTeam(Player player)
    {
        for (Team team : sc.getTeams()) {
            if (team.hasPlayer(player)) {
                return team;
            }
        }
        return null;
    }

    public class GetTeam {

        private final Team team;

        public GetTeam(String team)
        {
            this.team = sc.getTeam(team);
        }

        public void removePlayer(Player player)
        {
            team.removePlayer(player);
            if (team.getEntries().isEmpty()) {
                sc.getTeams().remove(team);
            }
        }
    }
}
