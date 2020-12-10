/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Point.java
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

package com.noahhusby.sledgehammer.datasets;

import com.noahhusby.lib.data.storage.Storable;
import org.json.simple.JSONObject;

public class Point implements Storable {
    public final String x;
    public final String y;
    public final String z;
    public final String yaw;
    public final String pitch;

    public Point() {
        this("", "", "", "", "");
    }

    public Point(String x, String y, String z, String yaw, String pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public JSONObject getJSON() {
        JSONObject o = new JSONObject();

        o.put("x", x);
        o.put("y", y);
        o.put("z", z);
        o.put("pitch", pitch);
        o.put("yaw", yaw);

        return o;
    }

    @Override
    public Storable load(JSONObject data) {
        return new Point((String) data.get("x"), (String) data.get("y"), (String) data.get("z"), (String) data.get("yaw"), (String) data.get("pitch"));
    }

    @Override
    public JSONObject save(JSONObject data) {
        return getJSON();
    }
}
