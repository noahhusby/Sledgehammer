package com.noahhusby.sledgehammer.network.S2P;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

@RequiredArgsConstructor
public class S2PPermissionPacket extends S2PPacket {

    private final boolean permission;
    private final Player player;
    private final String salt;

    @Override
    public String getPacketID() {
        return Constants.permissionCheckID;
    }

    @Override
    public void getMessage(JsonObject data) {
        data.addProperty("salt", salt);
        data.addProperty("permission", permission);
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player);
    }
}
