package com.noahhusby.sledgehammer.addons.terramap;

import java.util.HashMap;
import java.util.Map;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.packets.ForgePacket;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;

public class ForgeChannel {

	private String channelName;
	private Map<Integer, Class<? extends ForgePacket>> packetMap = new HashMap<Integer, Class<? extends ForgePacket>>();
	private Map<Class<? extends ForgePacket>, Integer> discriminatorMap = new HashMap<Class<? extends ForgePacket>, Integer>();
	
	public ForgeChannel(String channelName) {
		this.channelName = channelName;
	}

	public void process(PluginMessageEvent event) {
		if(!event.getTag().equals(this.channelName)) {
			Sledgehammer.logger.warning("Asked to process a channel from channel: " + event.getTag() + " in " + this.channelName + " channel handler!");
			return;
		}
		try {
			ByteBuf stream = Unpooled.copiedBuffer(event.getData());
			int discriminator = stream.readByte();
			Class<? extends ForgePacket> clazz = packetMap.get(discriminator);
			if(clazz == null) {
				Sledgehammer.logger.warning("Received an unregistered packet! Discriminator: " + discriminator);
				return;
			}
			ForgePacket packetHandler = clazz.newInstance();
			packetHandler.decode(stream);
			boolean cancel = false;
			if(event.getSender() instanceof ProxiedPlayer && event.getReceiver() instanceof Server) {
				cancel = packetHandler.processFromClient(this.channelName, (ProxiedPlayer)event.getSender(), (Server)event.getReceiver());
			} else if(event.getSender() instanceof Server && event.getReceiver() instanceof ProxiedPlayer) {
				cancel = packetHandler.processFromServer(this.channelName, (Server)event.getSender(), (ProxiedPlayer)event.getReceiver());
			} else {
				Sledgehammer.logger.warning(
						"Got an unknow combination of sender/receiver in Forge channel " + this.channelName + " channel. " +
						"Sender " + event.getSender().getClass() +
						", Receiver: " + event.getReceiver().getClass() +
						", Packet discriminator " + discriminator);
				return;
			}
			if(cancel) event.setCancelled(cancel);
		} catch(Exception e) {
			Sledgehammer.logger.warning("Failed to process a Forge packet!");
			e.printStackTrace();
		}
	}
	
	// This method is duplicated because ProxiedPlayer::sendData and Server::sendData do not share a supertype description
	public void send(ProxiedPlayer to, ForgePacket pkt) {
		if(!discriminatorMap.containsKey(pkt.getClass())) {
			Sledgehammer.logger.warning("Tried to send a Forge packet which has not been registered!");
			return;
		}
		int discriminator = discriminatorMap.get(pkt.getClass());
		ByteBuf stream = Unpooled.buffer();
		stream.writeByte(discriminator);
		pkt.encode(stream);
		to.sendData(this.channelName, stream.array());
	}
	
	public void send(SledgehammerPlayer to, ForgePacket pkt) {
		this.send((ProxiedPlayer)to, pkt);
	}
	
	// This method is duplicated because ProxiedPlayer::sendData and Server::sendData do not share a supertype description
	public void send(Server to, ForgePacket pkt) {
		if(!discriminatorMap.containsKey(pkt.getClass())) {
			Sledgehammer.logger.warning("Tried to send a Forge packet which has not been registered!");
			return;
		}
		int discriminator = discriminatorMap.get(pkt.getClass());
		ByteBuf stream = Unpooled.buffer();
		stream.writeByte(discriminator);
		pkt.encode(stream);
		to.sendData(this.channelName, stream.array());
	}

	public void registerPacket(int discriminator, Class<? extends ForgePacket> clazz) {
		packetMap.put(discriminator, clazz);
		discriminatorMap.put(clazz, discriminator);
	}

}
