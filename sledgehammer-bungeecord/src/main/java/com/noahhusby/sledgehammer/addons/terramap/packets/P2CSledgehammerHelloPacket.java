package com.noahhusby.sledgehammer.addons.terramap.packets;

import com.noahhusby.sledgehammer.addons.terramap.packets.mapsync.PlayerSyncStatus;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.protocol.DefinedPacket;

public class P2CSledgehammerHelloPacket extends ForgePacket {
	
	public String version = "";
	public PlayerSyncStatus syncPlayers = PlayerSyncStatus.DISABLED;
	public PlayerSyncStatus syncSpectators = PlayerSyncStatus.DISABLED;
	public boolean globalmap = true; // If true, the Terramap allows users to open the map on non-terra worlds
	public boolean globalSettings = false; // Should settings and preferences be saved for the whole network (true) or per server (false)
	
	public P2CSledgehammerHelloPacket(String version, PlayerSyncStatus syncPlayers, PlayerSyncStatus syncSpectators, boolean globalMap, boolean globalSettings) {
		this.version = version;
		this.syncPlayers = syncPlayers;
		this.syncSpectators = syncSpectators;
		this.globalmap = globalMap;
		this.globalSettings = globalSettings;
	}
	
	public P2CSledgehammerHelloPacket() {}

	@Override
	public void encode(ByteBuf buf) {
		DefinedPacket.writeString(this.version, buf);
		buf.writeByte(this.syncPlayers.VALUE);
		buf.writeByte(this.syncSpectators.VALUE);
		buf.writeBoolean(this.globalmap);
		buf.writeBoolean(this.globalSettings);
	}

	@Override
	public void decode(ByteBuf buf) {
		this.version = DefinedPacket.readString(buf);
		this.syncPlayers = PlayerSyncStatus.getFromNetworkCode(buf.readByte());
		this.syncSpectators = PlayerSyncStatus.getFromNetworkCode(buf.readByte());
		this.globalmap = buf.readBoolean();
		this.globalSettings = buf.readBoolean();
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
