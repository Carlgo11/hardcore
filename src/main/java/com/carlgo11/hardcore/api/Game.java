package com.carlgo11.hardcore.api;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.gamestate.GameState;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    public static int minPlayers;
    private final Hardcore hc;
    public double difficulty;
    private GameState gamestate = GameState.Warmup;
    private double difficultyAddition;
    private int loop;

    public Game(Hardcore parent) {
        this.hc = parent;
    }

    /**
     * Set the game state to Running and start the {@link #loop loop}.
     */
    public void startGame() {
        difficultyAddition = hc.getConfig().getDouble("difficulty.addition");
        ArrayList<Player> plyrs = new ArrayList<>(Bukkit.getOnlinePlayers());
        hc.players().setPlayers(plyrs);
        if (hc.getConfig().getBoolean("difficulty.peaceful-first-level"))
            hc.getServer().getWorlds().get(0).setDifficulty(Difficulty.PEACEFUL);

        for (Player p : plyrs) {
            hc.broadcastMessage(p.getName());
            p.setGameMode(GameMode.SURVIVAL);
            p.setFlying(false);
            p.getInventory().clear();
            p.getInventory().addItem(new ItemStack(Material.COMPASS, 1));
            hc.players().resetPlayerHealth(p);
            if (!hc.players().getPlayersAlive().contains(p)) hc.players().addPlayer(p);
        }
        setGameState(GameState.Running);
        if (hc.getConfig().getBoolean("difficulty.peaceful-first-level"))
            hc.getServer().getWorlds().get(0).setDifficulty(Difficulty.PEACEFUL);
        hc.getServer().getWorlds().get(0).setTime(hc.getConfig().getLong("game.start-time"));
        hc.setEndBorder();

        loop();
    }

    /**
     * Change the difficulty and give alive hc.players()
     * an item every x minutes
     */
    private void loop() {
        for (Player p : hc.players().getPlayersAlive())
            p.sendTitle(ChatColor.GREEN + "Game started", ChatColor.YELLOW + "Good luck have fun!", 10, 70, 20);

        long time = 20 * 60 * hc.getConfig().getInt("difficulty.delay");
        loop = hc.getServer().getScheduler().scheduleSyncRepeatingTask(hc, () -> {

            if (getGameState() == GameState.Running) {
                int max = hc.getConfig().getInt("difficulty.max");
                if (max == -1 || difficulty <= max) {
                    hc.dropItems();
                    if (getDifficulty() == 0) difficulty = hc.getConfig().getDouble("difficulty.start-difficulty");
                    else {
                        if (getDifficulty() == 1) hc.getServer().getWorlds().get(0).setDifficulty(Difficulty.HARD);
                        nextDifficulty();
                    }
                    hc.broadcastMessage(ChatColor.GOLD + "Next difficulty: x" + String.format("%.2f", getDifficulty()));
                }
            } else Bukkit.getScheduler().cancelTask(loop);
        }, 20L, time);
    }

    /**
     * Change the difficulty to the next level
     */
    private void nextDifficulty() {
        difficulty = difficulty * difficultyAddition;
    }

    /**
     * Stop the game, kick all hc.players() and stop the server.
     */
    public void stopGame() {
        setGameState(GameState.Ending);
        Bukkit.getScheduler().cancelTask(loop);
        Bukkit.getScheduler().scheduleSyncDelayedTask(hc, () -> {
            Bukkit.getOnlinePlayers().forEach((p) -> p.kickPlayer("Game restarting..."));
            Bukkit.getServer().shutdown();
        }, (hc.getConfig().getInt("game.end-delay") * 20));
    }

    /**
     * Returns current game difficulty
     *
     * @return Current difficulty in int value
     * @see #difficulty
     */
    public double getDifficulty() {
        return this.difficulty;
    }

    /**
     * Get the current game state.
     *
     * @return Current game state. 0 = Warmup, 1 = Running, 2 = Starting, 3 =
     * Ending
     */
    public GameState getGameState() {
        return gamestate;
    }

    /**
     * Set the current game state.
     *
     * @param newstate The new game state
     */
    public void setGameState(GameState newstate) {
        gamestate = newstate;
    }

    public void getGameEndMessage(ArrayList<Player> players) {

        if (players.size() >= 1) {
            String aliveplayers = null;
            for (Player p : players) {
                if (aliveplayers == null) aliveplayers += p.getName();
                else aliveplayers += ", " + p.getName();

                Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();
            }
            spawnFirework(players);
            hc.broadcastMessage("" + ChatColor.GREEN + ChatColor.BOLD + "GAME ENDED! The Winners are " + aliveplayers + "!");
        } else {
            spawnFirework(players);
            hc.broadcastMessage("" + ChatColor.RED + ChatColor.BOLD + "GAME ENDED! No one wins :(");
        }
    }

    /**
     * Spawn a firework entity on the player(s)'s location
     *
     * @param players Array of players to spawn firework on.
     */
    private void spawnFirework(ArrayList<Player> players) {
        for (Player player : players) {
            Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();
            Random r = new Random();
            Type[] types = Type.values();
            int type = r.nextInt(types.length);
            Color c1 = Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            Color c2 = Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(types[type]).withTrail().build();
            fwm.addEffect(effect);
            fwm.setPower(0);
            fw.setFireworkMeta(fwm);
        }
    }

}
