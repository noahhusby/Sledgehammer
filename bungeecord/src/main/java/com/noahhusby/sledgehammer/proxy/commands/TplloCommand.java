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

import com.noahhusby.sledgehammer.common.exceptions.InvalidCoordinatesException;
import com.noahhusby.sledgehammer.common.utils.LatLngHeight;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
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
                if (a.length == 0) {
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "Usage: /tpllo <lat> <lon> [alt]"));
                    return;
                }

                try {
                    LatLngHeight coords = SledgehammerUtil.parseCoordinates(a);
                    ServerInfo server = ((ProxiedPlayer) sender).getServer().getInfo();
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "(Override) Teleporting to ",
                            ChatColor.BLUE, String.format("%s, %s", coords.getLat(), coords.getLon())));
                    getNetworkManager().send(new P2SLocationPacket(sender.getName(), server.getName(), coords));
                } catch (InvalidCoordinatesException e) {
                    e.printStackTrace();
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "Usage: /tpllo <lat> <lon> [alt]"));
                }
                return;
            }
            sender.sendMessage(ChatUtil.getNoPermission());
        });
    }
}
