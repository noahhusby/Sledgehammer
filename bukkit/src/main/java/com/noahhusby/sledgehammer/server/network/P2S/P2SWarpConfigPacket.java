package com.noahhusby.sledgehammer.server.network.P2S;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.common.warps.WarpGroupPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.warp.config.ConfigMenu;
import com.noahhusby.sledgehammer.server.gui.warp.config.confirmation.ConfirmationController;
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
        WarpConfigPayload payload = SledgehammerUtil.GSON.fromJson(data.get("payload"), WarpConfigPayload.class);
        switch (payload.getAction()) {
            case OPEN_CONFIG:
                GUIRegistry.register(new ConfigMenu.ConfigMenuController(Bukkit.getPlayer(info.getSender()), payload));
                break;
            case ADD_FAILURE:
            case ADD_SUCCESSFUL:
            case REMOVE_SUCCESSFUL:
            case REMOVE_FAILURE:
                GUIRegistry.register(new ConfirmationController(Bukkit.getPlayer(info.getSender()), payload, ConfirmationController.Type.valueOf(payload.getAction().name())));
                break;
            case LOCATION_UPDATE:
                Warp warp = payload.getWaypoints().get(data.get("warpId").getAsInt());
                GUIRegistry.register(new ConfirmationController(Bukkit.getPlayer(info.getSender()), payload, ConfirmationController.Type.LOCATION_UPDATE, warp));
        }
    }
}
