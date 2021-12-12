/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerConfig.java
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

package com.noahhusby.sledgehammer.proxy.servers;

import com.noahhusby.lib.data.storage.StorageTreeMap;
import com.noahhusby.sledgehammer.common.SledgehammerVersion;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.datasets.Location;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.p2s.P2SInitializationPacket;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServerHandler implements Listener {
    @Getter
    private static final ServerHandler instance = new ServerHandler();

    @Getter
    private final StorageTreeMap<String, SledgehammerServer> servers = new StorageTreeMap<>(String.class, SledgehammerServer.class, String.CASE_INSENSITIVE_ORDER);

    private ServerHandler() {
        Sledgehammer.addListener(this);
    }

    /**
     * Initialize a sledgehammer server
     *
     * @param serverInfo {@link ServerInfo}
     * @param version    Version of initialized server
     */
    public void initialize(ServerInfo serverInfo, SledgehammerVersion version) {
        String name = serverInfo.getName();
        SledgehammerServer s = getServer(name);
        if (s == null) {
            s = new SledgehammerServer(name);
            servers.put(name, s);
            servers.saveAsync();
        }

        if (s.isInitialized()) {
            return;
        }

        s.initialize(version);
    }

    /**
     * Returns a list of Bungeecord servers
     *
     * @return {@link LinkedList<ServerInfo>}
     */
    public LinkedList<ServerInfo> getBungeeServers() {
        LinkedList<ServerInfo> bungeeServers = new LinkedList<>();

        Map<String, ServerInfo> serversTemp = ProxyServer.getInstance().getServers();
        for (Map.Entry<String, ServerInfo> s : serversTemp.entrySet()) {
            bungeeServers.add(s.getValue());
        }

        return bungeeServers;
    }

    /**
     * Creates a server and saves it to storage
     *
     * @param server {@link SledgehammerServer}
     */
    public void addServer(SledgehammerServer server) {
        servers.put(server.getName(), server);
        servers.saveAsync();
    }

    /**
     * Removes a server from memory and storage
     *
     * @param server {@link SledgehammerServer}
     */
    public void removeServer(SledgehammerServer server) {
        servers.remove(server.getName());
        servers.saveAsync();
    }

    /**
     * Gets {@link SledgehammerServer} by name
     *
     * @param name Name of server
     * @return {@link SledgehammerServer}
     */
    public SledgehammerServer getServer(String name) {
        return servers.get(name);
    }

    /**
     * Gets list of {@link Location} from {@link SledgehammerServer}
     *
     * @param server Name of server
     * @return {@link ArrayList<Location>}
     */
    public List<Location> getLocationsFromServer(String server) {
        SledgehammerServer sledgehammerServer = getServer(server);
        return sledgehammerServer == null ? null : sledgehammerServer.getLocations();
    }

    /**
     * Sends initialization packet on join
     *
     * @param e {@link net.md_5.bungee.api.event.ServerConnectedEvent}
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerJoin(ServerConnectedEvent e) {
        NetworkHandler.getInstance().send(new P2SInitializationPacket(e.getPlayer().getName(), e.getServer().getInfo().getName()));
    }
}
