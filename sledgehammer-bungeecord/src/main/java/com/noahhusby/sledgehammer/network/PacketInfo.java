/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PacketInfo.java
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

package com.noahhusby.sledgehammer.network;

import net.md_5.bungee.api.CommandSender;

public class PacketInfo {
    private final double time;
    private final String id;
    private final String sender;
    private final String server;

    public PacketInfo(String id, String sender, String server, double time) {
        this.id = id;
        this.sender = sender;
        this.server = server;
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public String getID() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getServer() {
        return server;
    }

    public static PacketInfo build(String id, String sender, String server) {
        return new PacketInfo(id, sender, server, System.currentTimeMillis());
    }

    public static PacketInfo build(String id, CommandSender player, String server) {
        return build(id, player.getName(), server);
    }

    public static PacketInfo renew(PacketInfo info) {
        return new PacketInfo(info.id, info.sender, info.server, System.currentTimeMillis());
    }
}
