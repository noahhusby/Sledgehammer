/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - BorderCheckerThread.java
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

package com.noahhusby.sledgehammer.proxy.players;

import com.google.common.collect.ImmutableMap;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * Finds players and flags them if they are within a certain region of a border
 * Flagged players are then tracked in {@link FlaggedBorderCheckerThread}
 */
public class BorderCheckerThread implements Runnable {
    @Override
    public void run() {
        ImmutableMap.copyOf(PlayerManager.getInstance().getPlayers()).forEach((u, p) -> {
            if (!p.onEarthServer() || !SledgehammerUtil.inEarthRegion(p) || p.checkAttribute("BORDER_MODE", false)) {
                p.setTrackingPoint(null);
                p.setFlagged(false);
                return;
            }

            boolean checkLocation = false;

            if (p.getTrackingPoint() == null) {
                p.setTrackingPoint(p.getLocation());
                p.setFlagged(false);
                checkLocation = true;
            }

            if (!checkLocation) {
                Point location = p.getLocation();
                Point track = p.getTrackingPoint();
                double x = location.getX();
                double z = location.getZ();
                double tx = track.getX();
                double tz = track.getZ();

                boolean inZone = x < tx + Constants.borderZone && x > tx - Constants.borderZone && z < tz + Constants.borderZone && z > tz - Constants.borderZone;

                if (!inZone) {
                    p.setTrackingPoint(p.getLocation());
                    p.setFlagged(false);
                    checkLocation = true;
                }
            }

            if (checkLocation) {
                Point track = p.getTrackingPoint();

                if(check(p, track.getX(), track.getZ())) {
                    return;
                }

                if(check(p, track.getX() + Constants.borderZone, track.getZ() + Constants.borderZone)) {
                    return;
                }

                if(check(p, track.getX() + Constants.borderZone, track.getZ() - Constants.borderZone)) {
                    return;
                }

                if(check(p, track.getX() - Constants.borderZone, track.getZ() + Constants.borderZone)) {
                    return;
                }

                check(p, track.getX() - Constants.borderZone, track.getZ() - Constants.borderZone);
            }
        });
    }

    private boolean check(SledgehammerPlayer player, double x, double z) {
        double[] proj = SledgehammerUtil.toGeo(x, z);
        ServerInfo info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

        if (info != null && !info.getName().equalsIgnoreCase(player.getServer().getInfo().getName())) {
            player.setFlagged(true);
            return true;
        }
        return false;
    }
}
