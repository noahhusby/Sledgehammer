/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Warp.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.util;

import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.datasets.Point;
import org.json.simple.JSONObject;

public class Warp {
    @Expose
    public final Point point;
    @Expose
    public final String server;
    @Expose
    public final boolean pinned;

    public Warp(Point point, String server, boolean pinned) {
        this.point = point;
        this.server = server;
        this.pinned = pinned;
    }
}
