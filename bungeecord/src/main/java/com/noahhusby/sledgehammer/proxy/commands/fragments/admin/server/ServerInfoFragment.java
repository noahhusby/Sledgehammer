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

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerInfoFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        SledgehammerServer server = ServerHandler.getInstance().getServer(args[0]);
        ServerInfo info = ProxyServer.getInstance().getServerInfo(args[0]);
        ChatUtil.sendMessageBox(sender, ChatColor.YELLOW + "" + ChatColor.BOLD + "Server Report", () -> {
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Name: ", ChatColor.WHITE, info.getName()));
            if (server == null) {
                sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Status: ", ChatColor.RED, "Unconfigured"));
                return;
            }
            TextComponent status = server.isInitialized() ? ChatUtil.combine(ChatColor.YELLOW, "Status: ", ChatColor.GREEN, "Configured") : ChatUtil.combine(ChatColor.YELLOW, "Status: ", ChatColor.GREEN, "Configured",
                    ChatColor.RED, " (Not Initialized)");
            sender.sendMessage(status);
            sender.sendMessage();
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Earth: ", (server.isEarthServer() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")));
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Friendly Name: ", ChatColor.WHITE, server.getFriendlyName()));
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "X Offset: ", ChatColor.WHITE, server.getXOffset()));
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Z Offset: ", ChatColor.WHITE, server.getZOffset()));
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Tpll Mode: ", ChatColor.WHITE, server.getTpllMode().name()));
            if (server.isInitialized()) {
                sender.sendMessage();
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "SH Version: ", ChatColor.WHITE, server.getSledgehammerVersion()));
            }
        });
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPurpose() {
        return "List info about server";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
