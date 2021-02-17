/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - P2SWarpGUIPacket.java
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

package com.noahhusby.sledgehammer.server.network.P2S;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.data.warp.WarpPayload;
import com.noahhusby.sledgehammer.server.gui.inventories.general.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.inventories.warp.AllWarpInventoryController;
import com.noahhusby.sledgehammer.server.gui.inventories.warp.GroupWarpInventoryController;
import com.noahhusby.sledgehammer.server.gui.inventories.warp.PinnedWarpInventoryController;
import com.noahhusby.sledgehammer.server.network.P2SPacket;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class P2SWarpGUIPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.warpGUIID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        Player p = Bukkit.getPlayer(info.getSender());
        if(p == null) {
            throwNoSender();
            return;
        }

        if(!p.isOnline()) {
            throwNoSender();
            return;
        }

        WarpPayload payload = WarpPayload.fromPayload(data);
        if(payload.getGroups().isEmpty()) payload.setDefaultPage("pinned");

        switch (payload.getDefaultPage()) {
            default:
            case "group":
                GUIRegistry.register(new GroupWarpInventoryController(p, payload, payload.getRequestGroup()));
                break;
            case "all":
                GUIRegistry.register(new AllWarpInventoryController(p, payload));
                break;
            case "pinned":
                GUIRegistry.register(new PinnedWarpInventoryController(p, payload));
                break;
        }
    }
}
