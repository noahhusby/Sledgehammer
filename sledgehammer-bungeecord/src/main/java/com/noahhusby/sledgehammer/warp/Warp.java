/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Warp.java
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

package com.noahhusby.sledgehammer.warp;

import com.noahhusby.lib.data.storage.Storable;
import com.noahhusby.sledgehammer.datasets.Point;
import org.json.simple.JSONObject;

public class Warp implements Storable {
    public final String name;
    public final Point point;
    public final String server;
    public final boolean pinned;

    public Warp() {
        this("", new Point(), "", false);
    }

    public Warp(String name, Point point, String server, boolean pinned) {
        this.name = name;
        this.point = point;
        this.server = server;
        this.pinned = pinned;
    }

    @Override
    public Storable load(JSONObject data) {
        Point p = (Point) new Point().load((JSONObject) data.get("point"));
        return new Warp((String) data.get("name"), p, (String) data.get("server"), (boolean) data.get("pinned"));
    }

    @Override
    public JSONObject save(JSONObject data) {
        data.put("name", name);
        data.put("server", server);
        data.put("pinned", pinned);
        data.put("point", point.getJSON());

        return data;
    }
}
