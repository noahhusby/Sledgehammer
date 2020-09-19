package com.noahhusby.sledgehammer.tasks.data;

import net.md_5.bungee.api.config.ServerInfo;
import org.json.simple.JSONObject;

public class TaskPacket {
    public final ServerInfo server;
    public final String sender;
    public final JSONObject data;

    public TaskPacket(ServerInfo server, String sender, JSONObject data) {
        this.server = server;
        this.sender = sender;
        this.data = data;
    }
}
