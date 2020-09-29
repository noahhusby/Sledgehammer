/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PlayerLocationHandler.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.players;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.projection.ScaleProjection;
import com.noahhusby.sledgehammer.datasets.Point;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;

public class PlayerLocationHandler {
    private static PlayerLocationHandler mInstance = null;

    public static PlayerLocationHandler getInstance() {
        if(mInstance == null) mInstance = new PlayerLocationHandler();
        return mInstance;
    }

    Map<ProxiedPlayer, Point> playerLocations = Maps.newHashMap();
    Map<ProxiedPlayer, Integer> playerRecall = Maps.newHashMap();

    private PlayerLocationHandler() {}

    public void onPlayerJoin(ProxiedPlayer player) {
        playerLocations.remove(player);
    }

    public void onPlayerQuit(ProxiedPlayer player) {
        playerLocations.remove(player);
    }

    public void updateLocation(String sender, Point point) {
        if(false) {
            //if(!ProxyUtil.isServerRegional(ProxyUtil.getServerFromPlayerName(sender))) return;
            GeographicProjection projection = new ModifiedAirocean();
            GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
            ScaleProjection scaleProj = new ScaleProjection(uprightProj, Constants.SCALE, Constants.SCALE);

            double proj[] = scaleProj.toGeo(Double.parseDouble(point.x), Double.parseDouble(point.z));
            Sledgehammer.logger.info("Location: "+proj[0]+", "+proj[1]);
        }
    }

}
