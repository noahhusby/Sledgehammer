/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GroupInfoFragment.java
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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin.groups;

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.warp.WarpGroup;
import com.noahhusby.sledgehammer.proxy.warp.WarpGroupType;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class GroupInfoFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha group info <id>"));
            return;
        }

        String gn = args[0];
        WarpGroup group = null;

        for (WarpGroup g : WarpHandler.getInstance().getWarpGroups().values()) {
            if (g.getId().equalsIgnoreCase(gn)) {
                group = g;
            }
        }

        if (group == null) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "That group doesn't exist"));
            return;
        }

        sender.sendMessage();
        WarpGroup finalGroup = group;
        ChatUtil.sendMessageBox(sender, ChatColor.AQUA + "" + ChatColor.BOLD + "Warp Group Report", () -> {
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Id: ", ChatColor.WHITE, finalGroup.getId()));
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Name: ", ChatColor.WHITE, finalGroup.getName()));
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Type: ", ChatColor.WHITE, finalGroup.getType().name()));
            if(finalGroup.getType() == WarpGroupType.SERVER) {
                sender.sendMessage();
                sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Servers: ", ChatColor.WHITE, String.join(ChatColor.GRAY + ", " + ChatColor.WHITE, finalGroup.getServers())));
            } else {
                sender.sendMessage();
                List<String> warpNames = Lists.newArrayList();
                for(Integer warpId : finalGroup.getWarps()) {
                    Warp warp = WarpHandler.getInstance().getWarp(warpId);
                    if(warp != null) {
                        warpNames.add(warp.getName());
                    }
                }
                sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Warps: ", ChatColor.WHITE, String.join(ChatColor.GRAY + ", " + ChatColor.WHITE, warpNames)));
            }
        });
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPurpose() {
        return "See info about a warp group";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "<id>" };
    }
}
