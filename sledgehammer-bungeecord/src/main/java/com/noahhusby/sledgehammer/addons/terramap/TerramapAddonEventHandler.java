package com.noahhusby.sledgehammer.addons.terramap;

import com.noahhusby.sledgehammer.addons.terramap.packets.P2CSledgehammerHelloPacket;
import com.noahhusby.sledgehammer.addons.terramap.packets.mapsync.PlayerSyncStatus;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TerramapAddonEventHandler implements Listener {
	
	@EventHandler
    public void onPostLogin(PostLoginEvent event) {
		String version = ProxyServer.getInstance().getPluginManager().getPlugin("Sledgehammer").getDescription().getVersion();
		
		//TODO Config options for player sync, spec sync, global map and global settings
    	TerramapAddon.instance.sledgehammerChannel.send(event.getPlayer(), new P2CSledgehammerHelloPacket(
    			version,
    			PlayerSyncStatus.getFromBoolean(true),
    			PlayerSyncStatus.getFromBoolean(true),
    			true,
    			false,
    			false
    		));
    }

}
