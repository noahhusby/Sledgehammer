/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - MapHandler.java
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

package com.noahhusby.sledgehammer.maps;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import com.noahhusby.sledgehammer.network.P2S.P2STeleportPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MapHandler {
    private static MapHandler mInstance = null;
    public static MapHandler getInstance() {
        if(mInstance == null) mInstance = new MapHandler();
        return mInstance;
    }

    public WebsocketEndpoint ws;

    private boolean isMapInitalized = false;

    private MapHandler() { }

    List<MapSession> sessions = new ArrayList<>();
    private boolean heartbeat = false;

    public void init() {

    }


    public void newMapCommand(CommandSender sender) {
        List<MapSession> temp = new ArrayList<>();
        MapSession session = null;
        for(MapSession m : sessions) {
            if(m.name.toLowerCase().equals(sender.getName())) {
                if(m.time > System.currentTimeMillis()) {
                    temp.add(m);
                } else {
                    session = m;
                }
            }
        }
        for(MapSession m : temp) {
            sessions.remove(m);
        }

        if(session == null) {
            session = new MapSession();
            session.name = sender.getName();
            session.key = UUID.randomUUID();
            session.time = System.currentTimeMillis();
            session.timeout = System.currentTimeMillis() + (60000*ConfigHandler.mapTimeout);

        }

        sender.sendMessage(ChatHelper.makeTitleMapComponent(new TextElement("Click here to access the warp map!", ChatColor.BLUE),
                ConfigHandler.mapLink+"/session?uuid="+session.name+"&key="+session.key));
        sessions.add(session);
    }

    private void handleMessage(String message) {
        try {
            JSONObject o = (JSONObject) new JSONParser().parse(message);
            String action = (String) o.get("action");
            switch(action) {
                case "init":
                    String state = (String) o.get("state");
                    if(state.trim().toLowerCase().equals("success")) {
                        Sledgehammer.logger.info("Successfully initialized map!");
                        isMapInitalized = true;
                    } else {
                        Sledgehammer.logger.info("Map initalized responded with error state: "+state);
                    }
                    break;
                case "warp":
                    JSONObject data = (JSONObject) o.get("data");
                    String uuid = (String) data.get("uuid");
                    String key = (String) data.get("key");
                    String w = (String) data.get("warp");
                    for(MapSession s : sessions) {
                        if(uuid.toLowerCase().trim().equals(s.name.toLowerCase()) && key.toLowerCase().trim().equals(s.key.toString())) {
                            Warp warp = WarpHandler.getInstance().getWarp(w);
                            if(warp == null) {
                                ProxyServer.getInstance().getPlayer(s.name).sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Error: Warp not found", ChatColor.RED)));
                                return;
                            }

                            if(ProxyServer.getInstance().getPlayer(s.name).getServer().getInfo() != ProxyServer.getInstance().getServerInfo(warp.server)) {
                                ProxyServer.getInstance().getPlayer(s.name).connect(ProxyServer.getInstance().getServerInfo(warp.server));
                                ProxyServer.getInstance().getPlayer(s.name).sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Sending you to ", ChatColor.GRAY), new TextElement(warp.server, ChatColor.RED)));
                            }

                            ProxyServer.getInstance().getPlayer(s.name).sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Warping to ", ChatColor.GRAY), new TextElement(w, ChatColor.RED)));
                            SledgehammerNetworkManager.getInstance().sendPacket(new P2STeleportPacket(s.name, warp.server, warp.point));
                        }
                    }
                    break;
                case "alive":
                    heartbeat = false;
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void attemptInit() {
        try {
            ws = new WebsocketEndpoint(new URI("ws://"+ConfigHandler.mapHost+":"+ConfigHandler.mapPort));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ws.addMessageHandler(new WebsocketEndpoint.MessageHandler() {
            public void handleMessage(String message) {
                getInstance().handleMessage(message);
            }
        });

        if(ws.userSession == null) return;

        JSONObject o = new JSONObject();
        JSONObject data = new JSONObject();

        data.put("title", ConfigHandler.mapTitle);
        data.put("subtitle", ConfigHandler.mapSubtitle);
        data.put("lat", ConfigHandler.startingLat);
        data.put("lon", ConfigHandler.startingLon);
        data.put("zoomLevel", ConfigHandler.startingZoom);
        data.put("auth", ConfigHandler.authenticationCode);

        o.put("data", data);
        o.put("action", "init");
        ws.sendMessage(o.toJSONString());
        attemptWarpRefresh();
    }
    public void attemptHeartbeat() {
        heartbeat = true;
        JSONObject o = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("auth", ConfigHandler.authenticationCode);
        o.put("data", data);
        o.put("action", "alive");
        ws.sendMessage(o.toJSONString());
    }

    public boolean getHeartBeatState() {
        return heartbeat;
    }

    public void setInitState(boolean state) {
        this.isMapInitalized = state;
    }

    public void attemptWarpRefresh() {
        Map<String, Warp> warps = WarpHandler.getInstance().getWarps();
        JSONObject o = new JSONObject();
        JSONArray waypoints = new JSONArray();
        for(Map.Entry<String, Warp> w : warps.entrySet()) {
            JSONObject waypoint = new JSONObject();

            waypoint.put("name", ChatHelper.capitalize(w.getKey()));
            waypoint.put("info", "");

            double proj[] = SledgehammerUtil.toGeo(Double.parseDouble(w.getValue().point.x), Double.parseDouble(w.getValue().point.z));

            waypoint.put("lon", proj[0]);
            waypoint.put("lat", proj[1]);
            waypoints.add(waypoint);
        }
        o.put("action", "warp_refresh");
        JSONObject data = new JSONObject();
        data.put("waypoints", waypoints.toJSONString());
        data.put("auth", ConfigHandler.authenticationCode);
        o.put("data", data);
        ws.sendMessage(o.toJSONString());
    }

    public boolean isMapInitalized() {
        return isMapInitalized;
    }
}
