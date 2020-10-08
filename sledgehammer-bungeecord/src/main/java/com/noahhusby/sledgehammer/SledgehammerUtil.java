/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerUtil.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer;

import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.SledgehammerServer;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.projection.ScaleProjection;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class SledgehammerUtil {

    private static GeographicProjection projection = new ModifiedAirocean();
    private static GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
    private static ScaleProjection scaleProj = new ScaleProjection(uprightProj, Constants.SCALE, Constants.SCALE);

    public static double[] toGeo(double x, double z) {
        return scaleProj.toGeo(x, z);
    }

    public static double[] fromGeo(double lon, double lat) {
        return scaleProj.fromGeo(lon, lat);
    }

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
        for(SledgehammerServer s : ServerConfig.getInstance().getServers()) {
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

    public static boolean inEarthRegion(SledgehammerPlayer player) {
        double[] geo = toGeo(Double.parseDouble(player.getLocation().x), Double.parseDouble(player.getLocation().z));
        return !(geo == null || geo.length < 1 || Double.isNaN(geo[0]) || Double.isNaN(geo[1]));
    }

    public static String getRawArguments(String[] args) {
        if(args.length == 0) {
            return "";
        } else if(args.length == 1) {
            return args[0];
        }

        String arguments = args[0];

        for(int x = 1; x < args.length; x++) {
            arguments+=" "+args[x];
        }

        return arguments;
    }
}
