package com.carlgo11.hardcore.server;

import com.carlgo11.hardcore.Hardcore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPing implements Listener {

    private Hardcore hc;

    public ServerPing(Hardcore plug)
    {
        this.hc = plug;
    }

    @EventHandler
    public void onMOTD(ServerListPingEvent event)
    {
        /*  if (hc.first == true) {
         if (hc.starting) {
         event.setMotd(event.getMotd() + ChatColor.GOLD + "Starting");
         } else {
         event.setMotd(event.getMotd() + ChatColor.GREEN + "Warmup");
         }
         } else if (hc.ending) {
         event.setMotd(event.getMotd() + ChatColor.DARK_RED + "Ending");
         } else {
         event.setMotd(event.getMotd() + ChatColor.RED + "In Game");
         }
         */
    }

}
