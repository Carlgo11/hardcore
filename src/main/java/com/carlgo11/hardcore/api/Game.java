package com.carlgo11.hardcore.api;

import com.carlgo11.hardcore.Hardcore;
import com.carlgo11.hardcore.api.Players;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class Game {

    private final Hardcore hc;
    private int gamestate; // 0 = warmup, 1 = running, 2 starting, 3 ending
    public double difficulty;
    private double difficultyAddition;
    private int loop;
    public static int minPlayers;

    public Game(Hardcore parent)
    {
        this.hc = parent;
    }

    /**
     * Set the game state to 1 = Running and start the {@link #loop loop}.
     */
    public void startGame()
    {
        difficultyAddition = hc.getConfig().getDouble("difficulty.addition");
        ArrayList<Player> plyrs = new ArrayList<>(Bukkit.getOnlinePlayers());
        hc.players().setPlayers(plyrs);
        if (hc.getConfig().getBoolean("difficulty.peaceful-first-level")) {
            hc.getServer().getWorlds().get(0).setDifficulty(Difficulty.PEACEFUL);
        }
        for (Player p : plyrs) {
            p.setGameMode(GameMode.SURVIVAL);
            p.setFlying(false);
            p.getInventory().clear();
            p.getInventory().addItem(new ItemStack(Material.COMPASS, 1));
            hc.players().resetPlayerHealth(p);
        }
        setGameState(1);
        hc.getServer().getWorlds().get(0).setTime(hc.getConfig().getLong("game.start-time"));
        hc.setEndBorder();
        loop();
    }

    /**
     * Change the difficulty and give {@link #alivePlayers() alive hc.players()}
     * an item every x minutes
     */
    private void loop()
    {
        for (Player p : hc.players().getPlayersAlive()) {
            p.sendTitle(ChatColor.GREEN + "Game started", ChatColor.YELLOW + "Good luck have fun!", 10, 70, 20);
        }
        long time = 20 * 60 * hc.getConfig().getInt("difficulty.delay");
        loop = hc.getServer().getScheduler().scheduleSyncRepeatingTask(hc, () -> {
            if (gamestate == 1) {
                int max = hc.getConfig().getInt("difficulty.max");
                if (max == -1 || difficulty <= max) {
                    hc.itemDrop();
                    if (getDifficulty() == 0) {
                        difficulty = hc.getConfig().getDouble("difficulty.start-difficulty");
                        hc.broadcastMessage(ChatColor.GOLD + "Next difficulty: x" + String.format("%.2f", getDifficulty()));
                    } else {
                        if (getDifficulty() == 1) {
                            hc.getServer().getWorlds().get(0).setDifficulty(Difficulty.HARD);
                        }
                        nextDifficulty();
                        hc.broadcastMessage(ChatColor.GOLD + "Next difficulty: x" + String.format("%.2f", getDifficulty()));
                    }
                }
            } else {
                Bukkit.getScheduler().cancelTask(loop);
            }
        }, 20L, time);
    }

    /**
     * Change the difficulty to the next level
     */
    private void nextDifficulty()
    {
        difficulty = difficulty * difficultyAddition;
    }

    /**
     * Stop the game, kick all hc.players() and stop the server.
     */
    public void stopGame()
    {
        setGameState(3);
        Bukkit.getScheduler().cancelTask(loop);
        Bukkit.getScheduler().scheduleSyncDelayedTask(hc, () -> {
            Bukkit.getOnlinePlayers().stream().forEach((p) -> {
                p.kickPlayer("Game restarting...");
            });
            Bukkit.getServer().shutdown();
        }, (hc.getConfig().getInt("game.end-delay") * 20));
    }

    /**
     * Returns current game difficulty
     *
     * @return Current difficulty in int value
     * @see #difficulty
     */
    public double getDifficulty()
    {
        return this.difficulty;
    }

    /**
     * Get the current game state.
     *
     * @return Current game state. 0 = Warmup, 1 = Running, 2 = Starting, 3 =
     * Ending
     */
    public int getGameState()
    {
        return gamestate;
    }

    /**
     * Set the current game state.
     *
     * @param newstate The new game state
     */
    public void setGameState(int newstate)
    {
        gamestate = newstate;

    }

    public void getGameEndMessage(ArrayList<Player> players, Player player)
    {

        if (players.size() >= 1) {
            String aliveplayers = null;
            for (Player p : players) {
                if (aliveplayers == null) {
                    aliveplayers += p.getName();
                } else {
                    aliveplayers += ", " + p.getName();
                }
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
    private void spawnFirework(ArrayList<Player> players)
    {
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
