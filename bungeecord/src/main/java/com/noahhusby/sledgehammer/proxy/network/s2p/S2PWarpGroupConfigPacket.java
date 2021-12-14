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
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.common.warps.WarpGroupConfigPayload;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.PacketInfo;
import com.noahhusby.sledgehammer.proxy.network.S2PPacket;
import com.noahhusby.sledgehammer.proxy.network.p2s.P2SWarpGroupConfigPacket;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;

import java.util.concurrent.CompletableFuture;

public class S2PWarpGroupConfigPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.warpGroupConfigID;
    }

    @Override
    public void onMessage(PacketInfo info, JsonObject packet) {
        JsonObject data = packet.getAsJsonObject("data");
        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(info.getSender());
        if (!player.validateAction(packet.get("salt").getAsString())) {
            return;
        }
        boolean g = PlayerHandler.getInstance().isAdmin(player) || player.hasPermission("sledgehammer.warp.edit");
        switch (ProxyConfigAction.valueOf(packet.get("action").getAsString())) {
            case OPEN_CONFIG:
                CompletableFuture<Permission> permissionFuture = player.getPermission("sledgehammer.warp.edit");
                permissionFuture.thenAccept(permission -> {
                    if (permission.isLocal()) {
                        NetworkHandler.getInstance().send(new P2SWarpGroupConfigPacket(player, WarpGroupConfigPayload.GroupConfigAction.OPEN_CONFIG, permission.isGlobal()));
                    } else {
                        player.sendMessage(ChatUtil.getNoPermission());
                    }
                });
                break;
            case CREATE_GROUP:
                String name = data.get("groupName").getAsString();
                WarpGroup group = new WarpGroup();
                group.setId(SledgehammerUtil.getSaltString());
                group.setName(name);
                WarpHandler.getInstance().getWarpGroups().put(group.getId(), group);
                WarpHandler.getInstance().getWarpGroups().saveAsync();
                NetworkHandler.getInstance().send(new P2SWarpGroupConfigPacket(player, WarpGroupConfigPayload.GroupConfigAction.ADD_SUCCESSFUL, g));
                break;
            case UPDATE_GROUP:
                WarpGroup incomingGroup = SledgehammerUtil.GSON.fromJson(data, WarpGroup.class);
                WarpGroup warpGroup = WarpHandler.getInstance().getWarpGroups().get(incomingGroup.getId());
                if (warpGroup == null) {
                    return;
                }
                warpGroup.setHeadId(incomingGroup.getHeadId());
                warpGroup.setName(incomingGroup.getName());
                warpGroup.setId(incomingGroup.getId());
                warpGroup.setType(incomingGroup.getType());
                warpGroup.setServers(incomingGroup.getServers());
                warpGroup.setWarps(incomingGroup.getWarps());
                WarpHandler.getInstance().getWarpGroups().saveAsync();
                break;
            case REMOVE_GROUP:
                WarpHandler.getInstance().getWarpGroups().remove(data.get("groupId").getAsString());
                WarpHandler.getInstance().getWarpGroups().saveAsync();
                NetworkHandler.getInstance().send(new P2SWarpGroupConfigPacket(player,
                        WarpGroupConfigPayload.GroupConfigAction.REMOVE_SUCCESSFUL, g));
                break;
        }
    }

    public enum ProxyConfigAction {
        OPEN_CONFIG, CREATE_GROUP, UPDATE_GROUP, REMOVE_GROUP
    }
}
