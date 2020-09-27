/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - P2SLocationPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.projection.ScaleProjection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class P2SLocationPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.locationID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        Player player = SledgehammerUtil.getPlayerFromName(info.getSender());
        if(player == null) {
            throwNoSender();
            return;
        }

        String lat = data.getString("lat");
        String lon = data.getString("lon");

        if(ConfigHandler.tpllMode.toLowerCase().equals("cs")) {
            Bukkit.getServer().dispatchCommand(player,"cs tpll "+lat+" "+lon);
            return;
        } else if(ConfigHandler.tpllMode.toLowerCase().equals("tpll")) {
            Bukkit.getServer().dispatchCommand(player,"tpll "+lat+" "+lon);
            return;
        }

        GeographicProjection projection = new ModifiedAirocean();
        GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
        ScaleProjection scaleProj = new ScaleProjection(uprightProj, Constants.SCALE, Constants.SCALE);

        double proj[] = scaleProj.fromGeo(Double.parseDouble(lon), Double.parseDouble(lat));

        int x = (int) Math.floor(proj[0]);
        int z = (int) Math.floor(proj[1]);


        int y = Constants.scanHeight;

        while(player.getWorld().getBlockAt(x, y, z).getType() != Material.AIR) {
            y += Constants.scanHeight;
        }

        while(player.getWorld().getBlockAt(x, y, z).getType() == Material.AIR) {
            y -= 1;
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),String.format("tp %s %s %s %s", player.getName(), x, y+1, z));

    }
}
