package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class P2STeleportPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.teleportID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        Player player = SledgehammerUtil.getPlayerFromName(info.getSender());
        if(player == null) {
            throwNoSender();
            return;
        }

        if(!player.isOnline()) {
            throwNoSender();
            return;
        }

        JSONObject pointData = (JSONObject) data.get("point");
        Point point = new Point((String) pointData.get("x"), (String) pointData.get("y"), (String) pointData.get("z"),
                (String) pointData.get("pitch"), (String) pointData.get("yaw"));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("tp %s %s %s %s %s %s", info.getSender(), point.x,
                point.y, point.z, point.yaw, point.pitch));
    }
}
