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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.FragmentManager;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerAddLocationFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerEarthModeFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerInfoFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerListLocationFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerOffsetFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerRemoveLocationFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerSHSelectFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerSetFriendlyFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerStealthModeFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerTpllModeFragment;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Map;

import static net.md_5.bungee.api.ProxyServer.*;

public class ServerFragment implements ICommandFragment {

    private final FragmentManager manager = new FragmentManager("/sha server <server name>", ChatColor.YELLOW + "Servers");

    public ServerFragment() {
        manager.register(new ServerInfoFragment());
        manager.register(new ServerAddLocationFragment());
        manager.register(new ServerRemoveLocationFragment());
        manager.register(new ServerListLocationFragment());
        manager.register(new ServerEarthModeFragment());
        manager.register(new ServerSHSelectFragment());
        manager.register(new ServerStealthModeFragment());
        manager.register(new ServerSetFriendlyFragment());
        manager.register(new ServerOffsetFragment());
        manager.register(new ServerTpllModeFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            for (Map.Entry<String, ServerInfo> s : getInstance().getServers().entrySet()) {
                if (s.getValue().getName().equalsIgnoreCase(args[0])) {
                    manager.setBase(String.format("/sha server %s", args[0]));
                    manager.execute(sender, args, 1);
                    return;
                }
            }
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.DARK_RED, args[0], ChatColor.RED, " is not a bungeecord server!"));
            return;
        }
        manager.setBase("/sha server <server name>");
        manager.execute(sender, args, 1);
    }

    @Override
    public String getName() {
        return "server";
    }

    @Override
    public String getPurpose() {
        return "Configure settings of sledgehammer server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "<server name>" };
    }
}
