/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.common.warps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.noahhusby.lib.data.storage.Key;
import com.noahhusby.sledgehammer.common.WarpDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Noah Husby
 */
@Data
@Key("id")
@AllArgsConstructor
@JsonAdapter(WarpDeserializer.class)
public class Warp {
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private Point point;
    @Expose
    private String server;
    @Expose
    private String headId;
    @Expose
    private boolean global;

    public Warp() {
        this(-1, "", new Point(), "", "", false);
    }

    public Warp copy() {
        return new Warp(id, name, point, server, headId, global);
    }

    public Warp toWaypoint() {
        Warp warp = copy();
        warp.setPoint(null);
        return warp;
    }
}
