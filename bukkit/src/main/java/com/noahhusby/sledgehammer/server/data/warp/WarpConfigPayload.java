package com.noahhusby.sledgehammer.server.data.warp;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.SmartObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class WarpConfigPayload {
    @Getter private final String requestGroup;
    @Getter private final String salt;
    @Getter private final boolean local;
    @Getter private final boolean admin;
    @Getter private final List<WarpGroup> groups;
    @Getter private final JsonObject data;

    public static WarpConfigPayload fromPayload(JsonObject data) {
        JsonArray groups = data.getAsJsonArray("groups");
        List<WarpGroup> groupsList = Lists.newArrayList();
        for(JsonElement je : groups) {
            groupsList.add(SledgehammerUtil.GSON.fromJson(je, WarpGroup.class));
        }
        return new WarpConfigPayload(data.get("requestGroup").getAsString(), data.get("salt").getAsString(), data.get("local").getAsBoolean(),
                data.get("admin").getAsBoolean(), groupsList, data.getAsJsonObject("data"));
    }
}
