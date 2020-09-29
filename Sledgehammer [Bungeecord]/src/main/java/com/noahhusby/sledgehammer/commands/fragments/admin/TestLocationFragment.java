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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.commands.fragments.admin;

import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.network.P2S.P2STestLocationPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class TestLocationFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            SledgehammerNetworkManager.getInstance().sendPacket(new P2STestLocationPacket(sender.getName(),
                    SledgehammerUtil.getServerNameByPlayer(sender), -1));
        } else {
            try {
                int zoom = Integer.parseInt(args[0]);

                if(zoom < 1 || zoom > 19) {
                    throw new Exception();
                }

                SledgehammerNetworkManager.getInstance().sendPacket(new P2STestLocationPacket(sender.getName(),
                        SledgehammerUtil.getServerNameByPlayer(sender), zoom));
            } catch (Exception e) {
                sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Invalid zoom level!", ChatColor.RED),
                        new TextElement(" Please enter a value between ", ChatColor.GRAY),
                        new TextElement("1", ChatColor.BLUE), new TextElement(" and ", ChatColor.GRAY), new TextElement("19", ChatColor.BLUE)));
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
        return new String[]{"[zoom]"};
    }
}
