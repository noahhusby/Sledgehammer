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

package com.noahhusby.sledgehammer.proxy.players;

import com.google.common.collect.ImmutableMap;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.p2s.P2STeleportPacket;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.concurrent.TimeUnit;

/**
 * @author Noah Husby
 * Checks flagged players and teleports them if within region
 */
public class FlaggedBorderCheckerThread implements Runnable {
    @Override
    public void run() {
        ImmutableMap.copyOf(PlayerHandler.getInstance().getPlayers()).forEach((u, p) -> {
            if (!p.isFlagged()) {
                return;
            }
            if (p.checkAttribute("BORDER_MODE", false)) {
                p.setFlagged(false);
                p.setTrackingPoint(null);
                return;
            }

            Point location = p.getLocation();

            double[] proj = SledgehammerUtil.toGeo(location.getX(), location.getZ());
            ServerInfo info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);
            if (info == null) {
                return;
            }
            SledgehammerServer server = ServerHandler.getInstance().getServer(info.getName());
            if (server == null) {
                return;
            }
            if (server.isStealthMode()) {
                return;
            }

            if (!info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                p.connect(info);
                NetworkHandler.getInstance().send(new P2STeleportPacket(p.getName(), info.getName(), location));
                Title title = ProxyServer.getInstance().createTitle();
                title.subTitle(ChatUtil.combine(ChatColor.RED, "You've crossed the border!"));
                title.title(ChatUtil.combine(ChatColor.GRAY, "Teleporting to ", ChatColor.BLUE, info.getName()));
                title.fadeIn(20);
                title.stay(100);
                title.fadeOut(20);

                p.setFlagged(false);

                ProxyServer.getInstance().getScheduler().schedule(Sledgehammer.getInstance(), () -> {
                    p.sendTitle(title);
                    if (!p.checkAttribute("PASSED_BORDER", true)) {
                        p.sendMessage(ChatUtil.combine(ChatColor.RED + "" + ChatColor.BOLD, "Reminder: ",
                                ChatColor.GRAY, "You can use ", ChatColor.YELLOW, "/border", ChatColor.GRAY,
                                " to toggle border teleportation!"));
                        p.getAttributes().put("PASSED_BORDER", true);
                    }
                }, 2, TimeUnit.SECONDS);
            }
        });
    }
}
