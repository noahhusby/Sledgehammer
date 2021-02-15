/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ConfigScene.java
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

package com.noahhusby.sledgehammer.dialogs.scenes.setup;

import com.noahhusby.sledgehammer.ChatUtil;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.dialogs.components.setup.EarthServerComponent;
import com.noahhusby.sledgehammer.dialogs.components.setup.EditComponent;
import com.noahhusby.sledgehammer.dialogs.components.setup.SledgehammerServerComponent;
import com.noahhusby.sledgehammer.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.dialogs.toolbars.ExitSkipToolbar;
import com.noahhusby.sledgehammer.dialogs.toolbars.IToolbar;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
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
        if(server == null) {
            this.server = ServerConfig.getInstance().getBungeeServers().get(0);
        }
        registerComponent(new EditComponent());
        registerComponent(new SledgehammerServerComponent());
        registerComponent(new EarthServerComponent());
    }

    @Override
    public void onInitialization() {
        if(increase) {
            increase = false;
            LinkedList<ServerInfo> servers = ServerConfig.getInstance().getBungeeServers();

            DialogHandler.getInstance().discardDialog(this);

            if(indexOf(server) + 2 > servers.size()) {
                getCommandSender().sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Finished the setup dialog!"));
                return;
            }

            DialogHandler.getInstance().startDialog(sender, new ConfigScene(servers.get(indexOf(server)+1)));
        }
    }

    @Override
    public void onFinish() {
        SledgehammerServer s = ServerConfig.getInstance().getServer(server.getName());

        if(s == null) s = new SledgehammerServer(server.getName());

        s.setEarthServer(true);
        ServerConfig.getInstance().pushServer(s);

        DialogHandler.getInstance().discardDialog(this);
        DialogHandler.getInstance().startDialog(getCommandSender(), new LocationConfigScene(server));
    }

    @Override
    public IToolbar getToolbar() {
        return new ExitSkipToolbar();
    }

    @Override
    public void onToolbarAction(String m) {
        if(m.equals("exit")) {
            getCommandSender().sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Finished the setup dialog!"));
            DialogHandler.getInstance().discardDialog(this);
        } else if(m.equals("@")) {
            progressDialog("");
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public TextComponent getTitle() {
        List<ServerInfo> servers = ServerConfig.getInstance().getBungeeServers();
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
                DialogHandler.getInstance().discardDialog(this);

                List<ServerInfo> servers = ServerConfig.getInstance().getBungeeServers();
                ServerInfo currentServer = this.server;

                if (indexOf(currentServer) + 2 > servers.size()) {
                    getCommandSender().sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Finished the setup dialog!"));
                    DialogHandler.getInstance().discardDialog(this);
                    return;
                }

                DialogHandler.getInstance().startDialog(sender, new ConfigScene(server, true));
            }
        } else if (getCurrentComponent() instanceof EarthServerComponent) {
            String response = getValue("earth").trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                progressDialog(response, true);
            } else if (response.equals("no") || response.equals("n")) {
                DialogHandler.getInstance().discardDialog(this);

                SledgehammerServer s = ServerConfig.getInstance().getServer(server.getName());
                if(s == null) s = new SledgehammerServer(server.getName());
                s.setEarthServer(false);
                ServerConfig.getInstance().pushServer(s);

                DialogHandler.getInstance().startDialog(sender, new ConfigScene(server, true));
            }
        } else if (getCurrentComponent() instanceof SledgehammerServerComponent) {
            String response = getValue("sledgehammer_server").trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                progressDialog(response, true);
            } else if (response.equals("no") || response.equals("n")) {
                DialogHandler.getInstance().discardDialog(this);

                SledgehammerServer s = ServerConfig.getInstance().getServer(server.getName());
                if(s != null) ServerConfig.getInstance().removeServer(s);

                DialogHandler.getInstance().startDialog(sender, new ConfigScene(server, true));
            }
        }
    }

    private int indexOf(ServerInfo info) {
        for(int x = 0; x < ServerConfig.getInstance().getBungeeServers().size(); x++) {
            if(ServerConfig.getInstance().getBungeeServers().get(x).getName().equalsIgnoreCase(info.getName())) return x;
        }

        return -1;
    }
}
