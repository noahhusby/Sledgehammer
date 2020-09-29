/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Server.java
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

package com.noahhusby.sledgehammer.config.types;

import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.datasets.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.List;

public class SledgehammerServer {
    @Expose
    public String name;
    @Expose
    public boolean earthServer;
    @Expose
    public String permission_type;
    @Expose
    public List<Location> locations = new ArrayList<>();

    private String shVersion = null;
    private String tpllMode = null;

    public SledgehammerServer(String name) {
        this.name = name;
    }

    public ServerInfo getServerInfo() {
        return ProxyServer.getInstance().getServerInfo(name);
    }

    public boolean isInitialized() {
        return shVersion != null;
    }

    public String getSledgehammerVersion() {
        return shVersion;
    }

    public String getTpllMode() {
        return tpllMode;
    }

    public void initialize(String version, String tpllMode) {
        this.shVersion = version;
        this.tpllMode = tpllMode;
    }
}
