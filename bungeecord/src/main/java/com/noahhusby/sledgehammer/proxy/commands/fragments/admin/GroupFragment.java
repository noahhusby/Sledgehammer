/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerFragment.java
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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.FragmentManager;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.groups.GroupCreateFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.groups.GroupInfoFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.groups.GroupListFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.groups.GroupRemoveFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.groups.GroupSetHeadFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.groups.GroupSetNameFragment;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class GroupFragment implements ICommandFragment {

    private final FragmentManager manager = new FragmentManager("/sha group", ChatColor.GREEN + "Groups");

    public GroupFragment() {
        manager.register(new GroupInfoFragment());
        manager.register(new GroupListFragment());
        manager.register(new GroupCreateFragment());
        manager.register(new GroupRemoveFragment());
        manager.register(new GroupSetHeadFragment());
        manager.register(new GroupSetNameFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!PlayerHandler.getInstance().isAdmin(sender)) {
            sender.sendMessage(ChatUtil.getNoPermission());
            return;
        }
        manager.execute(sender, args);
    }

    @Override
    public String getName() {
        return "group";
    }

    @Override
    public String getPurpose() {
        return "Configure warp groups";
    }

    @Override
    public String[] getArguments() {
        return new String[]{};
    }
}
