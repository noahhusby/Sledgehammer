package com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.TerramapAddon;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.ForgePacket;
import com.noahhusby.sledgehammer.players.PlayerManager;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class C2PRegisterForUpdatePacket extends ForgePacket {
	
	boolean register;

	@Override
	public void encode(ByteBuf buf) {
		buf.writeBoolean(this.register);
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
		SledgehammerPlayer player = PlayerManager.getInstance().getPlayer(fromPlayer.getName());
		if(this.register) {
			Sledgehammer.sledgehammer.getProxy().getScheduler().runAsync(Sledgehammer.sledgehammer, 
				() -> TerramapAddon.instance.synchronizer.registerPlayer(player) );
		} else {
			Sledgehammer.sledgehammer.getProxy().getScheduler().runAsync(Sledgehammer.sledgehammer,
				() -> TerramapAddon.instance.synchronizer.unregisterPlayer(player) );
		}
		return true; // Do not send to server
	}

}
