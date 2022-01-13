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

package com.noahhusby.sledgehammer.proxy.network.p2s;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.network.P2SPacket;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;

public class P2SLocationPacket extends P2SPacket {

    private final String sender;
    private final String server;
    private final String lat;
    private final String lon;
    private String xOffset;
    private String zOffset;

    public P2SLocationPacket(String sender, String server, double[] geo) {
        this.server = server;
        this.sender = sender;
        SledgehammerServer sledgehammerServer = ServerHandler.getInstance().getServer(server);
        if (sledgehammerServer != null) {
            xOffset = String.valueOf(sledgehammerServer.getXOffset());
            zOffset = String.valueOf(sledgehammerServer.getZOffset());
        }

        this.lat = String.valueOf(geo[0]);
        this.lon = String.valueOf(geo[1]);
    }

    @Override
    public String getPacketID() {
        return Constants.locationID;
    }

    @Override
    public void getMessage(JsonObject data) {
        data.addProperty("lat", lat);
        data.addProperty("lon", lon);
        data.addProperty("xOffset", xOffset);
        data.addProperty("zOffset", zOffset);
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
