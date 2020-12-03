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

package com.noahhusby.sledgehammer.config;

import com.noahhusby.lib.data.storage.Storable;
import com.noahhusby.sledgehammer.datasets.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SledgehammerServer implements Storable {
    private String name;
    private String friendlyName;
    private boolean earthServer;

    private List<Location> locations = new ArrayList<>();
    private String shVersion = null;

    public SledgehammerServer() {}

    public SledgehammerServer(String name) {
        this.name = name;
        this.friendlyName = name;
    }

    /**
     * Gets name of the server
     * @return Name of server
     */
    public String getName() {
        return name;
    }

    /**
     * Gets friendly nickname for server
     * @return Nickname of server
     */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * Sets the friendly nickname for server
     * @param name Nickname for server
     */
    public void setFriendlyName(String name) {
        this.friendlyName = name;
    }

    /**
     * Gets whether the server is a build server
     * @return True if a build server, false if not
     */
    public boolean isEarthServer() {
        return earthServer;
    }

    /**
     * Sets whether the server is a build server
     * @param earth True if a build server, false if not
     */
    public void setEarthServer(boolean earth) {
        this.earthServer = earth;
    }

    /**
     * Gets the list of locations on the server
     * @return List of locations
     */
    public List<Location> getLocations() {
        return locations;
    }

    /**
     * Sets the list of locations on the server
     * @param locations List of locations
     */
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    /**
     * Gets the version of the sledgehammer plugin
     * @return Sledgehammer Version
     */
    public String getSledgehammerVersion() {
        return shVersion;
    }

    /**
     * Initializes server from init packet
     * @param version Sledgehammer Version
     */
    public void initialize(String version) {
        this.shVersion = version;
    }

    /**
     * Gets whether the server is initialized
     * @return True if initialized, false if not
     */
    public boolean isInitialized() {
        return shVersion != null;
    }

    /**
     * Gets {@link ServerInfo}
     * @return {@link ServerInfo}
     */
    public ServerInfo getServerInfo() {
        return ProxyServer.getInstance().getServerInfo(name);
    }

    @Override
    public Storable load(JSONObject data) {
        SledgehammerServer server = new SledgehammerServer((String) data.get("name"));
        JSONArray storedLocs = (JSONArray) data.get("locations");
        for(Object o : storedLocs) {
            JSONObject location = (JSONObject) o;
            server.locations.add((Location) new Location().load(location));
        }

        String version = ServerConfig.getInstance().initializedServers.get(data.get("name"));
        if(version != null) server.shVersion = version;

        server.earthServer = (boolean) data.get("earthServer");
        if(data.get("friendlyName") != null) server.friendlyName = (String) data.get("friendlyName");

        return server;
    }

    @Override
    public JSONObject save(JSONObject data) {
        data.put("name", name);
        data.put("earthServer", earthServer);
        data.put("friendlyName", friendlyName);

        JSONArray locs = new JSONArray();
        for(Location l : locations)
            locs.add(l.save(new JSONObject()));

        data.put("locations", locs);

        return data;
    }
}
