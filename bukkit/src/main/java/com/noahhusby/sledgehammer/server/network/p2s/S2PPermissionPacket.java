package com.noahhusby.sledgehammer.server.network.p2s;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import com.noahhusby.sledgehammer.server.network.S2PPacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class S2PPermissionPacket extends S2PPacket {
    private final Player player;
    private final String salt;
    private final boolean permission;

    @Override
    public String getPacketID() {
        return Constants.permissionCheckID;
    }

    @Override
    public void getMessage(JsonObject data) {
        data.addProperty("player", player.getUniqueId().toString());
        data.addProperty("salt", salt);
        data.addProperty("permission", permission);
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player);
    }
}
