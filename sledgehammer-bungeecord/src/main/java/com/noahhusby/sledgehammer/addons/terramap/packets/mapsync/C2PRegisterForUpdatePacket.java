package com.noahhusby.sledgehammer.addons.terramap.packets.mapsync;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.packets.ForgePacket;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class C2PRegisterForUpdatePacket extends ForgePacket {
	
	boolean register;

	@Override
	public void encode(ByteBuf stream) {
	}

	@Override
	public void decode(ByteBuf stream) {
		this.register = stream.readBoolean();
	}

	@Override
	public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
		// Should never receive this from a server
		return false;
	}

	@Override
	public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
		//TODO Implement instead of logging
		Sledgehammer.logger.info("Got player register for " + fromPlayer.getDisplayName() + "/" + fromPlayer.getUniqueId() + ", value=" + this.register);
		return true; // Do not send to server
	}

}
