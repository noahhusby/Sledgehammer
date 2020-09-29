/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PlayerLocationHandler.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
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
