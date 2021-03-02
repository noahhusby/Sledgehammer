/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GUIHandler.java
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
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.proxy.gui;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;

import java.util.Map;

public class GUIHandler {
    private static GUIHandler instance = null;

    public static GUIHandler getInstance() {
        return instance == null ? instance = new GUIHandler() : instance;
    }

    private final Map<SledgehammerPlayer, String> guiTrackers = Maps.newHashMap();

    /**
     * Track a new player with random code
     *
     * @param player {@link SledgehammerPlayer}
     * @return Salt code
     */
    public String track(SledgehammerPlayer player) {
        String salt = SledgehammerUtil.getSaltString();
        guiTrackers.put(player, salt);
        return salt;
    }

    /**
     * Check whether an incoming request is valid
     *
     * @param player {@link SledgehammerPlayer}
     * @param salt   Salt code
     * @return True if valid, false if not
     */
    public boolean validateRequest(SledgehammerPlayer player, String salt) {
        String storedSalt = guiTrackers.get(player);
        if (storedSalt == null) {
            return false;
        }
        return storedSalt.equals(salt);
    }

    /**
     * Removes all trackers from a player
     *
     * @param player {@link SledgehammerPlayer}
     */
    public void invalidate(SledgehammerPlayer player) {
        guiTrackers.remove(player);
    }
}
