package com.noahhusby.sledgehammer.players;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.datasets.Point;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;

public class BorderCheckerThread implements Runnable {
    @Override
    public void run() {
        List<SledgehammerPlayer> players = PlayerManager.getInstance().getPlayers();

        for(SledgehammerPlayer p : players) {
            if(!p.onEarthServer()) {
                p.setTrackingPoint(null);
                p.setFlagged(false);
                continue;
            }

            if(!SledgehammerUtil.inEarthRegion(p)) {
                p.setTrackingPoint(null);
                p.setFlagged(false);
                continue;
            }

            boolean checkLocation = false;

            if(p.getTrackingPoint() == null) {
                p.setTrackingPoint(p.getLocation());
                p.setFlagged(false);
                checkLocation = true;
            }

            if(!checkLocation) {
                Point location = p.getLocation();
                Point track = p.getTrackingPoint();

                double x = Double.parseDouble(location.x);
                double z = Double.parseDouble(location.z);

                double tx = Double.parseDouble(track.x);
                double tz = Double.parseDouble(track.z);

                boolean inZone = x < tx + Constants.borderZone && x > tx - Constants.borderZone && z < tz + Constants.borderZone && z > tz - Constants.borderZone;

                if(!inZone) {
                    p.setTrackingPoint(p.getLocation());
                    p.setFlagged(false);
                    checkLocation = true;
                }
            }

            if(checkLocation) {
                System.out.println("Checking: " + p.getTrackingPoint().getJSON());
                Point track = p.getTrackingPoint();

                double[] proj = SledgehammerUtil.toGeo(Double.parseDouble(track.x), Double.parseDouble(track.z));
                ServerInfo info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    System.out.println(info.getName());
                    continue;
                }

                proj = SledgehammerUtil.toGeo(Double.parseDouble(track.x) + Constants.borderZone, Double.parseDouble(track.z) + Constants.borderZone);
                info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    p.setFlagged(true);
                    continue;
                }

                proj = SledgehammerUtil.toGeo(Double.parseDouble(track.x) + Constants.borderZone, Double.parseDouble(track.z) - Constants.borderZone);
                info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    p.setFlagged(true);
                    continue;
                }

                proj = SledgehammerUtil.toGeo(Double.parseDouble(track.x) - Constants.borderZone, Double.parseDouble(track.z) + Constants.borderZone);
                info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    p.setFlagged(true);
                    continue;
                }

                proj = SledgehammerUtil.toGeo(Double.parseDouble(track.x) - Constants.borderZone, Double.parseDouble(track.z) - Constants.borderZone);
                info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    p.setFlagged(true);
                    continue;
                }

            }
        }
    }
}
