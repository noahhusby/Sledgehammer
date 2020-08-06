package com.noahhusby.sledgehammer.util;

import com.noahhusby.sledgehammer.Sledgehammer;
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
        ArrayList<LinkedHashMap<String, ArrayList<String>>> map = (ArrayList<LinkedHashMap<String, ArrayList<String>>>) Sledgehammer.configuration.get("servers");
        for(LinkedHashMap<String, ArrayList<String>> s : map) {
            for(String server : s.keySet()) {
                if(server.equals(name)) return true;
            }
        }
        return false;
    }
}
