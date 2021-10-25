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

package com.noahhusby.sledgehammer.server.network;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.Sledgehammer;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class NetworkHandler implements Listener {
    private static NetworkHandler instance = null;

    public static NetworkHandler getInstance() {
        return instance == null ? instance = new NetworkHandler() : instance;
    }

    private NetworkHandler() {
        registeredPackets = Maps.newHashMap();
        cachedPackets = Maps.newHashMap();
        channel = new MessageChannel(Sledgehammer.getInstance(), Constants.serverChannel);
        channel.onMessage(m -> onMessage(SledgehammerUtil.parser.parse(m).getAsJsonObject()));
    }

    private final Map<String, P2SPacket> registeredPackets;
    private final Map<JsonObject, String> cachedPackets;
    private final MessageChannel channel;

    public void register(P2SPacket packet) {
        registeredPackets.put(packet.getPacketID(), packet);
    }

    public void register(P2SPacket... packets) {
        for (P2SPacket p : packets) {
            register(p);
        }
    }

    public void send(S2PPacket packet) {
        PacketInfo info = packet.getPacketInfo();
        JsonObject payload = new JsonObject();
        payload.addProperty("command", info.getId());
        payload.addProperty("sender", info.getSender());
        payload.addProperty("time", System.currentTimeMillis());
        JsonObject data = new JsonObject();
        packet.getMessage(data);
        payload.add("data", data);
        channel.send(Bukkit.getPlayer(packet.getPacketInfo().getSender()), SledgehammerUtil.GSON.toJson(payload));
    }

    private void onMessage(JsonObject message) {
        String command = message.get("command").getAsString();
        String sender = message.get("sender").getAsString();
        long time = message.get("time").getAsLong();
        PacketInfo packetInfo = new PacketInfo(command, sender, time);
        if (!SledgehammerUtil.isPlayerAvailable(packetInfo.getSender())) {
            cachedPackets.put(message, sender);
            return;
        }

        P2SPacket p = registeredPackets.get(packetInfo.getId());
        if (p != null) {
            p.onMessage(packetInfo, message.getAsJsonObject("data"));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Map.Entry<JsonObject, String> e : cachedPackets.entrySet()) {
            if (e.getValue().equalsIgnoreCase(event.getPlayer().getName())) {
                onMessage(e.getKey());
            }
        }
    }
}
