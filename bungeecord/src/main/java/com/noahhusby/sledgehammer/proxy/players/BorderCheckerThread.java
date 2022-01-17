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

package com.noahhusby.sledgehammer.proxy.players;

import com.google.common.collect.ImmutableMap;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * @author Noah Husby
 * Finds players and flags them if they are within a certain region of a border
 * Flagged players are then tracked in {@link FlaggedBorderCheckerThread}
 */
public class BorderCheckerThread implements Runnable {
    @Override
    public void run() {
        ImmutableMap.copyOf(PlayerHandler.getInstance().getPlayers()).forEach((u, p) -> {
            if (!p.onEarthServer() || !p.inEarthRegion() || p.checkAttribute("BORDER_MODE", false)) {
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

                if (check(p, track.getX(), track.getZ())) {
                    return;
                }

                if (check(p, track.getX() + Constants.borderZone, track.getZ() + Constants.borderZone)) {
                    return;
                }

                if (check(p, track.getX() + Constants.borderZone, track.getZ() - Constants.borderZone)) {
                    return;
                }

                if (check(p, track.getX() - Constants.borderZone, track.getZ() + Constants.borderZone)) {
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
