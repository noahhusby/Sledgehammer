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

package com.noahhusby.sledgehammer.proxy.dialogs.scenes.setup;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.proxy.dialogs.components.setup.EarthServerComponent;
import com.noahhusby.sledgehammer.proxy.dialogs.components.setup.EditComponent;
import com.noahhusby.sledgehammer.proxy.dialogs.components.setup.SledgehammerServerComponent;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.proxy.dialogs.toolbars.ExitSkipToolbar;
import com.noahhusby.sledgehammer.proxy.dialogs.toolbars.IToolbar;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.LinkedList;
import java.util.List;

public class ConfigScene extends DialogScene {

    private ServerInfo server;
    private boolean increase;

    public ConfigScene(ServerInfo server) {
        this(server, false);
    }

    public ConfigScene(ServerInfo server, boolean increase) {
        this.server = server;
        this.increase = increase;
        if (server == null) {
            this.server = ServerHandler.getInstance().getBungeeServers().get(0);
        }
        registerComponent(new EditComponent());
        registerComponent(new SledgehammerServerComponent());
        registerComponent(new EarthServerComponent());
    }

    @Override
    public void onInitialization() {
        if (increase) {
            increase = false;
            LinkedList<ServerInfo> servers = ServerHandler.getInstance().getBungeeServers();

            DialogHandler.getInstance().discardDialog(this);

            if (indexOf(server) + 2 > servers.size()) {
                getCommandSender().sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Finished the setup dialog!"));
                return;
            }

            DialogHandler.getInstance().startDialog(sender, new ConfigScene(servers.get(indexOf(server) + 1)));
        }
    }

    @Override
    public void onFinish() {
        SledgehammerServer s = ServerHandler.getInstance().getServers().containsKey(server.getName()) ? ServerHandler.getInstance().getServer(server.getName()) : new SledgehammerServer(server.getName());
        s.setEarthServer(true);
        ServerHandler.getInstance().getServers().put(server.getName(), s);
        ServerHandler.getInstance().getServers().saveAsync();

        DialogHandler.getInstance().discardDialog(this);
        DialogHandler.getInstance().startDialog(getCommandSender(), new LocationConfigScene(server));
    }

    @Override
    public IToolbar getToolbar() {
        return new ExitSkipToolbar();
    }

    @Override
    public void onToolbarAction(String m) {
        if (m.equals("exit")) {
            getCommandSender().sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Finished the setup dialog!"));
            discard();
        } else if (m.equals("@")) {
            progressDialog("");
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public TextComponent getTitle() {
        List<ServerInfo> servers = ServerHandler.getInstance().getBungeeServers();
        ServerInfo currentServer = this.server;

        return ChatUtil.combine(ChatColor.GRAY, "Server ", ChatColor.GREEN, String.valueOf(indexOf(currentServer) + 1),
                ChatColor.GRAY, " of ", ChatColor.GREEN, String.valueOf(servers.size()), ChatColor.GRAY, " - ",
                ChatColor.RED, currentServer.getName());
    }

    @Override
    public void onComponentFinish() {
        if (getCurrentComponent() instanceof EditComponent) {
            String response = getValue("edit").trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                progressDialog(response, true);
            } else if (response.equals("no") || response.equals("n")) {
                discard();

                List<ServerInfo> servers = ServerHandler.getInstance().getBungeeServers();
                ServerInfo currentServer = this.server;

                if (indexOf(currentServer) + 2 > servers.size()) {
                    getCommandSender().sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Finished the setup dialog!"));
                    discard();
                    return;
                }

                start(new ConfigScene(server, true));
            }
        } else if (getCurrentComponent() instanceof EarthServerComponent) {
            String response = getValue("earth").trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                progressDialog(response, true);
            } else if (response.equals("no") || response.equals("n")) {
                discard();

                SledgehammerServer s = ServerHandler.getInstance().getServers().containsKey(server.getName()) ? ServerHandler.getInstance().getServer(server.getName()) : new SledgehammerServer(server.getName());
                s.setEarthServer(false);
                ServerHandler.getInstance().getServers().put(server.getName(), s);
                ServerHandler.getInstance().getServers().saveAsync();

                DialogHandler.getInstance().startDialog(sender, new ConfigScene(server, true));
            }
        } else if (getCurrentComponent() instanceof SledgehammerServerComponent) {
            String response = getValue("sledgehammer_server").trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                progressDialog(response, true);
            } else if (response.equals("no") || response.equals("n")) {
                DialogHandler.getInstance().discardDialog(this);

                SledgehammerServer s = ServerHandler.getInstance().getServer(server.getName());
                if (s != null) {
                    ServerHandler.getInstance().removeServer(s);
                }

                DialogHandler.getInstance().startDialog(sender, new ConfigScene(server, true));
            }
        }
    }

    private int indexOf(ServerInfo info) {
        for (int x = 0; x < ServerHandler.getInstance().getBungeeServers().size(); x++) {
            if (ServerHandler.getInstance().getBungeeServers().get(x).getName().equalsIgnoreCase(info.getName())) {
                return x;
            }
        }

        return -1;
    }
}
