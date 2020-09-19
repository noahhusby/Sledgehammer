package com.noahhusby.sledgehammer.datasets;

import com.google.gson.annotations.Expose;

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
}
