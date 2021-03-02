/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - FragmentManager.java
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

package com.noahhusby.sledgehammer.proxy.commands.fragments;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class FragmentManager {

    public FragmentManager(String command) {
        this.commandBase = String.format("%s", command);
    }

    private final Map<String, ICommandFragment> fragments = Maps.newHashMap();
    private final String commandBase;

    protected void register(ICommandFragment c) {
        fragments.put(c.getName(), c);
    }

    protected void executeFragment(CommandSender sender, String[] args) {
        if (args.length != 0) {
            ICommandFragment fragment = fragments.get(args[0].toLowerCase(Locale.ROOT));
            if (fragment != null) {
                ArrayList<String> dataList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
                String[] data = dataList.toArray(new String[0]);
                fragment.execute(sender, data);
                return;
            }
        }
        displayCommands(sender);
    }

    protected void executeFragment(CommandSender sender, String[] args, int index) {
        if (args.length > index) {
            if (index == 0) {
                executeFragment(sender, args);
            } else {
                ICommandFragment fragment = fragments.get(args[index].toLowerCase(Locale.ROOT));
                if (fragment != null) {
                    ArrayList<String> dataList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
                    String[] data = dataList.toArray(new String[0]);
                    fragment.execute(sender, data);
                    return;
                }
            }
        }
        displayCommands(sender);
    }


    private void displayCommands(CommandSender sender) {
        for (int i = 0; i < 2; i++) {
            sender.sendMessage();
        }

        sender.sendMessage(ChatUtil.combine(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH, "==============",
                ChatColor.DARK_GREEN + "" + ChatColor.BLUE, " Sledgehammer ", ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH, "=============="));
        sender.sendMessage(ChatUtil.combine(""));

        for (ICommandFragment f : fragments.values()) {
            TextComponent message = ChatUtil.combine(ChatColor.YELLOW, commandBase);
            message.addExtra(ChatUtil.combine(ChatColor.GREEN, " ", f.getName(), " "));
            if (f.getArguments() != null) {
                for (int x = 0; x < f.getArguments().length; x++) {
                    String argument = f.getArguments()[x];
                    if (argument.startsWith("<")) {
                        message.addExtra(ChatUtil.combine(ChatColor.RED, argument + " "));
                    } else {
                        message.addExtra(ChatUtil.combine(ChatColor.GRAY, argument + " "));
                    }
                }
            }
            message.addExtra(ChatUtil.combine(ChatColor.GRAY, "- ", ChatColor.BLUE, f.getPurpose()));
            sender.sendMessage(message);
        }
        sender.sendMessage(ChatUtil.combine(""));
        sender.sendMessage(ChatUtil.combine(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH, "========================================="));
    }
}
