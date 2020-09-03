package com.noahhusby.sledgehammer.tasks.data;

import net.md_5.bungee.api.config.ServerInfo;

public class TransferPacket {
    public final ServerInfo server;
    public final String sender;

    public TransferPacket(ServerInfo server, String sender) {
        this.server = server;
        this.sender = sender;
    }
}
