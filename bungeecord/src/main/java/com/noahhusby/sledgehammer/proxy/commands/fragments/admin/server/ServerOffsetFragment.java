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

import com.noahhusby.sledgehammer.proxy.utils.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ServerOffsetFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (ServerHandler.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatUtil.notSledgehammerServer);
            return;
        }

        if (args.length < 4) {
            sender.sendMessage(ChatUtil.combine(ChatColor.RED, "Usage: /sha server <server name> setoffset <x/z> offset"));
            return;
        }

        SledgehammerServer s = ServerHandler.getInstance().getServer(args[0]);
        String axis = args[2];
        if (!(axis.equalsIgnoreCase("x") || axis.equalsIgnoreCase("z"))) {
            sender.sendMessage(ChatUtil.combine(ChatColor.RED, "Usage: /sha server <server name> setoffset <x/z> offset"));
            return;
        }

        String valString = args[3];
        int value;
        try {
            value = Integer.parseInt(valString);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatUtil.combine(ChatColor.RED, "Usage: /sha server <server name> setoffset <x/z> offset"));
            return;
        }

        if (axis.equalsIgnoreCase("z")) {
            s.setZOffset(value);
            sender.sendMessage(ChatUtil.getValueMessage("zOffset", valString, s.getName()));
        } else {
            s.setXOffset(value);
            sender.sendMessage(ChatUtil.getValueMessage("xOffset", valString, s.getName()));
        }

        ServerHandler.getInstance().getServers().saveAsync();
    }

    @Override
    public String getName() {
        return "setoffset";
    }

    @Override
    public String getPurpose() {
        return "Set the tpll offset";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "<x|z> <offset>" };
    }
}
