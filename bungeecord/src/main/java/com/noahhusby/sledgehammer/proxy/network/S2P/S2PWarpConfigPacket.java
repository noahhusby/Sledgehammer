/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PWarpConfigPacket.java
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

package com.noahhusby.sledgehammer.proxy.network.S2P;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.P2S.P2SWarpConfigPacket;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.network.S2PPacket;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;

public class S2PWarpConfigPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.warpConfigID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject packet) {
        JsonObject data = packet.getAsJsonObject("data");
        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(info.getSender());
        if (!player.validateAction(packet.get("salt").getAsString())) {
            return;
        }
        JsonObject response = new JsonObject();
        boolean g = PlayerHandler.getInstance().isAdmin(player) || player.hasPermission("sledgehammer.warp.edit");

        switch (ProxyConfigAction.valueOf(packet.get("action").getAsString())) {
            case OPEN_CONFIG:
                Permission permission = player.getPermission("sledgehammer.warp.edit");
                if(permission.isLocal()) {
                    NetworkHandler.getInstance().send(new P2SWarpConfigPacket(player, P2SWarpConfigPacket.ServerConfigAction.OPEN_CONFIG, permission.isGlobal()));
                } else {
                    player.sendMessage(ChatUtil.getNoPermission());
                }
                break;
            case CREATE_WARP:
                String warpName = data.get("warpName").getAsString();
                WarpHandler.WarpStatus warpStatus = WarpHandler.getInstance().getWarpStatus(warpName, info.getServer().getName());

                switch (warpStatus) {
                    case EXISTS:
                    case RESERVED:
                        response.addProperty("warpName", warpName);
                        NetworkHandler.getInstance().send(new P2SWarpConfigPacket(
                                player, P2SWarpConfigPacket.ServerConfigAction.ADD_FAILURE, g, response));
                        break;
                    case AVAILABLE:
                        WarpHandler.getInstance().requestNewWarp(warpName, player, (warp) -> {
                            response.addProperty("warpName", warp.getName());
                            response.addProperty("warpId", warp.getId());
                            NetworkHandler.getInstance().send(new P2SWarpConfigPacket(
                                    player, P2SWarpConfigPacket.ServerConfigAction.ADD_SUCCESSFUL, g, response));
                        });
                        break;
                }
                break;
            case UPDATE_WARP:
                int warpId = data.get("id").getAsInt();
                String name = data.get("name").getAsString();
                String headId = data.get("headId").getAsString();
                Warp.PinnedMode pin = Warp.PinnedMode.valueOf(data.get("pinned").getAsString());

                Warp warp = WarpHandler.getInstance().getWarp(warpId);
                if (warp == null) {
                    return;
                }
                warp.setHeadID(headId);
                warp.setName(name);
                warp.setPinned(pin);

                WarpHandler.getInstance().getWarps().saveAsync();
                break;
            case UPDATE_PLAYER_DEFAULT:
                String sort = data.get("sort").getAsString();
                if (sort.equalsIgnoreCase("all")) {
                    player.getAttributes().put("WARP_SORT", "WARP_SORT_ALL");
                } else if (sort.equalsIgnoreCase("group")) {
                    player.getAttributes().put("WARP_SORT", "WARP_SORT_GROUP");
                } else if (sort.equalsIgnoreCase("pinned")) {
                    player.getAttributes().put("WARP_SORT", "WARP_SORT_PINNED");
                }
                break;
            case WARP_UPDATE_LOCATION:
                JsonObject pointJson = data.getAsJsonObject("point");
                Point point = SledgehammerUtil.GSON.fromJson(pointJson, Point.class);
                Warp w = WarpHandler.getInstance().getWarp(data.get("warpId").getAsInt());
                w.setPoint(point);
                w.setServer(info.getServer().getName());
                WarpHandler.getInstance().getWarps().saveAsync();

                response.addProperty("warpId", w.getId());
                NetworkHandler.getInstance().send(new P2SWarpConfigPacket(player,
                        P2SWarpConfigPacket.ServerConfigAction.LOCATION_UPDATE,
                        g, response));
                break;
            case REMOVE_WARP:
                WarpHandler.getInstance().getWarps().remove(WarpHandler.getInstance().getWarp(data.get("warpId").getAsInt()));
                WarpHandler.getInstance().getWarps().saveAsync();
                NetworkHandler.getInstance().send(new P2SWarpConfigPacket(player,
                        P2SWarpConfigPacket.ServerConfigAction.REMOVE_SUCCESSFUL, g));
                break;
        }
    }

    public enum ProxyConfigAction {
        OPEN_CONFIG, CREATE_WARP, UPDATE_WARP, UPDATE_PLAYER_DEFAULT, WARP_UPDATE_LOCATION, REMOVE_WARP
    }
}
