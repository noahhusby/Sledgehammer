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

package com.noahhusby.sledgehammer.proxy.commands;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.network.p2s.P2SLocationPacket;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.CompletableFuture;

public class TplloCommand extends Command {

    public TplloCommand() {
        super("tpllo", "sledgehammer.tpllo");
    }

    @Override
    public void execute(CommandSender sender, String[] a) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatUtil.getPlayerOnly());
            return;
        }

        if (!isAllowed(sender)) {
            sender.sendMessage(ChatUtil.getNotAvailable());
            return;
        }

        CompletableFuture<Permission> permissionFuture = SledgehammerPlayer.getPlayer(sender).getPermission("sledgehammer.tpll");
        permissionFuture.thenAccept(permission -> {
            if (permission.isLocal()) {
                String[] args = a;
                if (args.length == 0) {
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "Usage: /tpllo <lat> <lon>"));
                    return;
                }

                String[] splitCoords = args[0].split(",");
                if (splitCoords.length == 2 && args.length < 3) { // lat and long in single arg
                    args = splitCoords;
                }

                if (args[0].endsWith(",")) {
                    args[0] = args[0].substring(0, args[0].length() - 1);
                }
                if (args.length > 1 && args[1].endsWith(",")) {
                    args[1] = args[1].substring(0, args[1].length() - 1);
                }
                if (args.length != 2 && args.length != 3) {
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "Usage: /tpllo <lat> <lon>"));
                    return;
                }

                double lon;
                double lat;

                try {
                    lat = Double.parseDouble(args[0]);
                    lon = Double.parseDouble(args[1]);
                } catch (Exception e) {
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "Usage: /tpllo <lat> <lon>"));
                    return;
                }

                ServerInfo server = ((ProxiedPlayer) sender).getServer().getInfo();

                sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "(Override) Teleporting to ",
                        ChatColor.RED, String.format("%s, %s", lat, lon)));

                double[] geo = { lat, lon };

                getNetworkManager().send(new P2SLocationPacket(sender.getName(), server.getName(), geo));
                return;
            }
            sender.sendMessage(ChatUtil.getNoPermission());
        });
    }
}
