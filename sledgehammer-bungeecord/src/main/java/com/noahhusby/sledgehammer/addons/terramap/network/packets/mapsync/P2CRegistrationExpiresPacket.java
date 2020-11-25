package com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync;

import com.noahhusby.sledgehammer.addons.terramap.network.packets.IForgePacket;
import com.noahhusby.sledgehammer.config.ConfigHandler;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class P2CRegistrationExpiresPacket implements IForgePacket {

	public P2CRegistrationExpiresPacket() {}

	@Override
	public void encode(ByteBuf buf) {}

	@Override
	public void decode(ByteBuf buf) {}

	@Override
	public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
		// Never received here
		return ConfigHandler.terramapSyncPlayers;
	}

	@Override
	public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
		// Never received here
		return ConfigHandler.terramapSyncPlayers;
	}
	
}
