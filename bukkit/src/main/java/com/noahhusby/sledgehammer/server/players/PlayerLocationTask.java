package com.noahhusby.sledgehammer.server.players;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.p2s.S2PPlayerUpdatePacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PlayerLocationTask implements Runnable {
    private final Map<UUID, Location> lastLocation = Maps.newHashMap();

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Location last = lastLocation.get(p.getUniqueId());
            if (last == null || !last.equals(p.getLocation())) {
                NetworkHandler.getInstance().send(new S2PPlayerUpdatePacket(p));
            }
            lastLocation.put(p.getUniqueId(), p.getLocation());
        }
    }
}
