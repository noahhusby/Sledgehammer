package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class S2PSetwarpPacket extends S2PPacket {

    private final PacketInfo info;
    private Point point;

    public S2PSetwarpPacket(PacketInfo info) {
        this.info = info;
    }

    @Override
    public String getPacketID() {
        return Constants.setwarpID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        Player p = Bukkit.getPlayer(info.getSender());
        if(p == null) return null;
        if(!p.isOnline()) return null;

        point = new Point(String.valueOf(p.getLocation().getX()), String.valueOf(p.getLocation().getY()), String.valueOf(p.getLocation().getZ()),
                String.valueOf(p.getLocation().getPitch()), String.valueOf(p.getLocation().getYaw()));

        data.put("point", point.getJSON());

        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.renew(info);
    }
}
