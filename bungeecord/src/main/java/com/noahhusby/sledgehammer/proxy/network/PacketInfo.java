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
