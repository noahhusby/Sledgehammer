/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpHandler.java
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
 *
 */

package com.noahhusby.sledgehammer.warp;

import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.network.P2S.P2SSetwarpPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public class WarpHandler {
    private static WarpHandler instance;

    public static WarpHandler getInstance() {
        return instance;
    }

    public static void setInstance(WarpHandler instance) {
        WarpHandler.instance = instance;
    }

    public WarpHandler() {}

    @Expose(serialize = true, deserialize = true)
    public Map<String, Warp> warps = Maps.newHashMap();

    public Map<String, JSONObject> requestedWarps = Maps.newHashMap();

    public Map<String, Warp> getWarps() {
        return warps;
    }

    public Warp getWarp(String w) {
        for(Map.Entry<String, Warp> s : warps.entrySet())
            if(s.getKey().equalsIgnoreCase(w)) return s.getValue();
        return null;
    }

    public void requestNewWarp(String warp, CommandSender sender, boolean pinned) {
        JSONObject w = new JSONObject();
        w.put("name", warp);
        w.put("pinned", pinned);
        requestedWarps.put(sender.getName(), w);

        SledgehammerNetworkManager.getInstance().sendPacket(new P2SSetwarpPacket(sender.getName(), SledgehammerUtil.getServerNameByPlayer(sender)));
    }

    public void removeWarp(String w, CommandSender sender) {
        if(warps.containsKey(w.toLowerCase())) {
            warps.remove(w.toLowerCase());
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Successfully removed ", ChatColor.GRAY),
                    new TextElement(ChatHelper.capitalize(w), ChatColor.RED)));
            ConfigHandler.getInstance().saveWarpDB();
            return;
        }

        sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Warp could not be removed", ChatColor.RED)));

    }

    public void incomingLocationResponse(String sender, Point p) {
        if(requestedWarps.containsKey(sender)) {
            addWarp(sender, (String) requestedWarps.get(sender).get("name"), (boolean) requestedWarps.get(sender).get("pinned"),
                    p, ProxyServer.getInstance().getPlayer(sender).getServer().getInfo());
            requestedWarps.remove(sender);
        }
    }

    public String getWarpList() {
        String warpList = "";
        boolean first = true;
        for(String s : warps.keySet()) {
            if(first) {
                warpList = ChatHelper.capitalize(s);
                first = false;
            } else {
                warpList += ", "+ChatHelper.capitalize(s);
            }
        }
        return warpList;
    }

    private void addWarp(String sender, String w, boolean pinned, Point p, ServerInfo s) {
        warps.remove(w.toLowerCase());

        warps.put(w, new Warp(p, s.getName(), pinned));

        if(pinned) {
            ProxyServer.getInstance().getPlayer(sender).sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Created pinned warp ", ChatColor.GRAY),
                    new TextElement(ChatHelper.capitalize(w), ChatColor.RED)));
        } else {
            ProxyServer.getInstance().getPlayer(sender).sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Created warp ", ChatColor.GRAY),
                    new TextElement(ChatHelper.capitalize(w), ChatColor.RED)));
        }

        ConfigHandler.getInstance().saveWarpDB();
    }

    public JSONObject generateGUIPayload() {
        JSONObject o = new JSONObject();
        JSONArray waypoints = new JSONArray();
        for(Map.Entry<String, Warp> w : warps.entrySet()) {
            JSONObject wa = new JSONObject();
            wa.put("name", w.getKey());
            wa.put("pinned", w.getValue().pinned);
            wa.put("server", w.getValue().server);
            waypoints.add(wa);
        }

        o.put("web", ConfigHandler.mapEnabled);
        o.put("waypoints", waypoints);

        return o;
    }

}
