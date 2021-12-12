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
