/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - EventCommand.java
 * All rights reserved.
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
