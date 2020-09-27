/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Point.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.datasets;

import com.google.gson.annotations.Expose;
import org.json.simple.JSONObject;

public class Point {
    @Expose
    public final String x;
    @Expose
    public final String y;
    @Expose
    public final String z;
    @Expose
    public final String yaw;
    @Expose
    public final String pitch;

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
}
