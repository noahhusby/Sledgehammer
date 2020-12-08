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
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.fragments.warps.*;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.network.P2S.P2STeleportPacket;
import com.noahhusby.sledgehammer.network.P2S.P2SWarpGUIPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.permissions.PermissionRequest;
import com.noahhusby.sledgehammer.permissions.PermissionResponse;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.Warp;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand extends WarpFragmentManager {
    public WarpCommand(String name) {
        super(name, "sledgehammer.warp");
        registerCommandFragment(new WarpSetFragment());
        registerCommandFragment(new WarpRemoveFragment());
        registerCommandFragment(new WarpListFragment());
        registerCommandFragment(new WarpMenuFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatConstants.issueByPlayer);
            return;
        }

        if(executeFragment(sender, args)) return;

        boolean local = ConfigHandler.localWarp;
        boolean hasPerms = PermissionHandler.getInstance().isAdmin(sender) || hasPerms(sender);
        if(local && !hasPerms) {
            PermissionHandler.getInstance().check(code -> {
                if(code == PermissionRequest.PermissionCode.PERMISSION) {
                    run(sender, args);
                    return;
                }
                sender.sendMessage(ChatConstants.noPermission);
            }, SledgehammerPlayer.getPlayer(sender), "sledgehammer.warp");
        } else {
            run(sender, args);
        }
    }

    private void run(CommandSender sender, String[] args) {



        if(args.length == 0) {
            if(ConfigHandler.warpMenuDefault) {
                SledgehammerServer server = SledgehammerPlayer.getPlayer(sender).getSledgehammerServer();
                boolean openGUI = (server != null);

                if(openGUI)
                    if(!server.isInitialized()) openGUI = false;

                if(openGUI) {
                    if(PermissionHandler.getInstance().isAdmin(sender) || sender.hasPermission("sledgehammer.warp.edit")) {
                        SledgehammerNetworkManager.getInstance().send(new P2SWarpGUIPacket(sender.getName(),
                                SledgehammerUtil.getServerFromSender(sender).getName(), true));
                    } else {
                        PermissionHandler.getInstance().check(code ->
                                SledgehammerNetworkManager.getInstance().send(new P2SWarpGUIPacket(sender.getName(),
                                SledgehammerUtil.getServerFromSender(sender).getName(),
                                code == PermissionRequest.PermissionCode.PERMISSION)),
                                SledgehammerPlayer.getPlayer(sender), "sledgehammer.warp.edit");
                    }
                    return;
                }
            }
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Usage: /nwarp <warp>", ChatColor.RED)));
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Use ", ChatColor.GRAY),
                    new TextElement(String.format("/%s list", ConfigHandler.warpCommand), ChatColor.BLUE),
                    new TextElement(" to see the available warps.", ChatColor.GRAY)));
            return;
        }

        List<Warp> warps = new ArrayList<>();
        for(Warp w : WarpHandler.getInstance().getWarps())
            if(w.getPinnedMode() == Warp.PinnedMode.GLOBAL || !ConfigHandler.localWarp
            || w.getServer().equalsIgnoreCase(SledgehammerUtil.getServerFromSender(sender).getName())) warps.add(w);

        String warpName = args[0];
        Warp warp = null;
        for(Warp w : warps)
            if(w.getName().equalsIgnoreCase(warpName)) warp = w;

        if(warp == null) {
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("That warp does not exist!", ChatColor.RED)));
            return;
        }

        if(SledgehammerUtil.getServerFromSender(sender) != SledgehammerUtil.getServerByName(warp.getServer())) {
            SledgehammerPlayer.getPlayer(sender).connect(SledgehammerUtil.getServerByName(warp.getServer()));
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Sending you to ", ChatColor.GRAY), new TextElement(warp.getServer(), ChatColor.RED)));
        }

        sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Warping to ", ChatColor.GRAY), new TextElement(warp.getName(), ChatColor.RED)));
        getNetworkManager().send(new P2STeleportPacket(sender.getName(), warp.getServer(), warp.getPoint()));
    }
}
