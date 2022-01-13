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

package com.noahhusby.sledgehammer.proxy.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;
import com.noahhusby.lib.data.JsonUtils;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.modules.Module;
import com.noahhusby.sledgehammer.proxy.network.s2p.S2PInitializationPacket;
import com.noahhusby.sledgehammer.proxy.network.s2p.S2PPermissionPacket;
import com.noahhusby.sledgehammer.proxy.network.s2p.S2PPlayerUpdatePacket;
import com.noahhusby.sledgehammer.proxy.network.s2p.S2PSetwarpPacket;
import com.noahhusby.sledgehammer.proxy.network.s2p.S2PTestLocationPacket;
import com.noahhusby.sledgehammer.proxy.network.s2p.S2PWarpConfigPacket;
import com.noahhusby.sledgehammer.proxy.network.s2p.S2PWarpGroupConfigPacket;
import com.noahhusby.sledgehammer.proxy.network.s2p.S2PWarpPacket;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.util.CaseInsensitiveMap;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class NetworkHandler implements Listener, Module {
    private static NetworkHandler instance = null;

    public static NetworkHandler getInstance() {
        return instance == null ? instance = new NetworkHandler() : instance;
    }

    private NetworkHandler() {
    }

    private final Map<String, S2PPacket> registeredPackets = new CaseInsensitiveMap<>();

    /**
     * Register incoming packets
     *
     * @param packet {@link S2PPacket}
     */
    private void register(S2PPacket packet) {
        registeredPackets.put(packet.getPacketID(), packet);
    }

    /**
     * Send an outgoing packet
     *
     * @param packet {@link P2SPacket}
     */
    public void send(P2SPacket packet) {
        PacketInfo info = packet.getPacketInfo();
        JsonObject message = new JsonObject();
        message.addProperty("command", info.getId());
        message.addProperty("sender", info.getSender());
        message.addProperty("time", System.currentTimeMillis());
        JsonObject data = new JsonObject();
        packet.getMessage(data);
        message.add("data", data);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF(SledgehammerUtil.GSON.toJson(message));
        } catch (IOException e) {
            e.printStackTrace();
        }

        packet.getPacketInfo().getServer().sendData(Constants.serverChannel, stream.toByteArray(), true);
    }

    /**
     * Checks for sledgehammer packets from incoming plugin messages
     *
     * @param e {@link PluginMessageEvent}
     */
    @EventHandler
    public void onIncomingPacket(PluginMessageEvent e) {
        if (!e.getTag().equalsIgnoreCase(Constants.serverChannel)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
        JsonObject packetMessage = JsonUtils.parseString(in.readUTF()).getAsJsonObject();

        String command = packetMessage.get("command").getAsString();
        String sender = packetMessage.get("sender").getAsString();
        ServerInfo server = e.getSender() instanceof Server ? ((Server) e.getSender()).getInfo() : null;
        long time = packetMessage.get("time").getAsLong();
        PacketInfo info = new PacketInfo(command, sender, server, time);
        JsonObject data = packetMessage.getAsJsonObject("data");

        S2PPacket p = registeredPackets.get(command);
        if (p != null) {
            p.onMessage(info, data);
        }
    }

    @Override
    public void onEnable() {
        Sledgehammer.addListener(this);
        register(new S2PInitializationPacket());
        register(new S2PSetwarpPacket());
        register(new S2PTestLocationPacket());
        register(new S2PWarpPacket());
        register(new S2PPlayerUpdatePacket());
        register(new S2PPermissionPacket());
        register(new S2PWarpConfigPacket());
        register(new S2PWarpGroupConfigPacket());
    }

    @Override
    public void onDisable() {
        Sledgehammer.removeListener(this);
        registeredPackets.clear();
    }

    @Override
    public String getModuleName() {
        return "Network";
    }
}
