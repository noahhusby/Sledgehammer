package com.noahhusby.sledgehammer.addons.terramap.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.protocol.DefinedPacket;

public class P2CSledgehammerHelloPacket extends ForgePacket {
	
	public String version = "";
	
	public P2CSledgehammerHelloPacket(String version) {
		this.version = version;
	}
	
	public P2CSledgehammerHelloPacket() {}

	@Override
	public void encode(ByteBuf buf) {
		DefinedPacket.writeString(this.version, buf);
	}

	@Override
	public void decode(ByteBuf buf) {
		this.version = DefinedPacket.readString(buf);
	}

	@Override
	public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
		return false; // We should never receive that, only send
	}

	@Override
	public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
		return false; // We should never receive that, only send
	}

}
