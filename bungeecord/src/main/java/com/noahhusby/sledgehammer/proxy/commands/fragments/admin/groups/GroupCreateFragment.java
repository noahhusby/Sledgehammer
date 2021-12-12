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
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class GroupCreateFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha group create <id> [name]"));
            return;
        }

        String ID = args[0];

        if (WarpHandler.getInstance().getWarpGroups().containsKey(ID)) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "A group with that name already exists!"));
            return;
        }

        if (args.length == 1) {
            WarpGroup group = new WarpGroup();
            group.setId(ID);
            WarpHandler.getInstance().getWarpGroups().put(group.getId(), group);
            WarpHandler.getInstance().getWarpGroups().saveAsync();
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Successfully created new warp group", ChatColor.YELLOW, group.getId()));
            return;
        }

        StringBuilder name = new StringBuilder(args[1]);
        for (int i = 2; i < args.length; i++) {
            name.append(" ").append(args[i]);
        }

        WarpGroup group = new WarpGroup();
        group.setId(ID);
        group.setName(name.toString());
        sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Successfully created new warp group ", ChatColor.YELLOW, group.getId(),
                ChatColor.GRAY, " with name ", ChatColor.RED, name.toString()));
        WarpHandler.getInstance().getWarpGroups().put(group.getId(), group);
        WarpHandler.getInstance().getWarpGroups().saveAsync();
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getPurpose() {
        return "Create a new warp group";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "<id>", "[name]" };
    }
}
