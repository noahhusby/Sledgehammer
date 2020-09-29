/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerAddLocationFragment.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.dialogs.scenes.location.LocationSelectionScene;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.packet.Chat;

public class ServerAddLocationFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatConstants.issueByPlayer);
            return;
        }
        DialogHandler.getInstance().startDialog(sender, new LocationSelectionScene(ProxyServer.getInstance().getServerInfo(args[0])));
    }

    @Override
    public String getName() {
        return "addlocation";
    }

    @Override
    public String getPurpose() {
        return "Add a location to the server";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
