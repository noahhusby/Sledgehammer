/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpRemoveFragment.java
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

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.warp.WarpGroup;
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
            sender.sendMessage(ChatUtil.combine(ChatColor.RED, String.format("Usage: /%s remove <name>", ConfigHandler.warpCommand)));
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
                    String.format("/%s remove %s", ConfigHandler.warpCommand, args[0]), ChatColor.RED,
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
