/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerNetworkManager.java
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
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.network;

import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.network.S2P.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SledgehammerNetworkManager {
    private static SledgehammerNetworkManager mInstance = null;

    public static SledgehammerNetworkManager getInstance() {
        if(mInstance == null) mInstance = new SledgehammerNetworkManager();
        return mInstance;
    }

    private final List<IS2PPacket> registeredServerPackets;

    private SledgehammerNetworkManager() {
        registeredServerPackets = new ArrayList<>();

        registerServerPacket(new S2PInitializationPacket());
        registerServerPacket(new S2PSetwarpPacket());
        registerServerPacket(new S2PTestLocationPacket());
        registerServerPacket(new S2PWarpPacket());
        registerServerPacket(new S2PWebMapPacket());
        registerServerPacket(new S2PPlayerUpdatePacket());
    }

    private void registerServerPacket(IS2PPacket packet) {
        registeredServerPackets.add(packet);
    }

    public void sendPacket(IP2SPacket packet) {
        JSONObject response = new JSONObject();
        response.put("command", packet.getPacketInfo().getID());
        response.put("sender", packet.getPacketInfo().getSender());
        response.put("server", packet.getPacketInfo().getServer());
        response.put("time", System.currentTimeMillis());
        response.put("data", packet.getMessage(new JSONObject()));

        sendMessage(response, packet.getPacketInfo().getServer());
    }

    private void onPacketRecieved(String m) {
        try {
            SmartObject packet = SmartObject.fromJSON((JSONObject) new JSONParser().parse(m));

            SmartObject packetData = SmartObject.fromJSON((JSONObject) packet.get("data"));
            PacketInfo packetInfo = new PacketInfo(packet.getString("command"), packet.getString("sender"),
                    packet.getString("server"), (long) packet.get("time"));

            for(IS2PPacket p : registeredServerPackets) {
                if(p.getPacketID().equalsIgnoreCase(packetInfo.getID())) {
                    p.onMessage(packetInfo, packetData);
                }
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage(JSONObject o, String server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF(o.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProxyServer.getInstance().getServerInfo(server).sendData("sledgehammer:channel", stream.toByteArray());
    }

    public void onPluginMessageReceived(PluginMessageEvent e) {
        if (!e.getTag().equalsIgnoreCase("sledgehammer:channel")) return;
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));

        onPacketRecieved(getStringFromMessage(in));
    }

    private String getStringFromMessage(DataInputStream i) {
        try {
            String x = "";
            while(i.available() != 0) {
                char c = (char) i.readByte();
                x += c;
            }
            return x.substring(x.indexOf("<")+1).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
