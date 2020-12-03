/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpCommand.java
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

package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import com.noahhusby.sledgehammer.maps.MapHandler;
import com.noahhusby.sledgehammer.network.P2S.P2STeleportPacket;
import com.noahhusby.sledgehammer.network.P2S.P2SWarpGUIPacket;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class WarpCommand extends Command {

    public WarpCommand() {
        super(ConfigHandler.warpCommand, "sledgehammer.warp");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatConstants.issueByPlayer);
            return;
        }

        if(!hasGeneralPermission(sender)) {
            sender.sendMessage(ChatConstants.noPermission);
            return;
        }

        SledgehammerServer sledgehammerServer = ServerConfig.getInstance().getServer(SledgehammerUtil.getServerNameByPlayer(sender));

        if(args.length < 1) {
            if(sledgehammerServer != null) {
                if(ConfigHandler.warpMode.equalsIgnoreCase("gui") && sledgehammerServer.isInitialized()) {
                    getNetworkManager().sendPacket(new P2SWarpGUIPacket(sender.getName(), SledgehammerUtil.getServerNameByPlayer(sender)));
                    return;
                }
            }
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Usage: /"+ConfigHandler.warpCommand+" <warp>", ChatColor.RED)));
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Use ", ChatColor.GRAY),
                    new TextElement("/"+ConfigHandler.warpCommand+" list", ChatColor.BLUE),new TextElement(" to see the available warps.", ChatColor.GRAY)));
            return;
        }

        if(args[0].equals("set")) {
            if(!hasPermissionAdmin(sender)) {
                sender.sendMessage(ChatConstants.noPermission);
                return;
            }

            if(args.length < 2) {
                sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Use ", ChatColor.GRAY),
                                new TextElement("/"+ConfigHandler.warpCommand+" set <warp name> [pinned:<true/false>]", ChatColor.BLUE), new TextElement(" to set a warp.", ChatColor.GRAY)));
                return;
            }

            if(args.length == 2) {
                WarpHandler.getInstance().requestNewWarp(args[1], sender, false);
                return;
            }

            WarpHandler.getInstance().requestNewWarp(args[1], sender, Boolean.parseBoolean(args[2]));
        } else if(args[0].equals("delete") || args[0].equals("remove")) {
            if (!hasPermissionAdmin(sender)) {
                sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("You don't have permission to run this command!", ChatColor.DARK_RED)));
                return;
            }

            if (args.length < 2) {
                sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Use ", ChatColor.GRAY),
                        new TextElement("/"+ConfigHandler.warpCommand+" remove <warp name>", ChatColor.BLUE), new TextElement(" to remove set a warp.", ChatColor.GRAY)));

                return;
            }
            WarpHandler.getInstance().removeWarp(args[1], sender);
        } else if(args[0].equals("list")) {
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Warps: ", ChatColor.GRAY),
                    new TextElement(WarpHandler.getInstance().getWarpList(), ChatColor.RED)));
        } else if(args[0].equals("map")) {
            MapHandler.getInstance().newMapCommand(sender);
        } else if(args[0].equalsIgnoreCase("gui")) {
            if(sledgehammerServer == null) {
                execute(sender, new String[]{});
                return;
            }

            if(!sledgehammerServer.isInitialized()) {
                execute(sender, new String[]{});
                return;
            }

            getNetworkManager().sendPacket(new P2SWarpGUIPacket(sender.getName(), SledgehammerUtil.getServerNameByPlayer(sender)));
        } else {
            Warp warp = WarpHandler.getInstance().getWarp(args[0]);
            if(warp == null) {
                sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Error: Warp not found", ChatColor.RED)));
                return;
            }

            if(ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo() != ProxyServer.getInstance().getServerInfo(warp.server)) {
                ProxyServer.getInstance().getPlayer(sender.getName()).connect(ProxyServer.getInstance().getServerInfo(warp.server));
                sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Sending you to ", ChatColor.GRAY), new TextElement(warp.server, ChatColor.RED)));
            }

            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Warping to ", ChatColor.GRAY), new TextElement(args[0], ChatColor.RED)));
            getNetworkManager().sendPacket(new P2STeleportPacket(sender.getName(), warp.server, warp.point));
        }
    }
}
