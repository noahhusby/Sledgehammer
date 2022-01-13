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
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.warp.config.ConfigMenu;
import com.noahhusby.sledgehammer.server.gui.warp.config.confirmation.ConfirmationController;
import com.noahhusby.sledgehammer.server.gui.warp.config.manage.ManageWarpInventory;
import com.noahhusby.sledgehammer.server.network.P2SPacket;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class P2SWarpConfigPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.warpConfigID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        WarpConfigPayload payload = SledgehammerUtil.GSON.fromJson(data.get("payload"), WarpConfigPayload.class);
        Player player = Bukkit.getPlayer(info.getSender());
        switch (payload.getAction()) {
            case OPEN_CONFIG:
                GUIRegistry.register(new ConfigMenu.ConfigMenuController(player, payload));
                break;
            case ADD_FAILURE:
                GUIRegistry.register(new ConfirmationController(player, new ConfigMenu.ConfigMenuController(player, payload), ConfirmationController.Type.ADD_FAILURE, "Failed to create warp!"));
                break;
            case ADD_SUCCESSFUL:
                GUIRegistry.register(new ConfirmationController(player, new ConfigMenu.ConfigMenuController(player, payload), ConfirmationController.Type.ADD_SUCCESSFUL, "Successfully created warp!"));
                break;
            case REMOVE_SUCCESSFUL:
                GUIRegistry.register(new ConfirmationController(player, new ConfigMenu.ConfigMenuController(player, payload), ConfirmationController.Type.REMOVE_SUCCESSFUL, "Successfully removed warp!"));
                break;
            case REMOVE_FAILURE:
                GUIRegistry.register(new ConfirmationController(player, new ConfigMenu.ConfigMenuController(player, payload), ConfirmationController.Type.REMOVE_FAILURE, "Failed to remove warp!"));
                break;
            case LOCATION_UPDATE:
                Warp warp = payload.getWaypoints().get(data.get("warpId").getAsInt());
                GUIRegistry.register(new ConfirmationController(Bukkit.getPlayer(info.getSender()), new ManageWarpInventory.ManageWarpInventoryController(player, payload, warp), ConfirmationController.Type.LOCATION_UPDATE, null));
        }
    }
}
