/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
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

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.Locale;

public class ServerSHSelectFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha server <server name> setsledgehammer <true/false>"));
        } else {
            String arg = args[2].toLowerCase();
            if (arg.equals("true") || arg.equals("false")) {

                if (!Boolean.parseBoolean(arg)) {
                    ServerHandler.getInstance().getServers().remove(args[0]);
                } else {
                    if (!ServerHandler.getInstance().getServers().containsKey(args[0])) {
                        ServerHandler.getInstance().getServers().put(args[0], new SledgehammerServer(args[0]));
                        ServerHandler.getInstance().getServers().saveAsync();
                    }
                }

                sender.sendMessage(ChatUtil.getValueMessage("runs_sledgehammer", arg, args[0].toLowerCase(Locale.ROOT)));
            } else {
                sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha server <server name> setsledgehammer <true/false>"));
            }
        }
    }

    @Override
    public String getName() {
        return "setsledgehammer";
    }

    @Override
    public String getPurpose() {
        return "Set whether the server is running sledgehammer or not";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "<true/false>" };
    }
}
