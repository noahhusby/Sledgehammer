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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public class WarpHandler {
    private static WarpHandler instance;

    public static WarpHandler getInstance() {
        if(instance == null) instance = new WarpHandler();
        return instance;
    }

    public StorageList<Warp> warps = new StorageList<>(Warp.class);
    public Map<String, JSONObject> requestedWarps = Maps.newHashMap();

    /**
     * Gets a list of warps
     * @return List of warps
     */
    public StorageList<Warp> getWarps() {
        return warps;
    }

    /**
     * Gets a warp by name
     * @param name Name of warp
     * @return Warp if found, null if not
     */
    public Warp getWarp(String name) {
        for(Warp w : warps)
            if(w.name.equalsIgnoreCase(name)) return w;
        return null;
    }

    /**
     * Requests that a new warp should be created
     * @param warp The name of the warp
     * @param sender The command sender
     * @param pinned True if the warp should be pinned, false if not
     */
    public void requestNewWarp(String warp, CommandSender sender, boolean pinned) {
        JSONObject w = new JSONObject();
        w.put("name", warp);
        w.put("pinned", pinned);
        requestedWarps.put(sender.getName(), w);

        SledgehammerNetworkManager.getInstance().send(new P2SSetwarpPacket(sender.getName(), SledgehammerUtil.getServerFromSender(sender).getName()));
    }

    /**
     * Deletes a warp by name
     * @param w Warp name
     * @param sender Command sender
     */
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

    /**
     * Called by incoming location packet to register a warp
     * @param sender Name of sender
     * @param p Location of warp
     */
    public void incomingLocationResponse(String sender, Point p) {
        if(requestedWarps.containsKey(sender)) {
            String warpName = (String) requestedWarps.get(sender).get("name");
            boolean pinned = (boolean) requestedWarps.get(sender).get("pinned");

            warps.remove(getWarp(warpName));
            warps.add(new Warp(warpName, p,ProxyServer.getInstance().getPlayer(sender).getServer().getInfo().getName(), pinned));

            String message = pinned ? "Created warp " : "Created pinned warp ";
            ProxyServer.getInstance().getPlayer(sender).sendMessage(ChatHelper.makeTitleTextComponent(new TextElement(message, ChatColor.GRAY),
                    new TextElement(ChatHelper.capitalize(warpName), ChatColor.RED)));

            warps.save();

            requestedWarps.remove(sender);
        }
    }

    /**
     * Gets list of warps as a String
     * @return List of warps as String
     */
    public String getWarpList() {
        StringBuilder warpList = new StringBuilder();
        boolean first = true;
        for(Warp w : warps) {
            if(first) {
                warpList = new StringBuilder(ChatHelper.capitalize(w.name));
                first = false;
            } else {
                warpList.append(", ").append(ChatHelper.capitalize(w.name));
            }
        }

        return warpList.toString();
    }

    /**
     * Generates a list of warp data for the warp GUI
     * @return Warp GUI Payload
     */
    public JSONObject generateGUIPayload() {
        JSONObject o = new JSONObject();
        JSONArray waypoints = new JSONArray();
        for(Warp w : warps) {
            JSONObject wa = new JSONObject();
            String friendlyName = ServerConfig.getInstance().getServer(w.server).getFriendlyName();
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
