package com.noahhusby.sledgehammer.util;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class ProxyUtil {
    public static ServerInfo getServerFromName(String name) {
        return ProxyServer.getInstance().getServerInfo(name);
    }
}
