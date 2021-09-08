/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerAdminCommand.java
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

package com.noahhusby.sledgehammer.proxy.commands;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.FragmentManager;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.GroupFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.MigrateFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.ReloadFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.ServerFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.SetupFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.TestLocationFragment;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class SledgehammerAdminCommand extends Command {

    private final FragmentManager manager = new FragmentManager("/sha", ChatColor.BLUE + "Sledgehammer");

    public SledgehammerAdminCommand() {
        super("sha", "sledgehammer.admin");
        manager.register(new ReloadFragment());
        manager.register(new SetupFragment());
        manager.register(new ServerFragment());
        manager.register(new GroupFragment());
        manager.register(new TestLocationFragment());
        manager.register(new MigrateFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPerms(sender)) {
            sender.sendMessage(ChatUtil.getNotAvailable());
            return;
        }
        manager.execute(sender, args);
    }
}
