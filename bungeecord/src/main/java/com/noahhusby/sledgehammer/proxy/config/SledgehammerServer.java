/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerServer.java
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

package com.noahhusby.sledgehammer.proxy.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.noahhusby.lib.data.storage.Key;
import com.noahhusby.sledgehammer.proxy.datasets.Location;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Key("Name")
public class SledgehammerServer {
    @Expose
    @SerializedName("Name")
    @Getter
    private String name;
    @Expose
    @SerializedName("Nick")
    @Getter
    @Setter
    private String friendlyName;
    @Expose
    @SerializedName("EarthServer")
    @Getter
    @Setter
    private boolean earthServer;
    @Expose
    @SerializedName("Locations")
    @Getter
    @Setter
    private List<Location> locations = new ArrayList<>();
    @Expose
    @SerializedName("XOffset")
    @Getter
    @Setter
    private int xOffset;
    @Expose
    @SerializedName("ZOffset")
    @Getter
    @Setter
    private int zOffset;
    @Expose
    @SerializedName("StealthMode")
    @Getter
    @Setter
    private boolean stealthMode;
    @Getter
    private String sledgehammerVersion = null;

    public SledgehammerServer() {
    }

    public SledgehammerServer(String name) {
        this.name = name;
        this.friendlyName = name;
    }

    /**
     * Initializes server from init packet
     *
     * @param version Sledgehammer Version
     */
    public void initialize(String version) {
        this.sledgehammerVersion = version;
    }

    /**
     * Gets whether the server is initialized
     *
     * @return True if initialized, false if not
     */
    public boolean isInitialized() {
        return sledgehammerVersion != null;
    }

    /**
     * Gets {@link ServerInfo}
     *
     * @return {@link ServerInfo}
     */
    public ServerInfo getServerInfo() {
        return ProxyServer.getInstance().getServerInfo(name);
    }

    /**
     * Gets the group assigned to this server
     *
     * @return Returns the associated group, or a new group if none exists
     */
    public ServerGroup getGroup() {
        for (ServerGroup g : ServerHandler.getInstance().getGroups()) {
            if (g.getServers().contains(name)) {
                return g;
            }
        }
        return new ServerGroup(name, "", friendlyName, Collections.singletonList(name), new ArrayList<>());
    }
}
