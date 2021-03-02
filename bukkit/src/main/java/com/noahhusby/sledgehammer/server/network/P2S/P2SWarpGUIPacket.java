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
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.common.warps.WarpPayload;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.warp.menu.AllWarpInventoryController;
import com.noahhusby.sledgehammer.server.gui.warp.menu.GroupWarpInventoryController;
import com.noahhusby.sledgehammer.server.gui.warp.menu.PinnedWarpInventoryController;
import com.noahhusby.sledgehammer.server.gui.warp.menu.WarpMenuInventoryController;
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
        Player player = Bukkit.getPlayer(info.getSender());
        if (player == null) {
            throwNoSender();
            return;
        }

        if (!player.isOnline()) {
            throwNoSender();
            return;
        }

        WarpPayload payload = SledgehammerUtil.GSON.fromJson(data.get("payload"), WarpPayload.class);
        if(payload.isOverride()) {
            for(WarpGroup wg: payload.getGroups()) {
                if(wg.getId().equalsIgnoreCase(payload.getLocalGroup())) {
                    GUIRegistry.register(new GroupWarpInventoryController(player, payload, payload.getLocalGroup()));
                    return;
                }
            }
        }

        switch (payload.getDefaultPage()) {
            default:
            case ALL:
                GUIRegistry.register(new AllWarpInventoryController(player, payload));
                break;
            case PINNED:
                GUIRegistry.register(new PinnedWarpInventoryController(player, payload));
                break;
            case GROUPS:
                GUIRegistry.register(new WarpMenuInventoryController(player, payload));
                break;
        }
    }
}
