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
import com.noahhusby.sledgehammer.common.utils.Coords;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.network.P2SPacket;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
public class P2SLocationPacket extends P2SPacket {

    private final String sender;
    private final String server;
    private final Coords coords;

    @Override
    public String getPacketID() {
        return Constants.locationID;
    }

    @Override
    public void getMessage(JsonObject data) {
        double[] proj = SledgehammerUtil.fromGeo(coords.getLon(), coords.getLat());
        double x = BigDecimal.valueOf(proj[0]).setScale(3, RoundingMode.HALF_UP).doubleValue();
        double z = BigDecimal.valueOf(proj[1]).setScale(3, RoundingMode.HALF_UP).doubleValue();

        SledgehammerServer sledgehammerServer = ServerHandler.getInstance().getServer(server);
        if (sledgehammerServer != null) {
            x += sledgehammerServer.getXOffset();
            z += sledgehammerServer.getZOffset();
        }
        data.addProperty("x", x);
        if (!Double.isNaN(coords.getHeight())) {
            data.addProperty("y", (int) Math.floor(coords.getHeight()));
        }
        data.addProperty("z", z);
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
