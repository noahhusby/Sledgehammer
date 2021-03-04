package com.noahhusby.sledgehammer.proxy.commands.fragments;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FragmentManager {
    private final String base;
    private final String title;

    private final Map<String, ICommandFragment> fragments = Maps.newHashMap();

    public void register(ICommandFragment fragment) {
        fragments.put(fragment.getName(), fragment);
    }

    public void execute(CommandSender sender, String[] args) {
        if(args.length != 0) {
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
