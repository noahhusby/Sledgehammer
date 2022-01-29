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
import com.noahhusby.sledgehammer.proxy.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.setup.LocationRemovalScene;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerRemoveLocationFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (ServerHandler.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatUtil.notSledgehammerServer);
            return;
        }

        if (!ServerHandler.getInstance().getServer(args[0]).isEarthServer()) {
            sender.sendMessage(ChatUtil.notEarthServer);
            return;
        }

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatUtil.getNoPermission());
            return;
        }
        DialogHandler.getInstance().startDialog(sender, new LocationRemovalScene(ProxyServer.getInstance().getServerInfo(args[0]), null));
    }

    @Override
    public String getName() {
        return "removelocation";
    }

    @Override
    public String getPurpose() {
        return "Remove a location on the server";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
