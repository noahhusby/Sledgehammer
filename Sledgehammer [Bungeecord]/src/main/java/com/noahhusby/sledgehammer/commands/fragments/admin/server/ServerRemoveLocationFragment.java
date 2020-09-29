/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerRemoveLocationFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.dialogs.scenes.setup.LocationRemovalScene;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerRemoveLocationFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(ServerConfig.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatConstants.notSledgehammerServer);
            return;
        }

        if(!ServerConfig.getInstance().getServer(args[0]).earthServer) {
            sender.sendMessage(ChatConstants.notEarthServer);
            return;
        }

        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatConstants.issueByPlayer);
            return;
        }
        DialogHandler.getInstance().startDialog(sender, new LocationRemovalScene(ProxyServer.getInstance().getServerInfo(args[0]), null));
    }

    @Override
    public String getName() {
        return "removelocation";
    }

    @Override
    public String getPurpose() {
        return "Remove a location on the server";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
