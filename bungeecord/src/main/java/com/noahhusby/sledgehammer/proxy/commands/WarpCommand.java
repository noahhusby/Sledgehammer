/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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

package com.noahhusby.sledgehammer.proxy.commands;

import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.warps.WarpFragmentManager;
import com.noahhusby.sledgehammer.proxy.commands.fragments.warps.WarpListFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.warps.WarpMenuFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.warps.WarpRemoveFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.warps.WarpSetFragment;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.p2s.P2STeleportPacket;
import com.noahhusby.sledgehammer.proxy.network.p2s.P2SWarpGUIPacket;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WarpCommand extends WarpFragmentManager implements TabExecutor {

    public WarpCommand(String name) {
        super(name, "sledgehammer.warp");
        registerCommandFragment(new WarpSetFragment());
        registerCommandFragment(new WarpRemoveFragment());
        registerCommandFragment(new WarpListFragment());
        registerCommandFragment(new WarpMenuFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatUtil.getPlayerOnly());
            return;
        }

        if (!isAllowed(sender)) {
            sender.sendMessage(ChatUtil.getNotAvailable());
            return;
        }

        if (executeFragment(sender, args)) {
            return;
        }

        CompletableFuture<Permission> permissionFuture = SledgehammerPlayer.getPlayer(sender).getPermission("sledgehammer.warp");
        permissionFuture.thenAccept(permission -> {
            if (permission.isLocal()) {
                run(sender, args);
            } else {
                sender.sendMessage(ChatUtil.getNoPermission());
            }
        });
    }

    private void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (SledgehammerConfig.warps.warpMenuDefault) {
                SledgehammerServer server = SledgehammerPlayer.getPlayer(sender).getSledgehammerServer();
                boolean openGUI = (server != null);

                if (openGUI) {
                    if (!server.isInitialized()) {
                        openGUI = false;
                    }
                }

                if (openGUI) {
                    CompletableFuture<Permission> permissionFuture = SledgehammerPlayer.getPlayer(sender).getPermission("sledgehammer.warp.edit");
                    permissionFuture.thenAccept(permission -> NetworkHandler.getInstance().send(new P2SWarpGUIPacket(sender.getName(), SledgehammerUtil.getServerFromSender(sender).getName(), permission.isLocal())));
                    return;
                }
            }

            sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, String.format("Usage: /%s <warp>", SledgehammerConfig.warps.warpCommand)));
            sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Use ", ChatColor.BLUE,
                    String.format("/%s list", SledgehammerConfig.warps.warpCommand), ChatColor.GRAY, " to see the available warps."));
            return;
        }

        List<String> aliases = new ArrayList<>();
        for (WarpGroup group : WarpHandler.getInstance().getWarpGroups().values()) {
            aliases.addAll(group.getAliases());
        }

        if (aliases.contains(args[0])) {
            for (WarpGroup g : WarpHandler.getInstance().getWarpGroups().values()) {
                if (g.getAliases().contains(args[0])) {
                    CompletableFuture<Permission> permissionFuture = SledgehammerPlayer.getPlayer(sender).getPermission("sledgehammer.warp.edit");
                    permissionFuture.thenAccept(permission -> NetworkHandler.getInstance().send(new P2SWarpGUIPacket(sender.getName(), SledgehammerUtil.getServerFromSender(sender).getName(), permission.isLocal())));
                    return;
                }
            }
        }

        List<Warp> warps = new ArrayList<>();
        for (Warp w : WarpHandler.getInstance().getWarps().values()) {
            if (w.isGlobal() || !SledgehammerConfig.warps.localWarp
                || w.getServer().equalsIgnoreCase(SledgehammerUtil.getServerFromSender(sender).getName())) {
                warps.add(w);
            }
        }

        String warpName = SledgehammerUtil.getRawArguments(args);
        Warp warp = null;
        for (Warp w : warps) {
            if (w.getName().equalsIgnoreCase(warpName)) {
                warp = w;
            }
        }

        if (warp == null) {
            sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "That warp does not exist!"));
            return;
        }

        if (SledgehammerUtil.getServerFromSender(sender) != SledgehammerUtil.getServerByName(warp.getServer())) {
            SledgehammerPlayer.getPlayer(sender).connect(SledgehammerUtil.getServerByName(warp.getServer()));
            sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Sending you to ", ChatColor.RED, warp.getServer()));
        }

        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Warping to ", ChatColor.RED, warp.getName()));
        getNetworkManager().send(new P2STeleportPacket(sender.getName(), warp.getServer(), warp.getPoint()));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        boolean tab = PlayerHandler.getInstance().isAdmin(sender) || sender.hasPermission("sledgehammer.warp");
        if (tab) {
            SledgehammerServer s = SledgehammerPlayer.getPlayer(sender).getSledgehammerServer();
            if (s == null) {
                return new ArrayList<>();
            }
            WarpGroup warpGroup = WarpHandler.getInstance().getWarpGroupByServer().get(s.getName());

            List<String> tabbedWarps = new ArrayList<>();
            for (Warp w : WarpHandler.getInstance().getWarps().values()) {
                if (warpGroup != null && warpGroup.getServers().contains(w.getServer())) {
                    tabbedWarps.add(w.getName());
                }
            }

            for (WarpGroup g : WarpHandler.getInstance().getWarpGroups().values()) {
                tabbedWarps.addAll(g.getAliases());
            }

            return SledgehammerUtil.copyPartialMatches(args[0], tabbedWarps);
        }

        return new ArrayList<>();
    }
}
