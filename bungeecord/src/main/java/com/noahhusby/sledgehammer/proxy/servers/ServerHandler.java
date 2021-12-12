/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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
