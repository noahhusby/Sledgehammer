/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PTestLocationPacket.java
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

package com.noahhusby.sledgehammer.proxy.network.s2p;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.datasets.Location;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.network.S2PPacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class S2PTestLocationPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.testLocationID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        JsonObject point = data.getAsJsonObject("point");
        int zoom = Math.round(data.get("zoom").getAsInt());
        double[] proj = SledgehammerUtil.toGeo(Double.parseDouble(point.get("x").getAsString()), Double.parseDouble(point.get("z").getAsString()));
        Location online = OpenStreetMaps.getInstance().getLocation(proj[0], proj[1], zoom);
        CommandSender player = ProxyServer.getInstance().getPlayer(info.getSender());
        if (online == null) {
            player.sendMessage(ChatUtil.getNotProjection());
            return;
        }
        player.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Testing location at ",
                ChatColor.BLUE, String.format("%s, %s", proj[1], proj[0]), ChatColor.GRAY, String.format(" (Zoom: %d)", zoom)));
        player.sendMessage(ChatUtil.combine(ChatColor.RED, "Online: "));
        printLocationBlocks(info, player, online);
        if (!SledgehammerConfig.geography.useOfflineMode) {
            player.sendMessage(ChatUtil.combine(ChatColor.RED, "Offline: ", ChatColor.DARK_RED, "Disabled"));
        } else {
            Location offline = OpenStreetMaps.getInstance().getOfflineLocation(proj[0], proj[1]);
            player.sendMessage(ChatUtil.combine(ChatColor.RED, "Offline ", ChatColor.GRAY, "(",
                    ChatColor.GREEN, "Active", ChatColor.GRAY, "):"));
            printLocationBlocks(info, player, offline);
        }
    }

    private void printLocationBlocks(PacketInfo info, CommandSender player, Location location) {
        if (!location.city.equals("")) {
            TextComponent text = ChatUtil.combine(ChatColor.GRAY, "City - ", ChatColor.BLUE, location.city);
            text.addExtra(generateAddButton(info, new Location(Location.Detail.city, location.city, location.county, location.state, location.country)));
            player.sendMessage(text);
        }
        if (!location.county.equals("")) {
            TextComponent text = ChatUtil.combine(ChatColor.GRAY, "County - ", ChatColor.BLUE, location.county);
            text.addExtra(generateAddButton(info, new Location(Location.Detail.county, location.city, location.county, location.state, location.country)));
            player.sendMessage(text);
        }
        if (!location.state.equals("")) {
            TextComponent text = ChatUtil.combine(ChatColor.GRAY, "State - ", ChatColor.BLUE, location.state);
            text.addExtra(generateAddButton(info, new Location(Location.Detail.state, location.city, location.county, location.state, location.country)));
            player.sendMessage(text);
        }
        if (!location.country.equals("")) {
            TextComponent text = ChatUtil.combine(ChatColor.GRAY, "Country - ", ChatColor.BLUE, location.country);
            text.addExtra(generateAddButton(info, new Location(Location.Detail.country, location.city, location.county, location.state, location.country)));
            player.sendMessage(text);
        }
    }

    private TextComponent generateAddButton(PacketInfo info, Location location) {
        TextComponent add = new TextComponent(ChatColor.GREEN + " [+]");
        add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Add location")));
        add.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sha server " + info.getServer() + " addlocation " + SledgehammerUtil.GSON.toJson(location)));
        return add;
    }
}
