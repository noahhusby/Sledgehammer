/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - ServerWarpInventoryController.java
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

public class ServerWarpInventoryController extends GUIController {
    private final List<ServerWarpInventory> warpInventories = new ArrayList<>();
    private final JSONObject warpData;

    public ServerWarpInventoryController(Player p, JSONObject warpData) {
        super(54, "Warps - Server Selector", p);
        this.warpData = warpData;
        init();
    }

    public ServerWarpInventoryController(GUIController controller, JSONObject warpData) {
        super(controller);
        this.warpData = warpData;
        init();
    }

    @Override
    public void init() {
        JSONArray rawWarps = (JSONArray) warpData.get("waypoints");
        List<String> servers = new ArrayList<>();
        for(Object o : rawWarps) {
            JSONObject ob = (JSONObject) o;
            String server = (String) ob.get("server");
            if(!servers.contains(server)) servers.add(server);
        }

        int total_pages = (int) Math.ceil(servers.size() / 27.0);
        if(total_pages == 0) total_pages = 1;
        for(int x = 0; x < total_pages; x++) {
            ServerWarpInventory w = new ServerWarpInventory(x, servers, (JSONObject) warpData.get("heads"));
            w.initFromController(this, getPlayer(), getInventory());
            warpInventories.add(w);
        }

        openChild(getChildByPage(0));
    }

    public IGUIChild getChildByPage(int page) {
        for(ServerWarpInventory w : warpInventories) {
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

    public void switchToServer(String server) {
        GUIRegistry.register(new SetServerWarpInventoryController(this, warpData, server));
    }
}
