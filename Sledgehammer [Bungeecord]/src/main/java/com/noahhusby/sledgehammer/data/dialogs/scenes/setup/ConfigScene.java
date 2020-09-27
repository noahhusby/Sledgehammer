/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ConfigScene.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.data.dialogs.scenes.setup;

import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.data.dialogs.components.setup.EarthServerComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.setup.EditComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.setup.PermissionComponent;
import com.noahhusby.sledgehammer.data.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.data.dialogs.toolbars.ExitSkipToolbar;
import com.noahhusby.sledgehammer.data.dialogs.toolbars.IToolbar;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

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
        registerComponent(new EarthServerComponent());
        registerComponent(new PermissionComponent());
    }

    @Override
    public void onInitialization() {
        if(increase) {
            increase = false;
            List<ServerInfo> servers = ServerConfig.getInstance().getBungeeServers();

            if(servers.indexOf(server) + 2 > servers.size()) {
                getCommandSender().sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("Finished the setup dialog!", ChatColor.RED)));
                DialogHandler.getInstance().discardDialog(this);
                return;
            }

            DialogHandler.getInstance().startDialog(sender, new ConfigScene(servers.get(servers.indexOf(server)+1)));
        }
    }

    @Override
    public void onFinish() {
        Server s = ServerConfig.getInstance().getServer(server.getName());

        if(s == null) s = new Server(server.getName());

        s.name = server.getName();
        s.permission_type = getValue("permission");
        s.earthServer = true;
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
            getCommandSender().sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("Finished the setup dialog!", ChatColor.RED)));
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
    public TextElement[] getTitle() {
        List<ServerInfo> servers = ServerConfig.getInstance().getBungeeServers();
        ServerInfo currentServer = this.server;

        return new TextElement[]{new TextElement("Server ", ChatColor.GRAY), new TextElement(String.valueOf(servers.indexOf(currentServer)+1), ChatColor.GREEN),
        new TextElement(" of ", ChatColor.GRAY), new TextElement(String.valueOf(servers.size()), ChatColor.GREEN), new TextElement(" - ", ChatColor.GRAY),
        new TextElement(currentServer.getName(), ChatColor.RED)};
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

                if (servers.indexOf(currentServer) + 2 > servers.size()) {
                    getCommandSender().sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("Finished the setup dialog!", ChatColor.RED)));
                    DialogHandler.getInstance().discardDialog(this);
                    return;
                }

                DialogHandler.getInstance().startDialog(sender, new ConfigScene(servers.get(servers.indexOf(currentServer) + 1)));
            }
        } else if (getCurrentComponent() instanceof EarthServerComponent) {
            String response = getValue("earth").trim().toLowerCase();
            if (response.equals("yes") || response.equals("y")) {
                progressDialog(response, true);
            } else if (response.equals("no") || response.equals("n")) {
                DialogHandler.getInstance().discardDialog(this);

                Server s = ServerConfig.getInstance().getServer(server.getName());
                if(s == null) s = new Server(server.getName());
                s.earthServer = false;
                ServerConfig.getInstance().pushServer(s);

                DialogHandler.getInstance().startDialog(sender, new ConfigScene(server, true));
            }
        }
    }
}
