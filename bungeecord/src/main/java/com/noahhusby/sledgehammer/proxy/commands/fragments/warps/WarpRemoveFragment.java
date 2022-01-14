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

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WarpRemoveFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        CompletableFuture<Permission> permissionFuture = SledgehammerPlayer.getPlayer(sender).getPermission("sledgehammer.warp.remove");
        permissionFuture.thenAccept(permission -> {
            if (permission.isLocal()) {
                run(sender, args, permission.isGlobal());
            } else {
                sender.sendMessage(ChatUtil.getNoPermission());
            }
        });
    }

    private void run(CommandSender sender, String[] args, boolean global) {
        if (args.length < 1) {
            sender.sendMessage(ChatUtil.combine(ChatColor.RED, String.format("Usage: /%s remove <name>", SledgehammerConfig.warps.warpCommand)));
            return;
        }

        List<Warp> rawWarps = WarpHandler.getInstance().getWarps(args[0]);
        List<Warp> warps = Lists.newArrayList();

        for (Warp w : rawWarps) {
            if (global) {
                warps.add(w);
                continue;
            }

            WarpGroup warpGroup = WarpHandler.getInstance().getWarpGroupByServer().get(SledgehammerPlayer.getPlayer(sender).getSledgehammerServer().getName());
            if (warpGroup != null && warpGroup.getServers().contains(w.getServer())) {
                warps.add(w);
            }
        }

        if (warps.isEmpty()) {
            sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "Warp not found!"));
            return;
        }

        if (warps.size() == 1) {
            WarpHandler.getInstance().removeWarp(warps.get(0).getId(), sender);
            return;
        }

        boolean showWarps = (args.length == 1);

        if (!showWarps) {
            try {
                int val = Integer.parseInt(args[1]);
                if (val < 0 || val > warps.size()) {
                    showWarps = true;
                }
            } catch (Exception e) {
                showWarps = true;
            }
        }

        if (showWarps) {
            sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "There are multiple warps with that name!"));
            for (int i = 0; i < warps.size(); i++) {
                sender.sendMessage(ChatUtil.combine(ChatColor.RED, i + ": ", ChatColor.BLUE, warps.get(i).getName(),
                        ChatColor.GRAY, " - ", ChatColor.YELLOW, warps.get(i).getServer()));
            }
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Use ", ChatColor.YELLOW,
                    String.format("/%s remove %s", SledgehammerConfig.warps.warpCommand, args[0]), ChatColor.RED,
                    " <id>", ChatColor.GRAY, " to remove a specific warp!"));
            return;
        }

        int val = Integer.parseInt(args[1]);
        WarpHandler.getInstance().removeWarp(warps.get(val).getId(), sender);
    }

    @Override
    public String getName() {
        return "remove";
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
