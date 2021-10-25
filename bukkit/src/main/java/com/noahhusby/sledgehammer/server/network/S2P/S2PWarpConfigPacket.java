package com.noahhusby.sledgehammer.server.network.S2P;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import com.noahhusby.sledgehammer.server.network.S2PPacket;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@AllArgsConstructor
public class S2PWarpConfigPacket extends S2PPacket {
    private final ProxyConfigAction action;
    private final Player player;
    private final String salt;
    private JsonObject data = new JsonObject();

    @Override
    public String getPacketID() {
        return Constants.warpConfigID;
    }

    @Override
    public void getMessage(JsonObject data) {
        data.addProperty("salt", salt);
        data.addProperty("action", action.name());
        data.add("data", this.data);
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player);
    }

    public enum ProxyConfigAction {
        OPEN_CONFIG, CREATE_WARP, UPDATE_WARP, UPDATE_PLAYER_DEFAULT, WARP_UPDATE_LOCATION, REMOVE_WARP
    }
}
