package com.noahhusby.sledgehammer;

import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class SledgehammerUtil {
    public static ServerInfo getServerFromName(String name) {
        return ProxyServer.getInstance().getServerInfo(name);
    }

    public static ServerInfo getServerFromPlayerName(String name) {
        return ProxyServer.getInstance().getPlayer(name).getServer().getInfo();
    }

    public static String getServerNameByPlayer(CommandSender sender) {
        return ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo().getName();
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

    public static boolean isGenuineRequest(String u) {
        try {
            return u.equals(ConfigHandler.authenticationCode);
        } catch (Exception e) {
            Sledgehammer.logger.info("Error occurred while parsing incoming authentication command!");
            return false;
        }
    }

    public static String getRawArguments(String[] args) {
        if(args.length == 0) {
            return "";
        } else if(args.length == 1) {
            return args[0];
        }

        String arguments = args[0];

        for(int x = 1; x < args.length; x++) {
            arguments+=","+args[x];
        }

        return arguments;
    }
}
