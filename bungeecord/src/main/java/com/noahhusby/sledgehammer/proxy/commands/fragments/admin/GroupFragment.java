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
