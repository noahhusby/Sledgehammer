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

package com.noahhusby.sledgehammer.network.S2P;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.ChatUtil;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.json.simple.JSONObject;

public class S2PTestLocationPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.testLocationID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject data) {
        new Thread(() -> {
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
            if (!online.city.equals("")) {
                TextComponent add = new TextComponent(ChatColor.GREEN + " [+]");
                add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add location").create()));
                add.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sha server " +
                        info.getServer() + " addlocation " +
                        SledgehammerUtil.JsonUtils.gson.toJson(new Location(Location.Detail.city,
                                online.city, online.county, online.state, online.country))));

                TextComponent text = ChatUtil.combine(ChatColor.GRAY, "City - ", ChatColor.BLUE, online.city);
                text.addExtra(add);

                player.sendMessage(text);
            }
            if (!online.county.equals("")) {
                TextComponent add = new TextComponent(ChatColor.GREEN + " [+]");
                add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add location").create()));
                add.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sha server " +
                        info.getServer() + " addlocation " +
                        SledgehammerUtil.JsonUtils.gson.toJson(new Location(Location.Detail.county,
                                online.city, online.county, online.state, online.country))));

                TextComponent text = ChatUtil.combine(ChatColor.GRAY, "County - ", ChatColor.BLUE, online.county);
                text.addExtra(add);

                player.sendMessage(text);
            }
            if (!online.state.equals("")) {
                TextComponent add = new TextComponent(ChatColor.GREEN + " [+]");
                add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add location").create()));
                add.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sha server " +
                        info.getServer() + " addlocation " +
                        SledgehammerUtil.JsonUtils.gson.toJson(new Location(Location.Detail.state,
                                online.city, online.county, online.state, online.country))));

                TextComponent text = ChatUtil.combine(ChatColor.GRAY, "State - ", ChatColor.BLUE, online.state);
                text.addExtra(add);

                player.sendMessage(text);
            }
            if (!online.country.equals("")) {
                TextComponent add = new TextComponent(ChatColor.GREEN + " [+]");
                add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add location").create()));
                add.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sha server " +
                        info.getServer() + " addlocation " +
                        SledgehammerUtil.JsonUtils.gson.toJson(new Location(Location.Detail.country,
                                online.city, online.county, online.state, online.country))));

                TextComponent text = ChatUtil.combine(ChatColor.GRAY, "Country - ", ChatColor.BLUE, online.country);
                text.addExtra(add);

                player.sendMessage(text);
            }
            if (!ConfigHandler.useOfflineMode) {
                player.sendMessage(ChatUtil.combine(ChatColor.RED, "Offline: ", ChatColor.DARK_RED, "Disabled"));
            } else {
                Location offline = OpenStreetMaps.getInstance().getOfflineLocation(proj[0], proj[1]);
                player.sendMessage(ChatUtil.combine(ChatColor.RED, "Offline ", ChatColor.GRAY, "(",
                        ChatColor.GREEN, "Active", ChatColor.GRAY, "):"));
                if (!offline.city.equals("")) {
                    TextComponent add = new TextComponent(ChatColor.GREEN + " [+]");
                    add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add location").create()));
                    add.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sha server " +
                            info.getServer() + " addlocation " +
                            SledgehammerUtil.JsonUtils.gson.toJson(new Location(Location.Detail.city,
                                    offline.city, offline.county, offline.state, offline.country))));

                    TextComponent text = ChatUtil.combine(ChatColor.GRAY, "City - ", ChatColor.BLUE, offline.city);
                    text.addExtra(add);

                    player.sendMessage(text);
                }
                if (!offline.county.equals("")) {
                    TextComponent add = new TextComponent(ChatColor.GREEN + " [+]");
                    add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add location").create()));
                    add.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sha server " +
                            info.getServer() + " addlocation " +
                            SledgehammerUtil.JsonUtils.gson.toJson(new Location(Location.Detail.county,
                                    offline.city, offline.county, offline.state, offline.country))));

                    TextComponent text = ChatUtil.combine(ChatColor.GRAY, "County - ", ChatColor.BLUE, offline.county);
                    text.addExtra(add);

                    player.sendMessage(text);
                }
                if (!offline.state.equals("")) {
                    TextComponent add = new TextComponent(ChatColor.GREEN + " [+]");
                    add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add location").create()));
                    add.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sha server " +
                            info.getServer() + " addlocation " +
                            SledgehammerUtil.JsonUtils.gson.toJson(new Location(Location.Detail.state,
                                    offline.city, offline.county, offline.state, offline.country))));

                    TextComponent text = ChatUtil.combine(ChatColor.GRAY, "State - ", ChatColor.BLUE, offline.state);
                    text.addExtra(add);

                    player.sendMessage(text);
                }
                if (!offline.country.equals("")) {
                    TextComponent add = new TextComponent(ChatColor.GREEN + " [+]");
                    add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add location").create()));
                    add.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sha server " +
                            info.getServer() + " addlocation " +
                            SledgehammerUtil.JsonUtils.gson.toJson(new Location(Location.Detail.country,
                                    offline.city, offline.county, offline.state, offline.country))));

                    TextComponent text = ChatUtil.combine(ChatColor.GRAY, "Country - ", ChatColor.BLUE, offline.country);
                    text.addExtra(add);

                    player.sendMessage(text);
                }
            }

        }).start();
    }
}
