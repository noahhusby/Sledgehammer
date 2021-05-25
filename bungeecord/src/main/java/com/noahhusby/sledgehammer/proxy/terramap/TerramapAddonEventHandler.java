package com.noahhusby.sledgehammer.proxy.terramap;

import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
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
public class TerramapAddonEventHandler implements Listener {

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
        boolean playerSync = ConfigHandler.terramapSyncPlayers && RemoteSynchronizer.hasSyncPermission(event.getPlayer());
        PlayerSyncStatus syncStatus = PlayerSyncStatus.getFromBoolean(playerSync);
        TerramapModule.instance.sledgehammerChannel.send(new P2CSledgehammerHelloPacket(
                version,
                syncStatus,
                syncStatus,
                ConfigHandler.terramapGlobalMap,
                ConfigHandler.terramapGlobalSettings,
                false, // We do not have warp support yet
                TerramapModule.instance.getProxyUUID()
        ), event.getPlayer());
        if (ConfigHandler.terramapSendCustomMapsToClient) {
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
