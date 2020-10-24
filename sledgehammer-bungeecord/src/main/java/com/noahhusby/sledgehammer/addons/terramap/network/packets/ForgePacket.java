package com.noahhusby.sledgehammer.addons.terramap.network.packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public abstract class ForgePacket {
	
	/**
	 * Encode the packet's content in the buffer, excluding discriminator
	 * 
	 * @param buf
	 */
	public abstract void encode(ByteBuf buf);
	
	/**
	 * Decode the packet's content from the buffer, excluding discriminator
	 * 
	 * @param buf
	 */
	public abstract void decode(ByteBuf buf);
	
	/**
	 * Process a packet sent from a server to a client
	 * 
	 * @param channel
	 * @param fromServer
	 * @param toPlayer
	 * @return true if the packet should be stopped from reaching the client
	 */
	public abstract boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer);
	
	/**
	 * Process a packet sent from a client to a server
	 * 
	 * @param channel
	 * @param fromPlayer
	 * @param toServer
	 * @return true if the packet should be stopped from reaching the server
	 */
	public abstract boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer);

}
