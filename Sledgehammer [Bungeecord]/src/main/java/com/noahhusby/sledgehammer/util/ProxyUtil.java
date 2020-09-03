package com.noahhusby.sledgehammer.util;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;

public class ProxyUtil {
    public static ServerInfo getServerFromName(String name) {
        return ProxyServer.getInstance().getServerInfo(name);
    }

    public static ServerInfo getServerFromPlayerName(String name) {
        return ProxyServer.getInstance().getPlayer(name).getServer().getInfo();
    }

    public static boolean isServerRegional(ServerInfo server) {
        return isServerRegional(server.getName());
    }

    public static boolean isServerRegional(String name) {
        for(Server s : ServerConfig.getInstance().getServers()) {
            if(s.name.equals(name)) return true;
        }
        return false;
    }
}
