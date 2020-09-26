package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class P2SCommandPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.commandID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        Player player = SledgehammerUtil.getPlayerFromName(info.getSender());
        if(player == null) {
            throwNoSender();
            return;
        }

        Bukkit.getServer().dispatchCommand(player, data.getString("args"));
    }
}
