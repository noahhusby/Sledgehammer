/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - Sledgehammer.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.server;

import com.noahhusby.sledgehammer.server.chat.ChatHandler;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.P2S.P2SCommandPacket;
import com.noahhusby.sledgehammer.server.network.P2S.P2SInitilizationPacket;
import com.noahhusby.sledgehammer.server.network.P2S.P2SLocationPacket;
import com.noahhusby.sledgehammer.server.network.P2S.P2SPermissionPacket;
import com.noahhusby.sledgehammer.server.network.P2S.P2SSetwarpPacket;
import com.noahhusby.sledgehammer.server.network.P2S.P2STeleportPacket;
import com.noahhusby.sledgehammer.server.network.P2S.P2STestLocationPacket;
import com.noahhusby.sledgehammer.server.network.P2S.P2SWarpConfigPacket;
import com.noahhusby.sledgehammer.server.network.P2S.P2SWarpGUIPacket;
import com.noahhusby.sledgehammer.server.players.PlayerManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Sledgehammer extends JavaPlugin implements Listener {

    public static Logger LOGGER = Logger.getLogger("Sledgehammer Bootstrap");
    @Getter
    private static Sledgehammer instance;

    public static String bungeecordName = "";

    @Override
    public void onEnable() {
        instance = this;
        LOGGER = getLogger();

        SledgehammerUtil.checkForTerra();

        NetworkHandler.getInstance().register(
                new P2SCommandPacket(),
                new P2SInitilizationPacket(),
                new P2SLocationPacket(),
                new P2SSetwarpPacket(),
                new P2STeleportPacket(),
                new P2STestLocationPacket(),
                new P2SWarpGUIPacket(),
                new P2SPermissionPacket(),
                new P2SWarpConfigPacket()
        );

        Bukkit.getServer().getPluginManager().registerEvents(ChatHandler.getInstance(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GUIRegistry(), this);
        Bukkit.getServer().getPluginManager().registerEvents(NetworkHandler.getInstance(), this);

        PlayerManager.getInstance();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
