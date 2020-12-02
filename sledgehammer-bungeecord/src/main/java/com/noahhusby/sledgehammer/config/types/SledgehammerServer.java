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

package com.noahhusby.sledgehammer.config.types;

import com.google.gson.annotations.Expose;
import com.noahhusby.lib.data.storage.Storable;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.datasets.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SledgehammerServer implements Storable {
    @Expose
    public String name;
    @Expose
    public boolean earthServer;
    @Expose
    public String permission_type;
    @Expose
    public List<Location> locations = new ArrayList<>();

    private String shVersion = null;

    public SledgehammerServer() {}

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

    public void initialize(String version) {
        this.shVersion = version;
    }

    @Override
    public Storable load(JSONObject data) {
        SledgehammerServer server = new SledgehammerServer((String) data.get("name"));
        JSONArray storedLocs = (JSONArray) data.get("locations");
        for(Object o : storedLocs) {
            JSONObject location = (JSONObject) o;
            server.locations.add((Location) new Location().load(location));
        }

        server.earthServer = (boolean) data.get("earthServer");
        server.permission_type = (String) data.get("permission_type");

        return server;
    }

    @Override
    public JSONObject save(JSONObject data) {
        data.put("name", name);
        data.put("earthServer", earthServer);
        data.put("permission_type", permission_type);
        JSONArray locs = new JSONArray();
        for(Location l : locations)
            locs.add(l.save(new JSONObject()));
        data.put("locations", locs);

        return data;
    }
}
