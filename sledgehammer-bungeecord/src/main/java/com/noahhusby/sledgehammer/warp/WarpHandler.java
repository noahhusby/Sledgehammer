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
 */

package com.noahhusby.sledgehammer.warp;

import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.noahhusby.lib.data.storage.StorageList;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
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
        if(instance == null) instance = new WarpHandler();
        return instance;
    }

    public WarpHandler() {}

    public StorageList<Warp> warps = new StorageList<>(Warp.class);

    public Map<String, JSONObject> requestedWarps = Maps.newHashMap();

    public StorageList<Warp> getWarps() {
        return warps;
    }

    public Warp getWarp(String wn) {
        for(Warp w : warps)
            if(w.name.equalsIgnoreCase(wn)) return w;
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
        Warp remove = null;
        for(Warp wp : warps) {
            if(wp.name.equalsIgnoreCase(w)) {
                remove = wp;
                sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Successfully removed ", ChatColor.GRAY),
                        new TextElement(ChatHelper.capitalize(w), ChatColor.RED)));
                warps.save();
            }
        }

        if(remove != null)  {
            warps.remove(remove);
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
        for(Warp w : warps) {
            if(first) {
                warpList = ChatHelper.capitalize(w.name);
            } else {
                warpList += ", "+ChatHelper.capitalize(w.name);
            }
        }

        return warpList;
    }

    private void addWarp(String sender, String w, boolean pinned, Point p, ServerInfo s) {
        warps.remove(w.toLowerCase());

        warps.add(new Warp(w, p, s.getName(), pinned));

        if(pinned) {
            ProxyServer.getInstance().getPlayer(sender).sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Created pinned warp ", ChatColor.GRAY),
                    new TextElement(ChatHelper.capitalize(w), ChatColor.RED)));
        } else {
            ProxyServer.getInstance().getPlayer(sender).sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Created warp ", ChatColor.GRAY),
                    new TextElement(ChatHelper.capitalize(w), ChatColor.RED)));
        }

        warps.save();
    }

    public JSONObject generateGUIPayload() {
        JSONObject o = new JSONObject();
        JSONArray waypoints = new JSONArray();
        for(Warp w : warps) {
            JSONObject wa = new JSONObject();
            String friendlyName = ServerConfig.getInstance().getServer(w.server).friendly_name;
            if(friendlyName == null) friendlyName = w.server;

            wa.put("name", w.name);
            wa.put("pinned", w.pinned);
            wa.put("server", friendlyName);
            waypoints.add(wa);
        }

        o.put("web", ConfigHandler.mapEnabled);
        o.put("waypoints", waypoints);

        return o;
    }

}
