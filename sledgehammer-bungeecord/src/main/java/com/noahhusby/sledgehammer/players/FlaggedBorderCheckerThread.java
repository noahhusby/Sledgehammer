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
