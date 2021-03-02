/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - TplloCommand.java
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

package com.noahhusby.sledgehammer.proxy.commands;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.network.P2S.P2SLocationPacket;
import com.noahhusby.sledgehammer.proxy.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.proxy.permissions.PermissionRequest;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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

        PermissionHandler.getInstance().check(SledgehammerPlayer.getPlayer(sender), "sledgehammer.tpllo", (code, global) -> {
            if (code == PermissionRequest.PermissionCode.PERMISSION) {
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
