package com.noahhusby.sledgehammer.server.data.warp;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.SmartObject;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class WarpPayload {
    private String defaultPage;
    private String requestGroup;
    private String salt;
    private boolean editAccess;
    private boolean local;
    private List<WarpGroup> groups;

    public String getDefaultPage() {
        return defaultPage;
    }

    public String getRequestGroup() {
        return requestGroup;
    }

    public boolean isEditAccess() {
        return editAccess;
    }

    public boolean isLocal() {
        return local;
    }

    public List<WarpGroup> getGroups() {
        return groups;
    }

    public void setDefaultPage(String defaultPage) {
        this.defaultPage = defaultPage;
    }

    public String getSalt() {
        return salt;
    }

    public static WarpPayload fromPayload(JsonObject data) {
        JsonArray groups = data.getAsJsonArray("groups");
        List<WarpGroup> warpGroupList = Lists.newArrayList();
        for(JsonElement je : groups) {
            warpGroupList.add(SledgehammerUtil.GSON.fromJson(je, WarpGroup.class));
        }

        return new WarpPayload(data.get("defaultPage").getAsString(), data.get("requestGroup").getAsString(),
                data.get("salt").getAsString(), data.get("editAccess").getAsBoolean(), data.get("local").getAsBoolean(), warpGroupList);
    }
}
