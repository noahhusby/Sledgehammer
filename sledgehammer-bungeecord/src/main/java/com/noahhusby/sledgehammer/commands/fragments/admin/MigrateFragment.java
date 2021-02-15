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

package com.noahhusby.sledgehammer.commands.fragments.admin;

import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class MigrateFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Type '", ChatColor.GRAY),
                    new TextElement("/sha migrate ", ChatColor.YELLOW), new TextElement("confirm", ChatColor.RED),
                    new TextElement("' ", ChatColor.GRAY), new TextElement("to migrate local storage.", ChatColor.GRAY)));
            return;
        }

        if(args[0].equalsIgnoreCase("confirm")) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Succesfully migrated data!", ChatColor.BLUE)));
            ConfigHandler.getInstance().migrate();
        } else {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Type '", ChatColor.GRAY),
                    new TextElement("/sha migrate ", ChatColor.YELLOW), new TextElement("confirm", ChatColor.RED),
                    new TextElement("' ", ChatColor.GRAY), new TextElement("to migrate local storage.", ChatColor.GRAY)));
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
