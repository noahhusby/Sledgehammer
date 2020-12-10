/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PlayerManager.java
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

package com.noahhusby.sledgehammer.players;

import com.google.common.collect.Sets;
import com.noahhusby.lib.data.storage.StorageHashMap;
import com.noahhusby.lib.data.storage.StorageList;
import com.noahhusby.sledgehammer.Sledgehammer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;

public class PlayerManager implements Listener {
    private static PlayerManager mInstance = null;

    public static PlayerManager getInstance() {
        if(mInstance == null) mInstance = new PlayerManager();
        return mInstance;
    }

    List<SledgehammerPlayer> players = new ArrayList<>();
    StorageList<Attribute> attributes = new StorageList<>(Attribute.class);

    private PlayerManager() {
        Sledgehammer.addListener(this);
    }

    /**
     * Creates a new SledgehammerPlayer and sets attributes from storage upon player joining
     * @param e {@link PostLoginEvent}
     */
    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        onPlayerDisconnect(new PlayerDisconnectEvent(player));
        SledgehammerPlayer newPlayer = new SledgehammerPlayer(player);

        Attribute attribute = null;
        for(Attribute a : attributes)
            if(a.getUuid().toString().equals(player.getUniqueId().toString())) attribute = a;

        if(attribute != null) {
            newPlayer.setAttributes(attribute.getAttributes());
        }

        players.add(newPlayer);
    }

    /**
     * Removes the SledgehammerPlayer and saves the attributes to storage upon player leaving
     * @param e {@link PlayerDisconnectEvent}
     */
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        List<SledgehammerPlayer> remove = new ArrayList<>();
        for(SledgehammerPlayer p : players) {
            if(p.getName().equalsIgnoreCase(player.getName())) {
                remove.add(p);
            }
        }

        if(!remove.isEmpty()) {
            SledgehammerPlayer p = remove.get(0);

            Attribute attribute = null;
            for(Attribute a : attributes)
                if(a.getUuid().toString().equals(player.getUniqueId().toString())) attribute = a;

            if(attribute != null) {
                attribute.getAttributes().clear();
                attribute.getAttributes().addAll(p.getAttributes());
            } else {
                Attribute a = new Attribute(player.getUniqueId(), p.getAttributes());
                attributes.add(a);
            }

            attributes.save(true);
        }

        for(SledgehammerPlayer pl : remove) {
            players.remove(pl);
        }
    }

    /**
     * Gets the list of Sledgehammer players
     * @return List of Sledgehammer players
     */
    public List<SledgehammerPlayer> getPlayers() {
        for(SledgehammerPlayer p : players) p.update();
        return players;
    }

    /**
     * Gets SledgehammerPlayer by player name
     * @param s Player name
     * @return {@link SledgehammerPlayer}
     */
    public SledgehammerPlayer getPlayer(String s) {
        for(SledgehammerPlayer p : players) {
            if(p.getName().equalsIgnoreCase(s)) {
                p.update();
                return p;
            }
        }

        return null;
    }

    /**
     * Gets SledgehammerPlayer by command sender
     * @param s {@link CommandSender}
     * @return {@link SledgehammerPlayer}
     */
    public SledgehammerPlayer getPlayer(CommandSender s) {
        for(SledgehammerPlayer p : players) {
            if(p.getName().equalsIgnoreCase(s.getName())) {
                p.update();
                return p;
            }
        }

        return null;
    }

    /**
     * Gets the hash map of player attributes
     * Use {@link SledgehammerPlayer#getAttributes()} to get attributes
     * @return Hash map of attributes
     */
    public StorageList<Attribute> getAttributes() {
        return attributes;
    }
}
