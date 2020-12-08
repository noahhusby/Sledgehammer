/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpCommand.java
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

package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import com.noahhusby.sledgehammer.maps.MapHandler;
import com.noahhusby.sledgehammer.network.P2S.P2STeleportPacket;
import com.noahhusby.sledgehammer.network.P2S.P2SWarpGUIPacket;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class OllleeWarpppCom extends Command {

    public OllleeWarpppCom() {
        super(ConfigHandler.warpCommand, "sledgehammer.warp");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 1) {

            if (args[0].equalsIgnoreCase("config")) {
                //TODO: CHECK FOR BUNGEE PERMISSIONS

            } else if (args[0].equals("map")) {
                MapHandler.getInstance().newMapCommand(sender);
            }
        }
    }
}
