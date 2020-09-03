package com.noahhusby.sledgehammer.datasets;

import com.google.gson.annotations.Expose;

public class Point {
    @Expose
    public final String x;
    @Expose
    public final String y;
    @Expose
    public final String z;

    public Point(String x, String y, String z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
