/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpSetFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.warps;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.permissions.PermissionRequest;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class WarpSetFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean local = ConfigHandler.localWarp;
        boolean hasPerms = PermissionHandler.getInstance().isAdmin(sender) || (!local && sender.hasPermission("sledgehammer.setwarp"));
        if(local && !hasPerms) {
            PermissionHandler.getInstance().check((code, global) -> {
                run(sender, args, code == PermissionRequest.PermissionCode.PERMISSION, true);
            }, SledgehammerPlayer.getPlayer(sender), "sledgehammer.setwarp");
        } else {
            run(sender, args, hasPerms, local);
        }
    }

    private void run(CommandSender sender, String[] args, boolean permission, boolean local) {
        if(!permission) {
            sender.sendMessage(ChatConstants.noPermission);
            return;
        }

        WarpHandler.WarpStatus warpStatus =
                WarpHandler.getInstance().getWarpStatus(args[0], SledgehammerPlayer.getPlayer(sender).getServer().getInfo().getName());

        switch (warpStatus) {
            case EXISTS:
                sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("A warp with that name already exists! Use ", ChatColor.GRAY),
                        new TextElement("/" + ConfigHandler.warpCommand + " move ", ChatColor.YELLOW), new TextElement("to change it's location.", ChatColor.GRAY)));
                break;
            case RESERVED:
                if(PermissionHandler.getInstance().isAdmin(sender) || sender.hasPermission("sledgehammer.setwarp")) {
                    sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("A warp with that name already exists! Use ", ChatColor.GRAY),
                            new TextElement("/" + ConfigHandler.warpCommand + " move ", ChatColor.YELLOW), new TextElement("to change it's location.", ChatColor.GRAY)));
                    return;
                }
                sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("This warp name is reserved and cannot be used.", ChatColor.RED)));
                break;
            case AVAILABLE:
                WarpHandler.getInstance().requestNewWarp(args[0], sender);
                break;
        }
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getPurpose() {
        return "";
    }

    @Override
    public String[] getArguments() {
        return new String[]{};
    }
}
