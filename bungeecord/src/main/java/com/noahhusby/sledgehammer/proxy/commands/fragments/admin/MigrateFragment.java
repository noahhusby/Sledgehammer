/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - MigrateFragment.java
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
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class MigrateFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Type ", ChatColor.YELLOW, "/sha migrate confirm ", ChatColor.GRAY, "to migrate local storage."));
            return;
        }

        if(args[0].equalsIgnoreCase("confirm")) {
            ConfigHandler.getInstance().migrate();
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.BLUE, "Successfully migrated data!"));
        } else {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Type ", ChatColor.YELLOW, "/sha migrate confirm ", ChatColor.GRAY, "to migrate local storage."));
        }
    }

    @Override
    public String getName() {
        return "migrate";
    }

    @Override
    public String getPurpose() {
        return "Migrate local storage to remote databases";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
