/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - SledgehammerNetworkManager.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.network;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.*;
import com.noahhusby.sledgehammer.network.P2S.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class NetworkHandler implements PluginMessageListener, Listener {
    private static NetworkHandler instance = null;

    public static NetworkHandler getInstance() {
        return instance == null ? instance = new NetworkHandler() : instance;
    }

    private NetworkHandler() {
        registeredPackets = Maps.newHashMap();
        cachedPackets = Maps.newHashMap();

        register(new P2SCommandPacket());
        register(new P2SInitilizationPacket());
        register(new P2SLocationPacket());
        register(new P2SSetwarpPacket());
        register(new P2STeleportPacket());
        register(new P2STestLocationPacket());
        register(new P2SWarpGUIPacket());
        register(new P2SPermissionPacket());
        register(new P2SWarpConfigPacket());
    }

    private final Map<String, P2SPacket> registeredPackets;
    private final Map<JsonObject, String> cachedPackets;

    private void register(P2SPacket packet) {
        registeredPackets.put(packet.getPacketID(), packet);
    }

    public void send(S2PPacket packet) {
        PacketInfo info = packet.getPacketInfo();
        JsonObject payload = new JsonObject();
        payload.addProperty("command", info.getID());
        payload.addProperty("sender", info.getSender());
        payload.addProperty("server", info.getSender());
        payload.addProperty("time", System.currentTimeMillis());
        JsonObject data = new JsonObject();
        packet.getMessage(data);
        payload.add("data", data);
        //TODO: Burn the fucking "Response Prefix"
        sendMessage(Constants.responsePrefix + SledgehammerUtil.GSON.toJson(payload), packet.getPacketInfo().getSender());
    }

    private void onPacketReceived(JsonObject message) {
        String command = message.get("command").getAsString();
        String sender = message.get("sender").getAsString();
        String server = message.get("server").getAsString();
        long time = message.get("time").getAsLong();
        PacketInfo packetInfo = new PacketInfo(command, sender, server, time);
        if(!SledgehammerUtil.isPlayerAvailable(packetInfo.getSender())) {
            cachedPackets.put(message, sender);
            return;
        }

        P2SPacket p = registeredPackets.get(packetInfo.getID());
        if(p != null) {
            p.onMessage(packetInfo, message.getAsJsonObject("data"));
        }
    }

    private void sendMessage(String message, String sender) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getPlayer(sender).sendPluginMessage(Sledgehammer.getInstance(), "sledgehammer:channel", stream.toByteArray());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for(Map.Entry<JsonObject, String> e : cachedPackets.entrySet()) {
            if(e.getValue().equalsIgnoreCase(event.getPlayer().getName())) {
                onPacketReceived(e.getKey());
            }
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase( "sledgehammer:channel")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String m = in.readUTF();
        onPacketReceived(SledgehammerUtil.parser.parse(m).getAsJsonObject());
    }
}
