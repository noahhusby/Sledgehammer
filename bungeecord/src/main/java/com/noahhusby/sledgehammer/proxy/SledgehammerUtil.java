/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.proxy;

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.common.CommonUtil;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
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
     * Checks if a Bungeecord server is a sledgehammer server
     *
     * @param server The Bungeecord server
     * @return True if the Bungeecord server is a Sledgehammer server, False if not
     */
    public static boolean isSledgehammerServer(ServerInfo server) {
        return ServerHandler.getInstance().getServers().containsKey(server.getName());
    }

    /**
     * Gets a space seperated string from an array
     *
     * @param args A string array
     * @return The space seperated String
     */
    public static String getRawArguments(String[] args) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(args).forEach(s -> builder.append(" ").append(s));
        return builder.toString().trim();
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

    public static List<String> copyPartialMatches(@NonNull final String token, @NonNull final Iterable<String> originals) throws UnsupportedOperationException, IllegalArgumentException {
        List<String> collection = Lists.newArrayList();
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
