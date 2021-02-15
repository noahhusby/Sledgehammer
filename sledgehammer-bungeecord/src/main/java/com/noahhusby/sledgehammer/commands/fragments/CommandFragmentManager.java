/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CommandFragmentManager.java
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

package com.noahhusby.sledgehammer.commands.fragments;

import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandFragmentManager extends Command {

    public CommandFragmentManager(String name, String node) {
        this(name, node, new String[]{});
    }

    public CommandFragmentManager(String name, String node, String[] alias) {
        super(name, node, alias);
        registerCommandFragment(new ICommandFragment() {
            @Override
            public void execute(CommandSender sender, String[] args) {
                displayCommands(sender, args);
            }

            @Override
            public String getName() {
                return "help";
            }

            @Override
            public String getPurpose() {
                return "List all commands";
            }

            @Override
            public String[] getArguments() {
                return new String[]{"[page]"};
            }
        });

    }

    private final List<ICommandFragment> commandFragments = new ArrayList<>();
    private String title = "";
    private String commandBase = "";

    protected void registerCommandFragment(ICommandFragment c) {
        commandFragments.add(c);
    }

    protected void setTitle(String t) {
        this.title = " "+t;
    }

    protected void setCommandBase(String b) {
        this.commandBase = "/"+b+" ";
    }

    protected void executeFragment(CommandSender sender, String[] args) {
        if (args.length != 0) {
            ArrayList<String> dataList = new ArrayList<>();
            for (int x = 1; x < args.length; x++) dataList.add(args[x]);

            String[] data = dataList.toArray(new String[dataList.size()]);
            for (ICommandFragment f : commandFragments) {
                if (f.getName().equals(args[0].toLowerCase())) {
                    f.execute(sender, data);
                    return;
                }
            }
        }
        displayCommands(sender, args);
    }

    private void displayCommands(CommandSender sender, String[] args) {
        int page = 1;
        if(args != null) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (Exception e) { }
            if(page > Math.ceil(commandFragments.size() / 7.0)) page = 1;
        }

        sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement(title + ":", ChatColor.GRAY)));
        for(int xf = (page - 1) * 7; xf < Math.min(((page - 1) * 7) + 7, commandFragments.size()); xf++) {
            ICommandFragment f = commandFragments.get(xf);

            TextComponent message = ChatHelper.makeTextComponent(new TextElement(commandBase, ChatColor.YELLOW));
            message.addExtra(ChatHelper.makeTextComponent(new TextElement(f.getName() + " ", ChatColor.GREEN)));
            if(f.getArguments() != null) {
                for(int x = 0; x < f.getArguments().length; x++) {
                    String argument = f.getArguments()[x];
                    if(argument.startsWith("<")) {
                        message.addExtra(ChatHelper.makeTextComponent(new TextElement(argument + " ", ChatColor.RED)));
                    } else {
                        message.addExtra(ChatHelper.makeTextComponent(new TextElement(argument + " ", ChatColor.GRAY)));
                    }
                }
            }
            message.addExtra(ChatHelper.makeTextComponent(new TextElement("- ", ChatColor.GRAY),
                    new TextElement(f.getPurpose(), ChatColor.BLUE)));

            sender.sendMessage(message);
        }

        if(Math.ceil(commandFragments.size() / 7.0) < 2) return;

        String end = page >= Math.ceil(commandFragments.size() / 7.0) ? "Use '" + commandBase + "help " + (page - 1) + "' to see the previous page."
                : "Use '" + commandBase + "help " + (page + 1) + "' to see the next page.";
        sender.sendMessage(ChatHelper.makeTextComponent(new TextElement(end, ChatColor.GOLD)));
    }

}
