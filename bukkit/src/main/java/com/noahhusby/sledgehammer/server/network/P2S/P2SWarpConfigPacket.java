package com.noahhusby.sledgehammer.server.network.P2S;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.gui.inventories.general.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.inventories.warp.config.ConfigMenuController;
import com.noahhusby.sledgehammer.server.gui.inventories.warp.config.confirmation.ConfirmationController;
import com.noahhusby.sledgehammer.server.network.P2SPacket;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import org.bukkit.Bukkit;

public class P2SWarpConfigPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.warpConfigID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        ServerConfigAction action = ServerConfigAction.valueOf(data.get("action").getAsString());
        WarpConfigPayload payload = WarpConfigPayload.fromPayload(data);
        switch(action) {
            case OPEN_CONFIG:
                GUIRegistry.register(new ConfigMenuController(Bukkit.getPlayer(info.getSender()), payload));
                break;
            case ADD_FAILURE:
            case ADD_SUCCESSFUL:
            case REMOVE_SUCCESSFUL:
            case REMOVE_FAILURE:
                GUIRegistry.register(new ConfirmationController(Bukkit.getPlayer(info.getSender()),
                        payload, ConfirmationController.Type.valueOf(action.name())));
                break;
            case LOCATION_UPDATE:
                JsonObject d = data.getAsJsonObject("data");
                for(WarpGroup wg : payload.getGroups()) {
                    for(Warp w : wg.getWarps()) {
                        if(w.getId() == SledgehammerUtil.JsonUtils.toInt(d.get("warpId"))) {
                            GUIRegistry.register(new ConfirmationController(Bukkit.getPlayer(info.getSender()),
                                    payload, ConfirmationController.Type.LOCATION_UPDATE, w));
                            return;
                        }
                    }
                }
        }
    }

    public enum ServerConfigAction {
        OPEN_CONFIG, REMOVE_SUCCESSFUL, REMOVE_FAILURE, ADD_SUCCESSFUL, ADD_FAILURE, LOCATION_UPDATE
    }
}
