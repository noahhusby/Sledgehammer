/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PermissionCheckFragment.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.commands.fragments.admin;

import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PermissionCheckFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p;
        if(args.length == 0) {
            p = ProxyServer.getInstance().getPlayer(sender.getName());
        } else {
            p = ProxyServer.getInstance().getPlayer(args[0]);
        }

        if(p == null) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement(args[0], ChatColor.RED),
                    new TextElement(" could not be found on the network!", ChatColor.DARK_RED)));
            return;
        }

        sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Permissions for ", ChatColor.GRAY),
                new TextElement(p.getName(), ChatColor.BLUE)));

        for(String s : p.getPermissions()) {
            if(s.contains("sledgehammer")) sender.sendMessage(ChatHelper.makeTextComponent(new TextElement(s, ChatColor.GOLD)));
        }
    }

    @Override
    public String getName() {
        return "pcheck";
    }

    @Override
    public String getPurpose() {
        return "List all sledgehammer permissions for a given player";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"[player]"};
    }
}
