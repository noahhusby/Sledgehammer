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

package com.noahhusby.sledgehammer.proxy;

import com.noahhusby.sledgehammer.common.CommonUtil;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.config.ServerHandler;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerServer;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@UtilityClass
public class SledgehammerUtil extends CommonUtil {

    /**
     * Gets Bungeecord server by it's name
     *
     * @param name The name of the BungeeCord server
     * @return The Bungeecord server. Result will be null if no matching server is found.
     */
    public static ServerInfo getServerByName(String name) {
        return ProxyServer.getInstance().getServerInfo(name);
    }

    /**
     * Gets the current Bungeecord server that a player is on
     *
     * @param sender The sender
     * @return The Bungeecord server. Result will be null if no matching server is found.
     */
    public static ServerInfo getServerFromSender(CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) {
            return null;
        }
        return ((ProxiedPlayer) sender).getServer().getInfo();
    }

    /**
     * Gets Bungeecord server from player name
     *
     * @param name The name of the player
     * @return The Bungeecord server. Result will be null if no matching server is found.
     * @deprecated As of release 0.4, replaced by {@link #getServerNameByPlayer(ProxiedPlayer)}
     */
    @Deprecated
    public static ServerInfo getServerFromPlayer(String name) {
        return ProxyServer.getInstance().getPlayer(name).getServer().getInfo();
    }

    /**
     * Gets name of Bungeecord server from player
     *
     * @param player The name of the player
     * @return The name of the Bungeecord server. Result will be null if no matching server is found.
     * @deprecated As of release 0.4, replaced by {@link #getServerFromSender(CommandSender)}
     */
    @Deprecated
    public static String getServerNameByPlayer(ProxiedPlayer player) {
        return player.getServer().getInfo().getName();
    }

    /**
     * Checks if a Bungeecord server is a sledgehammer server
     *
     * @param server The Bungeecord server
     * @return True if the Bungeecord server is a Sledgehammer server, False if not
     */
    public static boolean isSledgehammerServer(ServerInfo server) {
        for (SledgehammerServer s : ServerHandler.getInstance().getServers()) {
            if (s.getServerInfo() == null) {
                continue;
            }
            if (s.getServerInfo().equals(server)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a Bungeecord server is a sledgehammer server
     *
     * @param name The name of the Bungeecord server
     * @return True if the Bungeecord server is a Sledgehammer server, False if not
     * @deprecated As of release 0.4, replaced by {@link #isSledgehammerServer(ServerInfo)}
     */
    @Deprecated
    public static boolean isSledgehammerServer(String name) {
        for (SledgehammerServer s : ServerHandler.getInstance().getServers()) {
            if (s.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if player is within a build region
     *
     * @param player The player
     * @return True if they are within the region, false if not
     */
    public static boolean inEarthRegion(SledgehammerPlayer player) {
        double[] geo = toGeo(player.getLocation().getX(), player.getLocation().getZ());
        return !(geo == null || geo.length < 1 || Double.isNaN(geo[0]) || Double.isNaN(geo[1]));
    }

    /**
     * Checks if an incoming request matches the Sledgehammer authentication code
     *
     * @param u The incoming authentication code
     * @return True if codes math, false if not
     */
    public static boolean isGenuineRequest(String u) {
        try {
            return u.equals(ConfigHandler.authenticationCode);
        } catch (Exception e) {
            Sledgehammer.logger.info("Error occurred while parsing incoming authentication command!");
            return false;
        }
    }

    /**
     * Gets all objects in a string array above a given index
     *
     * @param args  Initial array
     * @param index Starting index
     * @return Selected array
     */
    public static String[] selectArray(String[] args, int index) {
        List<String> array = new ArrayList<>(Arrays.asList(args).subList(index, args.length));
        return array.toArray(array.toArray(new String[0]));
    }

    /**
     * Gets a space seperated string from an array
     *
     * @param args A string array
     * @return The space seperated String
     */
    public static String getRawArguments(String[] args) {
        if (args.length == 0) {
            return "";
        }
        if (args.length == 1) {
            return args[0];
        }

        StringBuilder arguments = new StringBuilder(args[0]);

        for (int x = 1; x < args.length; x++) {
            arguments.append(" ").append(args[x]);
        }

        return arguments.toString();
    }

    /**
     * Generates a random 6-character string
     *
     * @return Random string
     */
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    public String getLastArgument(String[] a) {
        return a.length == 0 ? null : a[a.length - 1];
    }


    public static <T extends Collection<? super String>> T copyPartialMatches(@NonNull final String token, @NonNull final Iterable<String> originals, @NonNull final T collection) throws UnsupportedOperationException, IllegalArgumentException {
        for (String string : originals) {
            if (startsWithIgnoreCase(string, token)) {
                collection.add(string);
            }
        }

        return collection;
    }

    public static boolean startsWithIgnoreCase(@NonNull final String string, final String prefix) throws IllegalArgumentException, NullPointerException {
        if (string.length() < prefix.length()) {
            return false;
        }
        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
