/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - TestLocationFragment.java
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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.P2S.P2STestLocationPacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TestLocationFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatUtil.getNoPermission());
            return;
        }

        if (args.length == 0) {
            NetworkHandler.getInstance().send(new P2STestLocationPacket(sender.getName(),
                    SledgehammerUtil.getServerFromSender(sender).getName(), -1));
        } else {
            try {
                int zoom = Integer.parseInt(args[0]);

                if (zoom < 1 || zoom > 19) {
                    throw new Exception();
                }

                NetworkHandler.getInstance().send(new P2STestLocationPacket(sender.getName(),
                        SledgehammerUtil.getServerFromSender(sender).getName(), zoom));
            } catch (Exception e) {
                sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Invalid zoom level! ", ChatColor.GRAY,
                        "Please enter a value between ", ChatColor.BLUE, 1, ChatColor.GRAY, " and ", ChatColor.BLUE, "19"));
            }
        }
    }

    @Override
    public String getName() {
        return "testlocation";
    }

    @Override
    public String getPurpose() {
        return "See what the API sees at the current location";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "[zoom]" };
    }
}
