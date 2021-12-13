/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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

package com.noahhusby.sledgehammer.server.network.p2s;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.network.P2SPacket;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import com.noahhusby.sledgehammer.server.network.s2p.S2PPermissionPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class P2SPermissionPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.permissionCheckID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        UUID player = UUID.fromString(data.get("player").getAsString());
        String salt = data.get("salt").getAsString();
        String permission = data.get("permission").getAsString();

        boolean permissionResponse = false;

        Player p = Bukkit.getPlayer(player);
        if (p != null) {
            permissionResponse = p.hasPermission(permission);
        }

        getManager().send(new S2PPermissionPacket(p, salt, permissionResponse));
    }
}
