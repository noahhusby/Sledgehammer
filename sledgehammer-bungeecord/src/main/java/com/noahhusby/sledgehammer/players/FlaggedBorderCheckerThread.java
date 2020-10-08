/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - FlaggedBorderCheckerThread.java
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
 *
 */

package com.noahhusby.sledgehammer.players;

import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.datasets.Point;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.config.ServerInfo;

public class FlaggedBorderCheckerThread implements Runnable{
    @Override
    public void run() {
        for(SledgehammerPlayer p : PlayerManager.getInstance().getPlayers()) {
            if(p.isFlagged()) {
                Point location = p.getLocation();

                double[] proj = SledgehammerUtil.toGeo(Double.parseDouble(location.x), Double.parseDouble(location.z));
                ServerInfo info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    Title title = ProxyServer.getInstance().createTitle();
                    title.subTitle(ChatHelper.makeTextComponent(new TextElement("You've crossed the border!", ChatColor.RED)));
                    title.title(ChatHelper.makeTextComponent(new TextElement("Teleporting to ", ChatColor.GRAY), new TextElement(info.getName(), ChatColor.BLUE)));
                    title.fadeIn(20);
                    title.stay(100);
                    title.fadeOut(20);
                    p.sendTitle(title);

                    p.connect(info);
                    p.setFlagged(false);
                }
            }
        }
    }
}
