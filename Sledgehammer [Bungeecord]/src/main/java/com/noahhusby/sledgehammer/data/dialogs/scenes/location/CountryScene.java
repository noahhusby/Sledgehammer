/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CountryScene.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.data.dialogs.scenes.location;

import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.data.dialogs.components.location.CountryComponent;
import com.noahhusby.sledgehammer.data.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
import net.md_5.bungee.api.config.ServerInfo;

public class CountryScene extends DialogScene {

    private ServerInfo server;
    private DialogScene scene;

    public CountryScene(ServerInfo server) {
        this(server, null);
    }

    public CountryScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new CountryComponent());
    }

    @Override
    public void onFinish() {
        Location l = new Location(Location.detail.country, "", "", "", getValue("country"));
        Server s = ServerConfig.getInstance().getServer(server.getName());

        if(s == null) s = new Server(server.getName());

        s.locations.add(l);
        ServerConfig.getInstance().pushServer(s);
        if(scene != null) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), scene);
        }
    }

    @Override
    public void onToolbarAction(String m) {
        if(m.equals("exit")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new LocationSelectionScene(server));
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
