package com.noahhusby.sledgehammer.commands.data;

import com.noahhusby.sledgehammer.commands.fragments.admin.SetupAdminCommand;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.datasets.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetupAdminTracker {

    public List<ServerInfo> servers = new ArrayList<>();
    public List<Server> queuedServers;

    public Server currentServer;
    public SetupAdminCommand.dialogAction dialogAction = SetupAdminCommand.dialogAction.SERVER_EDIT;
    public ServerInfo currentBungeeServer;

    public Location locationEdit = new Location();

    public SetupAdminTracker() {
        Map<String, ServerInfo> serversTemp = ProxyServer.getInstance().getServers();
        for(Map.Entry<String, ServerInfo> s : serversTemp.entrySet()) {
            servers.add(s.getValue());
        }

        currentBungeeServer = servers.get(0);
        queuedServers = ServerConfig.getInstance().getServers();

        currentServer = getServerFromQueue(currentBungeeServer.getName());
    }


    public Server getServerFromQueue(String name) {
        for(Server s : queuedServers) {
            if(s.name.toLowerCase().equals(name)) {
                return s;
            }
        }

        return new Server(name);
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
            currentServer = getServerFromQueue(currentBungeeServer.getName());
            return;
        }

        currentBungeeServer = servers.get(getCurrentServerIndex()+1);
        currentServer = getServerFromQueue(currentBungeeServer.getName());
    }
}
