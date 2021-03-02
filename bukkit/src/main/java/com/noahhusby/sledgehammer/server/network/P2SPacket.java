/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - P2SPacket.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.server.network;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Sledgehammer;

public abstract class P2SPacket {
    public NetworkHandler getManager() {
        return NetworkHandler.getInstance();
    }

    protected void throwNoSender() {
        Sledgehammer.LOGGER.warning("The task manager attempted to execute a task without an available sender.");
    }

    public abstract String getPacketID();
    public abstract void onMessage(PacketInfo info, JsonObject data);
}
