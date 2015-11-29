package com.carlgo11.hardcore.server;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPing implements Listener {

    private Hardcore hc;

    public ServerPing(Hardcore parent)
    {
        this.hc = parent;
    }

    @EventHandler
    public void onMOTD(ServerListPingEvent event)
    {
        int state = hc.game().getGameState();
        String motd = event.getMotd()+"";
        if (state == 0) {
            event.setMotd(motd + ChatColor.GREEN + "Warmup...");
        } else if (state == 1) {
             event.setMotd(motd + ChatColor.RED + "Running...");
        } else if (state == 2) {
            event.setMotd(motd + ChatColor.GOLD + "Starting...");
        } else if (state == 3){
            event.setMotd(motd + ChatColor.YELLOW + "Ending...");
        }
    }
}
