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
import com.noahhusby.sledgehammer.common.warps.Page;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.network.S2PPacket;
import com.noahhusby.sledgehammer.proxy.network.p2s.P2SWarpConfigPacket;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;

import java.util.concurrent.CompletableFuture;

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
                CompletableFuture<Permission> permissionFuture = player.getPermission("sledgehammer.warp.edit");
                permissionFuture.thenAccept(permission -> {
                    if (permission.isLocal()) {
                        NetworkHandler.getInstance().send(new P2SWarpConfigPacket(player, WarpConfigPayload.ServerConfigAction.OPEN_CONFIG, permission.isGlobal()));
                    } else {
                        player.sendMessage(ChatUtil.getNoPermission());
                    }
                });
                break;
            case CREATE_WARP:
                String warpName = data.get("warpName").getAsString();
                WarpHandler.WarpStatus warpStatus = WarpHandler.getInstance().getWarpStatus(warpName, info.getServer().getName());

                switch (warpStatus) {
                    case EXISTS:
                    case RESERVED:
                        response.addProperty("warpName", warpName);
                        NetworkHandler.getInstance().send(new P2SWarpConfigPacket(
                                player, WarpConfigPayload.ServerConfigAction.ADD_FAILURE, g, response));
                        break;
                    case AVAILABLE:
                        WarpHandler.getInstance().requestNewWarp(warpName, player, (warp) -> {
                            response.addProperty("warpName", warp.getName());
                            response.addProperty("warpId", warp.getId());
                            NetworkHandler.getInstance().send(new P2SWarpConfigPacket(
                                    player, WarpConfigPayload.ServerConfigAction.ADD_SUCCESSFUL, g, response));
                        });
                        break;
                }
                break;
            case UPDATE_WARP:
                Warp incomingWarp = SledgehammerUtil.GSON.fromJson(data, Warp.class);

                Warp warp = WarpHandler.getInstance().getWarp(incomingWarp.getId());
                if (warp == null) {
                    return;
                }
                warp.setHeadID(incomingWarp.getHeadID());
                warp.setName(incomingWarp.getName());
                //warp.setPinned(pin);

                WarpHandler.getInstance().getWarps().saveAsync();
                break;
            case UPDATE_PLAYER_DEFAULT:
                String sort = data.get("sort").getAsString();
                if (sort.equalsIgnoreCase("all")) {
                    player.getAttributes().put("WARP_SORT", Page.ALL.name());
                } else if (sort.equalsIgnoreCase("group")) {
                    player.getAttributes().put("WARP_SORT", Page.GROUPS.name());
                } else if (sort.equalsIgnoreCase("server")) {
                    player.getAttributes().put("WARP_SORT", Page.SERVERS.name());
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
                        WarpConfigPayload.ServerConfigAction.LOCATION_UPDATE,
                        g, response));
                break;
            case REMOVE_WARP:
                WarpHandler.getInstance().getWarps().remove(data.get("warpId").getAsInt());
                WarpHandler.getInstance().getWarps().saveAsync();
                NetworkHandler.getInstance().send(new P2SWarpConfigPacket(player,
                        WarpConfigPayload.ServerConfigAction.REMOVE_SUCCESSFUL, g));
                break;
        }
    }

    public enum ProxyConfigAction {
        OPEN_CONFIG, CREATE_WARP, UPDATE_WARP, UPDATE_PLAYER_DEFAULT, WARP_UPDATE_LOCATION, REMOVE_WARP
    }
}
