/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PacketInfo.java
 * All rights reserved.
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
