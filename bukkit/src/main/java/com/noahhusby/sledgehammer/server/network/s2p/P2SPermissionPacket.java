package com.noahhusby.sledgehammer.server.network.s2p;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.network.P2SPacket;
import com.noahhusby.sledgehammer.server.network.PacketInfo;
import com.noahhusby.sledgehammer.server.network.p2s.S2PPermissionPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class P2SPermissionPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.permissionCheckID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        UUID player = UUID.fromString(data.get("player").getAsString());
        String salt = data.get("salt").getAsString();
        String permission = data.get("permission").getAsString();

        boolean permissionResponse = false;

        Player p = Bukkit.getPlayer(player);
        if (p != null) {
            permissionResponse = p.hasPermission(permission);
        }

        getManager().send(new S2PPermissionPacket(p, salt, permissionResponse));
    }
}
