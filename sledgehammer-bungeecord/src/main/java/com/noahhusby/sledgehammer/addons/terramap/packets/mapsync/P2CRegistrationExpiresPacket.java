package com.noahhusby.sledgehammer.addons.terramap.packets.mapsync;

import com.noahhusby.sledgehammer.addons.terramap.packets.ForgePacket;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

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
