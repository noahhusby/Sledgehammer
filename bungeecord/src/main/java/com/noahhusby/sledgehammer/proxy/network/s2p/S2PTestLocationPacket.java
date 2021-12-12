/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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
