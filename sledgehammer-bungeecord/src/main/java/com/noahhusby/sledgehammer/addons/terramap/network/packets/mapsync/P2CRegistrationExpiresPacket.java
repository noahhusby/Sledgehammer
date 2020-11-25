package com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync;

import com.noahhusby.sledgehammer.addons.terramap.network.packets.ForgePacket;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

//TODO Do not intercept is the proxy has player sync disabled
public class P2CRegistrationExpiresPacket extends ForgePacket {

	public P2CRegistrationExpiresPacket() {}

	@Override
	public void encode(ByteBuf buf) {}

	@Override
	public void decode(ByteBuf buf) {}

	@Override
	public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
		// Never received here
		return false;
	}

	@Override
	public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
		// Never received here
		return false;
	}
	
}
