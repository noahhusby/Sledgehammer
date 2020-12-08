/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - SetServerWarpInventoryController.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.gui.inventories.warp;

import com.noahhusby.sledgehammer.gui.inventories.general.GUIController;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIRegistry;
import com.noahhusby.sledgehammer.gui.inventories.general.IGUIChild;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SetServerWarpInventoryController extends GUIController {
    private final List<SetServerWarpInventory> warpInventories = new ArrayList<>();
    private final JSONObject warpData;
    private final String server;

    public SetServerWarpInventoryController(Player p, JSONObject warpData, String server) {
        super(54, "Warps - " + server, p);
        this.warpData = warpData;
        this.server = server;
        init();
    }

    public SetServerWarpInventoryController(GUIController controller, JSONObject warpData, String server){
        super(controller);
        this.warpData = warpData;
        this.server = server;
        init();
    }

    @Override
    public void init() {
        JSONArray rawWarps = (JSONArray) warpData.get("waypoints");
        JSONArray warps = new JSONArray();
        for(Object o : rawWarps) {
            JSONObject ob = (JSONObject) o;
            String sname = (String) ob.get("server");
            if(sname.equalsIgnoreCase(server)) warps.add(o);
        }

        boolean web = (boolean) warpData.get("web");

        int total_pages = (int) Math.ceil(warps.size() / 27.0);
        if(total_pages == 0) total_pages = 1;
        for(int x = 0; x < total_pages; x++) {
            SetServerWarpInventory w = new SetServerWarpInventory(x, warps, web, server);
            w.initFromController(this, getPlayer(), getInventory());
            warpInventories.add(w);
        }

        openChild(getChildByPage(0));
    }

    public IGUIChild getChildByPage(int page) {
        for(SetServerWarpInventory w : warpInventories) {
            if(w.getPage() == page) return w;
        }

        return null;
    }

    public void switchToAll() {
        GUIRegistry.register(new WarpInventoryController(this, warpData));
    }

    public void switchToServerList() {
        GUIRegistry.register(new ServerWarpInventoryController(this, warpData));
    }
}
