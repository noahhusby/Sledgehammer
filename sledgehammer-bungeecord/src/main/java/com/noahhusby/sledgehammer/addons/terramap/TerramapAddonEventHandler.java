package com.noahhusby.sledgehammer.addons.terramap;

import com.noahhusby.sledgehammer.addons.terramap.network.packets.P2CSledgehammerHelloPacket;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync.PlayerSyncStatus;
import com.noahhusby.sledgehammer.config.ConfigHandler;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TerramapAddonEventHandler implements Listener {
	
	@EventHandler
    public void onPostLogin(PostLoginEvent event) {
		String version = ProxyServer.getInstance().getPluginManager().getPlugin("Sledgehammer").getDescription().getVersion();
		PlayerSyncStatus syncStatus = PlayerSyncStatus.getFromBoolean(ConfigHandler.terramapSyncPlayers);
    	TerramapAddon.instance.sledgehammerChannel.send(new P2CSledgehammerHelloPacket(
    			version,
    			syncStatus,
    			syncStatus,
    			ConfigHandler.terramapGlobalMap,
    			ConfigHandler.terramapGlobalSettings,
    			false // We do not have warp support yet
    		), event.getPlayer());
    }

}
