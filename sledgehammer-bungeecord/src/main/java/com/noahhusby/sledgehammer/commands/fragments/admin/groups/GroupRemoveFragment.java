/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GroupCreateFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.admin.groups;

import com.noahhusby.sledgehammer.ChatUtil;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerHandler;
import com.noahhusby.sledgehammer.config.ServerGroup;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class GroupRemoveFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha group remove <id>"));
            return;
        }

        String ID = args[0];
        ServerGroup group = null;
        for(ServerGroup sg : ServerHandler.getInstance().getGroups())
            if(sg.getID().equals(ID)) group = sg;

        if(group == null) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "That group doesn't exist!"));
            return;
        }

        sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Successfully removed group ", ChatColor.BLUE, group.getID()));
        ServerHandler.getInstance().getGroups().remove(group);
        ServerHandler.getInstance().getGroups().save(true);
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getPurpose() {
        return "Remove a group";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<id>"};
    }
}
