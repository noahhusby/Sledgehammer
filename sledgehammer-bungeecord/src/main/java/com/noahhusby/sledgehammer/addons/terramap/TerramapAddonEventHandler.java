package com.noahhusby.sledgehammer.addons.terramap;

import com.noahhusby.sledgehammer.addons.terramap.network.packets.P2CMapStylePacket;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.P2CSledgehammerHelloPacket;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync.PlayerSyncStatus;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.players.PlayerManager;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Handles all event's needed for Terramap integration
 * 
 * @author SmylerMC
 *
 */
public class TerramapAddonEventHandler implements Listener {
	
	@EventHandler
    public void onPostLogin(PostLoginEvent event) {
		
		// Avoid spamming people's logs with unknown channel reports
		if(!SledgehammerPlayer.getPlayer(event.getPlayer()).hasCompatibleTerramap()) return;
		
		String version = ProxyServer.getInstance().getPluginManager().getPlugin("Sledgehammer").getDescription().getVersion();
		boolean playerSync = ConfigHandler.terramapSyncPlayers && event.getPlayer().hasPermission(TerramapAddon.PLAYER_SYNC_PERMISSION_NODE);
		PlayerSyncStatus syncStatus = PlayerSyncStatus.getFromBoolean(playerSync);
    	TerramapAddon.instance.sledgehammerChannel.send(new P2CSledgehammerHelloPacket(
    			version,
    			syncStatus,
    			syncStatus,
    			ConfigHandler.terramapGlobalMap,
    			ConfigHandler.terramapGlobalSettings,
    			false, // We do not have warp support yet
    			TerramapAddon.instance.getProxyUUID()
    		), event.getPlayer());
    	if(ConfigHandler.terramapSendCustomMapsToClient) {
    		for(P2CMapStylePacket packet: MapStyleRegistry.getMaps().values()) {
    			TerramapAddon.instance.sledgehammerChannel.send(packet, event.getPlayer());
    		}
    	}
    }
	
	@EventHandler
	public void onPlayerLeave(PlayerDisconnectEvent event) {
		TerramapAddon.instance.synchronizer.unregisterPlayer(PlayerManager.getInstance().getPlayer(event.getPlayer()));
	}

}
