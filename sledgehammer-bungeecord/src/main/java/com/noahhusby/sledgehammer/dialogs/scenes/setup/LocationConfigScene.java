/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationConfigScene.java
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

import com.noahhusby.sledgehammer.dialogs.components.setup.LocationMenuComponent;
import com.noahhusby.sledgehammer.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.dialogs.scenes.location.LocationSelectionScene;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

public class LocationConfigScene extends DialogScene {

    private final ServerInfo server;

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
