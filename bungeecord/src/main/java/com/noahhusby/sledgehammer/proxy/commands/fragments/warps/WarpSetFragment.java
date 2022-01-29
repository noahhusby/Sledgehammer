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

package com.noahhusby.sledgehammer.proxy.commands.fragments.warps;

import com.noahhusby.sledgehammer.proxy.utils.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
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
                    sender.sendMessage(ChatUtil.combine(ChatColor.RED, String.format("Usage: /%s set <name>", SledgehammerConfig.warps.warpCommand)));
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
