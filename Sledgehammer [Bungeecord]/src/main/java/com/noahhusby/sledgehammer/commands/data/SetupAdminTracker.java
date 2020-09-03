package com.noahhusby.sledgehammer.commands.data;

import com.noahhusby.sledgehammer.commands.admin.SetupAdminCommand;
import com.noahhusby.sledgehammer.config.types.Server;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetupAdminTracker {

    List<ServerInfo> servers = new ArrayList<>();
    public Server currentServer;
    public SetupAdminCommand.dialogAction dialogAction = SetupAdminCommand.dialogAction.SERVER_EDIT;
    public ServerInfo currentBungeeServer;

    public SetupAdminTracker() {
        Map<String, ServerInfo> serversTemp = ProxyServer.getInstance().getServers();
        for(Map.Entry<String, ServerInfo> s : serversTemp.entrySet()) {
            servers.add(s.getValue());
        }
        currentBungeeServer = servers.get(0);
    }

    public int getCurrentServerIndex() {
        return servers.indexOf(currentBungeeServer);
    }

    public int getMaxServerIndex() {
        return servers.size();
    }

    public void nextServer() {
        if(getCurrentServerIndex()+1 >= getMaxServerIndex()) {
            currentBungeeServer = servers.get(0);
            return;
        }
        currentBungeeServer = servers.get(getCurrentServerIndex()+1);
    }
}
