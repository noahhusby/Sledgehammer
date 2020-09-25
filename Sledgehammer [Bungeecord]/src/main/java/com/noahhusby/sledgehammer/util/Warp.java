package com.noahhusby.sledgehammer.util;

import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.datasets.Point;
import org.json.simple.JSONObject;

public class Warp {
    @Expose
    public final Point point;
    @Expose
    public final String server;

    public Warp(Point point, String server) {
        this.point = point;
        this.server = server;
    }
}
