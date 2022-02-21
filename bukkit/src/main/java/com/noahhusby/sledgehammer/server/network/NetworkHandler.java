/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
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
    private final Map<String, P2SPacket> registeredPackets;
    private final Map<JsonObject, String> cachedPackets;
    private final MessageChannel channel;
    private NetworkHandler() {
        registeredPackets = Maps.newHashMap();
        cachedPackets = Maps.newHashMap();
        channel = new MessageChannel(Sledgehammer.getInstance(), Constants.serverChannel);
        channel.onMessage(m -> onMessage(SledgehammerUtil.parser.parse(m).getAsJsonObject()));
    }

    public static NetworkHandler getInstance() {
        return instance == null ? instance = new NetworkHandler() : instance;
    }

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
