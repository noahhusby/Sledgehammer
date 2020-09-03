package com.noahhusby.sledgehammer.tasks.data;

import net.md_5.bungee.api.config.ServerInfo;

public class TaskPacket {
    public final ServerInfo server;
    public final String sender;
    public final String[] data;

    public TaskPacket(ServerInfo server, String sender, String[] data) {
        this.server = server;
        this.sender = sender;
        this.data = data;
    }
}
