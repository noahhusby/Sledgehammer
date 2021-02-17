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

package com.noahhusby.sledgehammer.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.noahhusby.lib.data.storage.StorageList;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.network.P2S.P2SInitializationPacket;
import com.noahhusby.sledgehammer.network.NetworkHandler;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerHandler implements Listener {
    private static ServerHandler instance;

    public static ServerHandler getInstance() {
        return instance == null ? instance = new ServerHandler() : instance;
    }

    @Getter private Map<String, SledgehammerServer> serverCache = Maps.newHashMap();

    @Getter private final StorageList<SledgehammerServer> servers = new StorageList<>(SledgehammerServer.class);
    @Getter private final StorageList<ServerGroup> groups = new StorageList<>(ServerGroup.class);
    private final Map<String, String> initialized = Maps.newHashMap();

    private ServerHandler() {
        Sledgehammer.addListener(this);
        Sledgehammer.sledgehammer.getGeneralThreads().scheduleAtFixedRate(this::updateCache, 0, 5, TimeUnit.SECONDS);
    }

    /**
     * Initialize a sledgehammer server
     * @param serverInfo {@link ServerInfo}
     * @param data Incoming data from init packet
     */
    public void initialize(ServerInfo serverInfo, JSONObject data) {
        String version = (String) data.get("version");
        String name = serverInfo.getName();

        SledgehammerServer s = getServer(name);
        if(s == null) {
            s = new SledgehammerServer(name);
            servers.add(s);
            servers.save(true);
        }

        if(s.isInitialized()) return;

        s.initialize(version);
        initialized.put(s.getName(), version);
    }

    /**
     * Returns a list of Bungeecord servers
     * @return {@link LinkedList<ServerInfo>}
     */
    public LinkedList<ServerInfo> getBungeeServers() {
        LinkedList<ServerInfo> bungeeServers = new LinkedList<>();

        Map<String, ServerInfo> serversTemp = ProxyServer.getInstance().getServers();
        for(Map.Entry<String, ServerInfo> s : serversTemp.entrySet()) {
            bungeeServers.add(s.getValue());
        }

        return bungeeServers;
    }

    /**
     * Updates and saves {@link SledgehammerServer} to storage
     * @param server {@link SledgehammerServer}
     */
    public void pushServer(SledgehammerServer server) {
        servers.removeIf(s -> s.getName().equalsIgnoreCase(server.getName()));
        servers.add(server);
        servers.save();
    }

    /**
     * Removes a server from memory and storage
     * @param server {@link SledgehammerServer}
     */
    public void removeServer(SledgehammerServer server) {
        servers.remove(server);
        servers.save();
    }

    /**
     * Gets {@link SledgehammerServer} by name
     * @param name Name of server
     * @return {@link SledgehammerServer}
     */
    public SledgehammerServer getServer(String name) {
        return serverCache.get(name);
    }

    /**
     * Gets list of {@link Location} from {@link SledgehammerServer}
     * @param server Name of server
     * @return {@link ArrayList<Location>}
     */
    public List<Location> getLocationsFromServer(String server) {
        SledgehammerServer sledgehammerServer = serverCache.get(server);
        return sledgehammerServer == null ? null : sledgehammerServer.getLocations();
    }

    /**
     * Gets map of initialized servers with SH versions
     * @return Map of initialized servers
     */
    public Map<String, String> getInitializedMap() {
        return initialized;
    }

    private void updateCache() {
        Map<String, SledgehammerServer> tempServerCache = Maps.newHashMap();
        for(SledgehammerServer s : servers) {
            tempServerCache.put(s.getName(), s);
        }
        this.serverCache = ImmutableMap.copyOf(tempServerCache);
    }

    /**
     * Sends initialization packet on join
     * @param e {@link net.md_5.bungee.api.event.ServerConnectedEvent}
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerJoin(ServerConnectedEvent e) {
        NetworkHandler.getInstance().send(new P2SInitializationPacket(e.getPlayer().getName(), e.getServer().getInfo().getName()));
    }
}
