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

package com.noahhusby.sledgehammer.proxy.servers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.noahhusby.lib.data.storage.Key;
import com.noahhusby.sledgehammer.common.SledgehammerVersion;
import com.noahhusby.sledgehammer.common.TpllMode;
import com.noahhusby.sledgehammer.proxy.datasets.Location;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Key("Name")
@Getter
public class SledgehammerServer {
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("Nick")
    @Setter
    private String friendlyName;
    @Expose
    @SerializedName("EarthServer")
    @Setter
    private boolean earthServer;
    @Expose
    @SerializedName("Locations")
    @Setter
    private List<Location> locations = new ArrayList<>();
    @Expose
    @SerializedName("XOffset")
    @Setter
    private int xOffset;
    @Expose
    @SerializedName("ZOffset")
    @Setter
    private int zOffset;
    @Expose
    @SerializedName("StealthMode")
    @Setter
    private boolean stealthMode;
    @Expose
    @SerializedName("TpllMode")
    @Setter
    private TpllMode tpllMode = TpllMode.NORMAL;
    private SledgehammerVersion sledgehammerVersion = null;

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
    public void initialize(SledgehammerVersion version) {
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
}
