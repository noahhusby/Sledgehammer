/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - P2SWarpGUIPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.gui.GUIRegistry;
import com.noahhusby.sledgehammer.gui.inventories.WarpInventoryController;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class P2SWarpGUIPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.warpGUIID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        Player p = Bukkit.getPlayer(info.getSender());
        if(p == null) {
            throwNoSender();
            return;
        }

        if(!p.isOnline()) {
            throwNoSender();
            return;
        }

        GUIRegistry.register(new WarpInventoryController(p, data));
    }
}
