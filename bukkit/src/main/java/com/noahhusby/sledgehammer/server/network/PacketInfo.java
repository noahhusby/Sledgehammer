/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - PacketInfo.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.server.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class PacketInfo {
    private final String id;
    private final String sender;
    private final double time;

    public static PacketInfo build(String id, String sender) {
        return new PacketInfo(id, sender, System.currentTimeMillis());
    }

    public static PacketInfo build(String id, Player player) {
        return build(id, player.getName());
    }

    public static PacketInfo renew(PacketInfo info) {
        return new PacketInfo(info.id, info.sender, System.currentTimeMillis());
    }
}
