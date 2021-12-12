/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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

package com.noahhusby.sledgehammer.server;

import com.noahhusby.sledgehammer.server.chat.ChatHandler;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.s2p.P2SInitializationPacket;
import com.noahhusby.sledgehammer.server.network.s2p.P2SLocationPacket;
import com.noahhusby.sledgehammer.server.network.s2p.P2SPermissionPacket;
import com.noahhusby.sledgehammer.server.network.s2p.P2SSetwarpPacket;
import com.noahhusby.sledgehammer.server.network.s2p.P2STeleportPacket;
import com.noahhusby.sledgehammer.server.network.s2p.P2STestLocationPacket;
import com.noahhusby.sledgehammer.server.network.s2p.P2SWarpConfigPacket;
import com.noahhusby.sledgehammer.server.network.s2p.P2SWarpGUIPacket;
import com.noahhusby.sledgehammer.server.players.PlayerLocationTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Sledgehammer extends JavaPlugin implements Listener {

    public static Logger LOGGER;
    @Getter
    private static Sledgehammer instance;

    @Override
    public void onEnable() {
        instance = this;
        LOGGER = getLogger();

        SledgehammerUtil.checkForTerra();

        NetworkHandler.getInstance().register(
                new P2SInitializationPacket(),
                new P2SLocationPacket(),
                new P2SSetwarpPacket(),
                new P2STeleportPacket(),
                new P2STestLocationPacket(),
                new P2SWarpGUIPacket(),
                new P2SPermissionPacket(),
                new P2SWarpConfigPacket()
        );

        Bukkit.getPluginManager().registerEvents(new GUIRegistry(), this);
        Bukkit.getPluginManager().registerEvents(ChatHandler.getInstance(), this);
        Bukkit.getPluginManager().registerEvents(NetworkHandler.getInstance(), this);
        this.getCommand("shs").setExecutor(new SledgehammerCommand());
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new PlayerLocationTask(), 0, 250);
    }

    @Override
    public void onDisable() {
        instance = null;
        Bukkit.getScheduler().cancelTasks(this);
    }
}
