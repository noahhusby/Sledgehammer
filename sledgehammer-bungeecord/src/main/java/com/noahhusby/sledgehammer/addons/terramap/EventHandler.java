package com.noahhusby.sledgehammer.addons.terramap;

import com.noahhusby.sledgehammer.addons.terramap.packets.P2CSledgehammerHelloPacket;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;

public class EventHandler implements Listener {
	
	@net.md_5.bungee.event.EventHandler
    public void onPostLogin(PostLoginEvent event) {
		String version = ProxyServer.getInstance().getPluginManager().getPlugin("Sledgehammer").getDescription().getVersion();
    	TerramapAddon.instance.sledgehammerChannel.send(event.getPlayer(), new P2CSledgehammerHelloPacket(version));
    }

}
