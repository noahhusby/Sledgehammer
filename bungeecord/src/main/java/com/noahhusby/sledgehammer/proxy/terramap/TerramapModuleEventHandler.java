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

package com.noahhusby.sledgehammer.proxy.terramap;

import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.terramap.MapStyleLibrary.MapStyle;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.P2CMapStylePacket;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.P2CSledgehammerHelloPacket;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync.PlayerSyncStatus;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * Handles all event's needed for Terramap integration
 *
 * @author SmylerMC
 */
public class TerramapModuleEventHandler implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPostLogin(PostLoginEvent event) {
        /*
         *  Avoid spamming people's logs with unknown channel reports.
         *  We can't tell if the player has Terramap at this point as the mod list hasn't been sent yet,
         *  so we can only check for Forge.
         */
        if (!event.getPlayer().isForgeUser()) {
            return;
        }

        String version = ProxyServer.getInstance().getPluginManager().getPlugin("Sledgehammer").getDescription().getVersion();
        boolean playerSync = SledgehammerConfig.terramap.terramapSyncPlayers && RemoteSynchronizer.hasSyncPermission(event.getPlayer());
        PlayerSyncStatus syncStatus = PlayerSyncStatus.getFromBoolean(playerSync);
        TerramapModule.instance.sledgehammerChannel.send(new P2CSledgehammerHelloPacket(
                version,
                syncStatus,
                syncStatus,
                SledgehammerConfig.terramap.terramapGlobalMap,
                SledgehammerConfig.terramap.terramapGlobalSettings,
                false, // We do not have warp support yet
                TerramapModule.instance.getProxyUUID()
        ), event.getPlayer());
        if (SledgehammerConfig.terramap.terramapSendCustomMapsToClient) {
            for (P2CMapStylePacket packet : MapStyleRegistry.getMaps().values()) {
                TerramapModule.instance.sledgehammerChannel.send(packet, event.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        TerramapModule.instance.synchronizer.unregisterPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getTag().equals(TerramapModule.MAPSYNC_CHANNEL_NAME)) {
            TerramapModule.instance.mapSyncChannel.process(event);
        } else if (event.getTag().equals(TerramapModule.SLEDGEHAMMER_CHANNEL_NAME)) {
            TerramapModule.instance.sledgehammerChannel.process(event);
        }
    }
}
