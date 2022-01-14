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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin;

import com.noahhusby.sledgehammer.common.SledgehammerVersion;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class VersionFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        ChatUtil.sendMessageBox(sender, ChatColor.BLUE + "" + ChatColor.BOLD + "Version Information", () -> {
            SledgehammerVersion proxyVersion = Constants.VERSION;
            sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Proxy Version: ", ChatColor.WHITE, proxyVersion.toString()));
            int older = 0;
            int current = 0;
            int newer = 0;
            for (SledgehammerServer server : ServerHandler.getInstance().getServers().values()) {
                if (server.isInitialized()) {
                    SledgehammerVersion serverVersion = server.getSledgehammerVersion();
                    if (serverVersion.isNewer(proxyVersion)) {
                        newer++;
                    } else if (serverVersion.isOlder(proxyVersion)) {
                        older++;
                    } else {
                        current++;
                    }
                }
            }
            sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Outdated Servers: ", ChatColor.WHITE, older));
            sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Up-to-date Servers: ", ChatColor.WHITE, current));
            sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Newer Servers: ", ChatColor.WHITE, newer));
        });
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getPurpose() {
        return "Display version information";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
