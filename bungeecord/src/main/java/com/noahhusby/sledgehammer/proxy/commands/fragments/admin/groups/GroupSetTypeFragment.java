/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GroupSetHeadFragment.java
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

import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.common.warps.WarpGroupType;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.Locale;

public class GroupSetTypeFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha group settype <id> <group/server>"));
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Group [default] ", ChatColor.GRAY, "is a group of singular warps / waypoints."));
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Server ", ChatColor.GRAY, "is a collection of warps from a group of servers."));
            return;
        }

        String ID = args[0];
        String type = args[1].toLowerCase(Locale.ROOT);
        WarpGroup group = WarpHandler.getInstance().getWarpGroups().get(ID);

        if (group == null) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "That group doesn't exist!"));
            return;
        }

        if (type.equalsIgnoreCase("group") || type.equalsIgnoreCase("server")) {
            WarpGroupType warpGroupType = WarpGroupType.valueOf(type.toUpperCase(Locale.ROOT));
            sender.sendMessage(ChatUtil.getValueMessage("type", warpGroupType.name(), group.getId()));
            group.setType(warpGroupType);
            WarpHandler.getInstance().getWarpGroups().saveAsync();
        } else {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha group settype <id> <group/server>"));
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Group [default] ", ChatColor.GRAY, "is a group of singular warps / waypoints."));
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Server ", ChatColor.GRAY, "is a collection of warps from a group of servers."));
        }
    }

    @Override
    public String getName() {
        return "settype";
    }

    @Override
    public String getPurpose() {
        return "Set type of a warp group";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "<id>", "<group/server>" };
    }
}
