package com.carlgo11.hardcore;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Statics implements Listener {

    private final Hardcore hc;
    private final GetDatabase getdatabase;
    private final Listeners listeners = null;

    public Statics(Hardcore parent, String[] database)
    {
        this.hc = parent;
        this.getdatabase = new GetDatabase(database);
    }

    public GetDatabase getDatabase()
    {
        return getdatabase;
    }

    public class GetDatabase {

        private String[] setup;

        public GetDatabase(String[] setup)
        {

        }

        /**
         * Add a player event to the database.
         *
         * @param event Player event.
         * @exception org.bukkit.plugin.IllegalPluginAccessException if the
         * event is not a Player event.
         */
        public void addPlayerEntry(Event event)
        {
            String eventName = event.getEventName();
            UUID player = null;
            if (event instanceof PlayerEvent) {
                PlayerEvent e = (PlayerEvent) event;
                player = e.getPlayer().getUniqueId();
            }
            if (event instanceof EntityEvent) {
                EntityEvent e = (EntityEvent) event;
                Player p = (Player) e.getEntity();
                player = p.getUniqueId();
            }
        }
    }

    public class Listeners implements Listener {

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e)
        {
            System.out.println(e.getPlayer() + " : " + e.getEventName());
            getDatabase().addPlayerEntry(e);
        }

        @EventHandler
        public void onPlayerDeath(PlayerDeathEvent e)
        {
            getDatabase().addPlayerEntry(e);
        }
    }
}
