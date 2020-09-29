/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - TpllCommand.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.network.P2S.P2SLocationPacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TplloCommand extends Command {

    public TplloCommand() {
        super("tpllo", "sledgehammer.tpllo");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatConstants.issueByPlayer);
            return;
        }

        if(!hasAdmin(sender)) {
            sender.sendMessage(ChatConstants.noPermission);
            return;
        }

        if(args.length==0) {
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Usage: /tpllo <lat> <lon>", ChatColor.RED)));
            return;
        }

        String[] splitCoords = args[0].split(",");
        String alt = null;
        if(splitCoords.length==2&&args.length<3) { // lat and long in single arg
            if(args.length>1) alt = args[1];
            args = splitCoords;
        } else if(args.length==3) {
            alt = args[2];
        }
        if(args[0].endsWith(","))
            args[0] = args[0].substring(0, args[0].length() - 1);
        if(args.length>1&&args[1].endsWith(","))
            args[1] = args[1].substring(0, args[1].length() - 1);
        if(args.length!=2&&args.length!=3) {
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Usage: /tpllo <lat> <lon>", ChatColor.RED)));
            return;
        }

        double lon;
        double lat;

        try {
            lat = Double.parseDouble(args[0]);
            lon = Double.parseDouble(args[1]);
        } catch(Exception e) {
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Usage: /tpllo <lat> <lon>", ChatColor.RED)));
            return;
        }

        ServerInfo server = ((ProxiedPlayer) sender).getServer().getInfo();

        sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("(Override) Teleporting to ", ChatColor.GRAY),
                new TextElement(lat+", "+lon, ChatColor.RED)));

        getNetworkManager().sendPacket(new P2SLocationPacket(sender.getName(), server.getName(), String.valueOf(lat), String.valueOf(lon)));
    }
}
