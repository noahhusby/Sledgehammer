/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerAddLocationFragment.java
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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server;

import com.noahhusby.lib.data.JsonUtils;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.datasets.Location;
import com.noahhusby.sledgehammer.proxy.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.location.CityScene;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.location.CountryScene;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.location.CountyScene;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.location.LocationSelectionScene;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.location.StateScene;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.util.Arrays;

public class ServerAddLocationFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatUtil.getPlayerOnly());
            return;
        }

        if (ServerHandler.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatUtil.notSledgehammerServer);
            return;
        }

        if (!ServerHandler.getInstance().getServer(args[0]).isEarthServer()) {
            sender.sendMessage(ChatUtil.notEarthServer);
            return;
        }

        if (args.length > 2) {
            String arg = SledgehammerUtil.getRawArguments(Arrays.copyOfRange(args, 2, args.length));
            if (arg.equalsIgnoreCase("city")) {
                DialogHandler.getInstance().startDialog(sender, new CityScene(ProxyServer.getInstance().getServerInfo(args[0])));
                return;
            } else if (arg.equalsIgnoreCase("county")) {
                DialogHandler.getInstance().startDialog(sender, new CountyScene(ProxyServer.getInstance().getServerInfo(args[0])));
                return;
            } else if (arg.equalsIgnoreCase("state")) {
                DialogHandler.getInstance().startDialog(sender, new StateScene(ProxyServer.getInstance().getServerInfo(args[0])));
                return;
            } else if (arg.equalsIgnoreCase("country")) {
                DialogHandler.getInstance().startDialog(sender, new CountryScene(ProxyServer.getInstance().getServerInfo(args[0])));
                return;
            }

            boolean validJson = false;
            try {
                validJson = JsonUtils.isJsonValid(arg);
            } catch (IOException ignored) {
            }

            if (validJson) {
                Location l = SledgehammerUtil.GSON.fromJson(arg, Location.class);
                if (l == null) {
                    sender.sendMessage(ChatUtil.combine(ChatColor.RED, "Unable to parse json location! Please try again."));
                    return;
                }

                SledgehammerServer s = ServerHandler.getInstance().getServer(args[0]);
                s.getLocations().add(l);
                ServerHandler.getInstance().getServers().saveAsync();
                sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Successfully added ", ChatColor.BLUE, l.detailType.name() + ": ", ChatColor.RED, l));
                return;
            } else if (arg.contains("{")) {
                sender.sendMessage(ChatUtil.combine(ChatColor.RED, "Unable to parse json location! Please try again."));
                return;
            }

        }
        DialogHandler.getInstance().startDialog(sender, new LocationSelectionScene(ProxyServer.getInstance().getServerInfo(args[0])));
    }

    @Override
    public String getName() {
        return "addlocation";
    }

    @Override
    public String getPurpose() {
        return "Add a location to the server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "[city|county|state|county|{json}]" };
    }
}
