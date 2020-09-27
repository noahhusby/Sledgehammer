/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PSetwarpPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import org.json.simple.JSONObject;

public class S2PSetwarpPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.setwarpID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        SmartObject point = SmartObject.fromJSON((JSONObject) data.get("point"));
        WarpHandler.getInstance().incomingLocationResponse(info.getSender(), new Point(point.getString("x"), point.getString("y"),
                point.getString("z"), point.getString("yaw"), point.getString("pitch")));
    }
}
