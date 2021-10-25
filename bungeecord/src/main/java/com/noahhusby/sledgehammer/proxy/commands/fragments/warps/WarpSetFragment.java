/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpSetFragment.java
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

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.concurrent.CompletableFuture;

public class WarpSetFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(sender);
        CompletableFuture<Permission> permissionFuture = player.getPermission("sledgehammer.warp.set");
        permissionFuture.thenAccept(permission -> {
            if (permission.isLocal()) {
                if (args.length == 0) {
                    sender.sendMessage(ChatUtil.combine(ChatColor.RED, String.format("Usage: /%s set <name>", ConfigHandler.warpCommand)));
                    return;
                }
                WarpHandler.WarpStatus warpStatus = WarpHandler.getInstance().getWarpStatus(args[0], player.getServer().getInfo().getName());

                switch (warpStatus) {
                    case EXISTS:
                        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "A warp with that name already exists!"));
                        sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Use the warp GUI to move it's location."));
                        break;
                    case RESERVED:
                        if (permission.isGlobal()) {
                            sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "A warp with that name already exists!"));
                            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Use the warp GUI to move it's location."));
                            return;
                        }
                        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "This warp name is reserved and cannot be used."));
                        break;
                    case AVAILABLE:
                        WarpHandler.getInstance().requestNewWarp(args[0], sender);
                        break;
                }
            } else {
                sender.sendMessage(ChatUtil.getNoPermission());
            }
        });
    }

    @Override
    public String getName() {
        return "set";
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
