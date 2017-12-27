package com.carlgo11.hardcore.objects;

import com.carlgo11.hardcore.Game;
import com.carlgo11.hardcore.Hardcore;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class Players {

    private final Hardcore hc;

    private ArrayList<Player> playersAlive = new ArrayList<>();
    private ArrayList<Player> playersSpectating = new ArrayList<>();

    public Players(Hardcore parent)
    {
        this.hc = parent;
    }

    /**
     * Get the players that are currently alive.
     *
     * @return The alive players. If non are alive then null.
     */
    public ArrayList<Player> getPlayersAlive()
    {
        return playersAlive;
    }

    public ArrayList<Player> getPlayersSpectating()
    {
        return playersSpectating;
    }

    public boolean playerIsAlive(Player player)
    {
        return this.getPlayersAlive().contains(player);
    }

    /**
     * Set alive players. Should not be used for adding or removing a single
     * player!
     *
     * @param aliveplayers Alive players
     */
    public void setPlayers(ArrayList<Player> aliveplayers)
    {
        playersAlive = aliveplayers;
    }

    /**
     * Remove a single player from the game.
     *
     * @param player Player to remove from the game
     */
    public void removePlayer(Player player)
    {
        if (this.playerIsAlive(player)) {
            player.setGameMode(GameMode.SPECTATOR);
            ArrayList<Player> plyrs = getPlayersAlive();
            plyrs.remove(player);
            Team team = hc.teams.inTeam(player);
            if (team != null) {
                hc.teams.getTeam(team.getName()).removePlayer(player);
            }
            setPlayers(plyrs);
            if (plyrs.size() > Game.minPlayers) {
                for (Team team2 : hc.teams.sc.getTeams()) {
                    if (team2.getPlayers().equals(plyrs)) {
                        hc.game().getGameEndMessage(plyrs, player);
                        hc.game().stopGame();
                        return;
                    }
                }
                hc.broadcastMessage(ChatColor.YELLOW + "Only " + plyrs.size() + " players left!");
            } else if (hc.game().getGameState() == 1) {
                hc.game().getGameEndMessage(plyrs, player);
                hc.game().stopGame();
            }
        }
    }

    /**
     * Add a new player to the game.
     *
     * @param player New player
     */
    public void addPlayer(Player player)
    {
        ArrayList<Player> plyrs = getPlayersAlive();
        plyrs.add(player);
        this.setPlayers(plyrs);
        if (hc.game().getGameState() == 1) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    /**
     * A player might sometimes not have max health when the game starts. This
     * function resets all possible health parameters.
     *
     * @param player Player to reset health to
     */
    public void resetPlayerHealth(Player player)
    {
        player.setHealth(player.getMaxHealth()); //Player might have extra lifes due to custom server settings.
        player.setFoodLevel(20);
    }

}
