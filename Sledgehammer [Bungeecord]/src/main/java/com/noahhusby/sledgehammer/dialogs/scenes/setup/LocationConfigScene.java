/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationConfigScene.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.dialogs.scenes.setup;

import com.noahhusby.sledgehammer.dialogs.components.setup.LocationMenuComponent;
import com.noahhusby.sledgehammer.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.dialogs.scenes.location.LocationSelectionScene;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

public class LocationConfigScene extends DialogScene {

    private ServerInfo server;

    public LocationConfigScene(ServerInfo server) {
        this.server = server;
        registerComponent(new LocationMenuComponent());
    }

    @Override
    public TextElement[] getTitle() {
        return new TextElement[]{new TextElement("Editing Locations - ", ChatColor.GRAY),
        new TextElement(server.getName(), ChatColor.RED)};
    }

    @Override
    public void onFinish() {
        String v = getValue("location").trim().toLowerCase();
        if(v.equals("add")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new LocationSelectionScene(server, new LocationConfigScene(server)));
        } else if(v.equals("finish")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new ConfigScene(server, true));
        } else if(v.equals("remove")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new LocationRemovalScene(server, new LocationConfigScene(server)));
        } else if(v.equals("list")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new LocationListScene(server, new LocationConfigScene(server)));
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
