package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.json.simple.JSONObject;

public class P2STeleportPacket extends P2SPacket {
    private final String server;
    private final String sender;
    private final Point point;

    public P2STeleportPacket(String sender, String server, Point point) {
        this.server = server;
        this.sender = sender;
        this.point = point;
    }

    @Override
    public String getPacketID() {
        return Constants.teleportID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("point", point.getJSON());
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
