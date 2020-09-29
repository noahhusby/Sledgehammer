/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - EventCommand.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class EventCommand extends Command {
    public EventCommand(String name) {
        super("event");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            if(args.length < 1) {
                if(sender.hasPermission("sledgehammer.event.admin")) {
                    sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Usage: /event <info|go|admin>", ChatColor.RED)));
                    return;
                }
                sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Usage: /event <info|go>", ChatColor.RED)));
                return;
            }

            if(args[0].equals("admin")) {
                if(!sender.hasPermission("sledgehammer.event.admin")) {
                    sender.sendMessage(ChatConstants.noPermission);
                    return;
                }
            }
        }
    }
}
