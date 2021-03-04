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
 *
 * A deserializer for converting legacy string locations to double
 */
public class WarpDeserializer implements JsonDeserializer<Warp> {
    @Override
    public Warp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        JsonObject point = object.getAsJsonObject("Point");
        convert(point, "x");
        convert(point, "y");
        convert(point, "z");
        convert(point, "yaw");
        convert(point, "pitch");
        return new Warp(object.get("Id").getAsInt(),
                object.get("Name").getAsString(),
                context.deserialize(point, Point.class),
                object.get("Server").getAsString(),
                Warp.PinnedMode.valueOf(object.get("Pinned").getAsString()),
                object.get("headID") == null ? null : object.get("headID").getAsString());
    }

    private void convert(JsonObject json, String key) {
        try {
            String val = json.get(key).getAsString();
            json.addProperty(key, Double.parseDouble(val));
        } catch (ClassCastException | IllegalStateException ignored) {
        }
    }
}
