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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.noahhusby.sledgehammer.datasets.Point;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.function.BiConsumer;

public class Warp {

    @Expose
    @SerializedName("Pinned")
    @Getter @Setter private PinnedMode pinned;
    @Expose
    @SerializedName("Server")
    @Getter @Setter private String server;
    @Expose
    @SerializedName("HeadId")
    @Getter @Setter private String headID;
    @Expose
    @SerializedName("Point")
    @Getter @Setter private Point point;
    @Expose
    @SerializedName("Name")
    @Getter @Setter private String name;
    @Expose
    @SerializedName("Id")
    @Getter @Setter private int id;

    @Getter @Setter private BiConsumer<Boolean, Warp> response;

    public Warp() {
        this(-1, "", new Point(), "", PinnedMode.NONE, "");
    }

    public Warp(int id, String name, Point point, String server, PinnedMode pinned, String headID) {
        this.id = id;
        this.name = name;
        this.point = point;
        this.server = server;
        this.pinned = pinned;
        this.headID = headID;
    }

    public JSONObject save(JSONObject data) {
        data.put("Id", id);
        data.put("Name", name);
        data.put("Server", server);
        data.put("Pinned", pinned.name());
        data.put("Point", point.getJSON().toJSONString());
        data.put("HeadId", headID);
        return data;
    }

    public JSONObject toWaypoint() {
        JSONObject data = save(new JSONObject());
        data.remove("Point");
        return data;
    }

    public enum PinnedMode {
        NONE, LOCAL, GLOBAL
    }
}
