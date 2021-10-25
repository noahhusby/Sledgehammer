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

package com.noahhusby.sledgehammer.proxy.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

@AllArgsConstructor
@Getter
public class PacketInfo {
    private final String id;
    private final String sender;
    private final ServerInfo server;
    private final double time;

    /**
     * Creates new {@link PacketInfo}
     *
     * @param id     ID of the packet
     * @param sender Name of sender
     * @param server Name of server
     * @return {@link PacketInfo}
     */
    public static PacketInfo build(String id, String sender, String server) {
        return build(id, sender, ProxyServer.getInstance().getServerInfo(server));
    }

    /**
     * Creates new {@link PacketInfo}
     *
     * @param id     ID of the packet
     * @param sender Name of sender
     * @param server Name of server
     * @return {@link PacketInfo}
     */
    public static PacketInfo build(String id, String sender, ServerInfo server) {
        return new PacketInfo(id, sender, server, System.currentTimeMillis());
    }

    /**
     * Creates new {@link PacketInfo}
     *
     * @param id     ID of the packet
     * @param sender {@link CommandSender}
     * @param server Name of server
     * @return {@link PacketInfo}
     */
    public static PacketInfo build(String id, CommandSender sender, ServerInfo server) {
        return build(id, sender.getName(), server);
    }

    /**
     * Creates new {@link PacketInfo}
     *
     * @param id     ID of the packet
     * @param sender {@link CommandSender}
     * @param server Name of server
     * @return {@link PacketInfo}
     */
    public static PacketInfo build(String id, CommandSender sender, String server) {
        return build(id, sender.getName(), server);
    }

    /**
     * Renews packet info to current time
     *
     * @param info Current {@link PacketInfo}
     * @return New {@link PacketInfo}
     */
    public static PacketInfo renew(PacketInfo info) {
        return new PacketInfo(info.id, info.sender, info.server, System.currentTimeMillis());
    }
}
