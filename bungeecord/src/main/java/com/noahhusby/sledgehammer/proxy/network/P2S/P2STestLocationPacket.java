/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2STestLocationPacket.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.proxy.network.P2S;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.network.P2SPacket;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;

public class P2STestLocationPacket extends P2SPacket {
    private final String server;
    private final String sender;
    private int zoom;

    public P2STestLocationPacket(String sender, String server, int zoom) {
        this.server = server;
        this.sender = sender;
        this.zoom = zoom;
        if (zoom == -1) {
            this.zoom = ConfigHandler.zoom;
        }
    }

    @Override
    public String getPacketID() {
        return Constants.testLocationID;
    }

    @Override
    public void getMessage(JsonObject data) {
        data.addProperty("zoom", zoom);
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
