/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PlayerLocationHandler.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.players;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.datasets.Point;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;

public class PlayerLocationHandler {
    private static PlayerLocationHandler mInstance = null;

    public static PlayerLocationHandler getInstance() {
        if(mInstance == null) mInstance = new PlayerLocationHandler();
        return mInstance;
    }

    Map<ProxiedPlayer, Point> playerLocations = Maps.newHashMap();
    Map<ProxiedPlayer, Integer> playerRecall = Maps.newHashMap();

    private PlayerLocationHandler() {}

    public void onPlayerJoin(ProxiedPlayer player) {
        playerLocations.remove(player);
    }

    public void onPlayerQuit(ProxiedPlayer player) {
        playerLocations.remove(player);
    }

    public void updateLocation(String sender, Point point) {
        if(false) {
            //if(!ProxyUtil.isServerRegional(ProxyUtil.getServerFromPlayerName(sender))) return;

        }
    }

}
