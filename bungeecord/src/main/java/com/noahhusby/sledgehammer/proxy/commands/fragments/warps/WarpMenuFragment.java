/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpMenuFragment.java
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

package com.noahhusby.sledgehammer.proxy.commands.fragments.warps;

import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.commands.WarpCommand;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.P2S.P2SWarpGUIPacket;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.CommandSender;

import java.util.concurrent.CompletableFuture;

public class WarpMenuFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        SledgehammerServer server = SledgehammerPlayer.getPlayer(sender).getSledgehammerServer();
        if (server == null) {
            new WarpCommand("").execute(sender, new String[]{});
            return;
        }

        if (!server.isInitialized()) {
            new WarpCommand("").execute(sender, new String[]{});
            return;
        }
        CompletableFuture<Permission> permissionFuture = SledgehammerPlayer.getPlayer(sender).getPermission("sledgehammer.warp.edit");
        permissionFuture.thenAccept(permission -> NetworkHandler.getInstance().send(new P2SWarpGUIPacket(sender.getName(), SledgehammerUtil.getServerFromSender(sender).getName(), permission.isLocal())));
    }

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public String getPurpose() {
        return "";
    }

    @Override
    public String[] getArguments() {
        return new String[]{};
    }
}
