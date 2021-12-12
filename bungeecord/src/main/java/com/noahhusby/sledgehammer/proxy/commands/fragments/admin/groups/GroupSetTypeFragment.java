/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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
