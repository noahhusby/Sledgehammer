/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationListScene.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.dialogs.scenes.setup;

import com.noahhusby.sledgehammer.dialogs.components.location.LocationListComponent;
import com.noahhusby.sledgehammer.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

public class LocationListScene extends DialogScene {

    private ServerInfo server;
    private DialogScene scene;

    public LocationListScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new LocationListComponent(server));
    }

    @Override
    public TextElement[] getTitle() {
        return new TextElement[]{new TextElement("Locations - ", ChatColor.GRAY),
        new TextElement(server.getName(), ChatColor.RED)};
    }

    @Override
    public void onFinish() {
        DialogHandler.getInstance().discardDialog(this);
        if(scene != null) {
            DialogHandler.getInstance().startDialog(getCommandSender(), scene);
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
