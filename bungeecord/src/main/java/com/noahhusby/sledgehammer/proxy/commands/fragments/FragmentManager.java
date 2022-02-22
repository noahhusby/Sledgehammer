/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
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

package com.noahhusby.sledgehammer.proxy.commands.fragments;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import lombok.AllArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/**
 * @author Noah Husby
 */
@AllArgsConstructor
public class FragmentManager {
    @Setter
    private String base;
    private final String title;
    private final Map<String, ICommandFragment> fragments = Maps.newHashMap();

    public void register(ICommandFragment fragment) {
        fragments.put(fragment.getName(), fragment);
    }

    public void execute(CommandSender sender, String[] args) {
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

    public void execute(CommandSender sender, String[] args, int index) {
        if (args.length > index) {
            if (index == 0) {
                execute(sender, args);
            } else {
                ICommandFragment fragment = fragments.get(args[index].toLowerCase(Locale.ROOT));
                if (fragment != null) {
                    fragment.execute(sender, args);
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
        ChatUtil.sendMessageBox(sender, title, () -> {
            for (Map.Entry<String, ICommandFragment> e : fragments.entrySet()) {
                ICommandFragment f = e.getValue();
                TextComponent message = ChatUtil.combine(ChatColor.YELLOW, base);
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
        });
    }
}
