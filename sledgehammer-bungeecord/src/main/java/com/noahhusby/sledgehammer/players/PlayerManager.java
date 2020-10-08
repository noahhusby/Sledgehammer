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

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private static PlayerManager mInstance = null;

    public static PlayerManager getInstance() {
        if(mInstance == null) mInstance = new PlayerManager();
        return mInstance;
    }

    List<SledgehammerPlayer> players = new ArrayList<>();

    private PlayerManager() {}

    public void onPlayerJoin(ProxiedPlayer player) {
        onPlayerDisconnect(player);
        players.add(new SledgehammerPlayer(player));
    }

    public void onPlayerDisconnect(ProxiedPlayer player) {
        List<SledgehammerPlayer> remove = new ArrayList<>();
        for(SledgehammerPlayer p : players) {
            if(p.getName().equalsIgnoreCase(player.getName())) {
                remove.add(p);
            }
        }

        for(SledgehammerPlayer p : remove) {
            players.remove(p);
        }
    }

    public List<SledgehammerPlayer> getPlayers() {
        for(SledgehammerPlayer p : players) p.update();
        return players;
    }

    public SledgehammerPlayer getPlayer(String s) {
        for(SledgehammerPlayer p : players) {
            if(p.getName().equalsIgnoreCase(s)) {
                p.update();
                return p;
            }
        }

        return null;
    }

    public SledgehammerPlayer getPlayer(CommandSender s) {
        for(SledgehammerPlayer p : players) {
            if(p.getName().equalsIgnoreCase(s.getName())) {
                p.update();
                return p;
            }
        }

        return null;
    }


}
