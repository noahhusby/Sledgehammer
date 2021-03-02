package com.noahhusby.sledgehammer.common.warps;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Noah Husby
 */
@RequiredArgsConstructor
public class WarpGroup {
    @Getter
    private final String Id;
    @Getter
    private final String name;
    @Getter
    private final String headId;
    @Getter
    private List<Warp> warps = new ArrayList<>();

    public JsonObject toJson() {
        JsonObject warpGroup = new JsonObject();
        warpGroup.addProperty("id", Id);
        warpGroup.addProperty("name", name);
        JsonArray waypoints = new JsonArray();
        warpGroup.addProperty("headId", headId);
        for (Warp w : warps) {
            waypoints.add(w.toWaypoint());
        }
        return warpGroup;
    }
}