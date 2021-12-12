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

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.common.warps.WarpGroupType;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
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
            if (finalGroup.getType() == WarpGroupType.SERVER) {
                sender.sendMessage();
                sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Servers: ", ChatColor.WHITE, String.join(ChatColor.GRAY + ", " + ChatColor.WHITE, finalGroup.getServers())));
            } else {
                sender.sendMessage();
                List<String> warpNames = Lists.newArrayList();
                for (Integer warpId : finalGroup.getWarps()) {
                    Warp warp = WarpHandler.getInstance().getWarp(warpId);
                    if (warp != null) {
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
