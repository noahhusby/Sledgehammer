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

package com.noahhusby.sledgehammer.server.network.p2s;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.WarpPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.warp.menu.AllWarpInventory;
import com.noahhusby.sledgehammer.server.gui.warp.menu.GroupWarpInventory;
import com.noahhusby.sledgehammer.server.gui.warp.menu.ServerSortMenuInventory;
import com.noahhusby.sledgehammer.server.gui.warp.menu.ServerWarpInventory;
import com.noahhusby.sledgehammer.server.gui.warp.menu.WarpMenuInventory;
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

        switch (payload.getDefaultPage()) {
            default:
            case ALL:
                GUIRegistry.register(new AllWarpInventory.AllWarpInventoryController(player, payload));
                break;
            case LOCAL_GROUP:
                GUIRegistry.register(new GroupWarpInventory.GroupWarpInventoryController(player, payload, payload.getLocalGroup()));
                break;
            case GROUPS:
                GUIRegistry.register(new WarpMenuInventory.WarpMenuInventoryController(player, payload));
                break;
            case SERVERS:
                GUIRegistry.register(new ServerSortMenuInventory.ServerSortMenuInventoryController(player, payload));
                break;
            case LOCAL_SERVER:
                GUIRegistry.register(new ServerWarpInventory.ServerWarpInventoryController(player, payload, payload.getLocalGroup()));
                break;
        }
    }
}
