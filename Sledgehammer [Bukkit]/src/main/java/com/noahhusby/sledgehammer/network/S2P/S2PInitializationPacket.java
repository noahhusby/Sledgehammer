package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class S2PInitializationPacket extends S2PPacket {

    private final Player player;

    public S2PInitializationPacket(Player player) {
        this.player = player;
    }

    @Override
    public String getPacketID() {
        return Constants.initID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("version", Constants.VERSION);
        data.put("tpllmode", ConfigHandler.tpllMode);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player);
    }
}
