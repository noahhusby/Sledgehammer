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

package com.noahhusby.sledgehammer.common;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.common.warps.Warp;

import java.lang.reflect.Type;

/**
 * @author Noah Husby
 * <p>
 * A deserializer for converting legacy string locations to double
 */
public class WarpDeserializer implements JsonDeserializer<Warp> {
    @Override
    public Warp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonObject point = object.getAsJsonObject("point");
        if (point != null) {
            convert(point, "x");
            convert(point, "y");
            convert(point, "z");
            convert(point, "yaw");
            convert(point, "pitch");
        }
        return new Warp(object.get("id").getAsInt(),
                object.get("name").getAsString(),
                point == null ? null : context.deserialize(point, Point.class),
                object.get("server").getAsString(),
                (object.get("headId") == null || object.get("headId").isJsonNull()) ? null : object.get("headId").getAsString(),
                object.get("global").getAsBoolean());
    }

    private void convert(JsonObject json, String key) {
        try {
            String val = json.get(key).getAsString();
            json.addProperty(key, Double.parseDouble(val));
        } catch (ClassCastException | IllegalStateException ignored) {
        }
    }
}
