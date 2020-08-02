package com.noahhusby.sledgehammer.util;

import com.google.gson.annotations.Expose;
import net.md_5.bungee.api.config.ServerInfo;

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
