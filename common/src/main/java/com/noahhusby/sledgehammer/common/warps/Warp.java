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

package com.noahhusby.sledgehammer.common.warps;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.noahhusby.lib.data.storage.Key;
import com.noahhusby.sledgehammer.common.CommonUtil;
import com.noahhusby.sledgehammer.common.WarpDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Noah Husby
 */
@Data
@Key("Id")
@AllArgsConstructor
@JsonAdapter(WarpDeserializer.class)
public class Warp {
    @Expose
    @SerializedName("Id")
    private int id;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("Point")
    private Point point;
    @Expose
    @SerializedName("Server")
    private String server;
    @Expose
    @SerializedName("HeadId")
    private String headID;
    @Expose
    @SerializedName("Global")
    private boolean global;

    public Warp() {
        this(-1, "", new Point(), "", "", false);
    }

    public Warp copy() {
        return new Warp(id, name, point, server, headID, global);
    }

    public JsonObject toJson() {
        return CommonUtil.GSON.toJsonTree(this).getAsJsonObject();
    }

    public Warp toWaypoint() {
        Warp warp = copy();
        warp.setPoint(null);
        return warp;
    }
}
