package com.noahhusby.sledgehammer.data.location;

import com.google.gson.annotations.Expose;
import org.json.simple.JSONObject;

public class Point {
    public final String x;
    public final String y;
    public final String z;
    public final String pitch;
    public final String yaw;

    public Point(String x, String y, String z, String pitch, String yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
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
