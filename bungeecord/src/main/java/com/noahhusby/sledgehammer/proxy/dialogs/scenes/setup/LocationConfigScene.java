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

package com.noahhusby.sledgehammer.proxy.dialogs.scenes.setup;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.dialogs.components.setup.LocationMenuComponent;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.location.LocationSelectionScene;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

public class LocationConfigScene extends DialogScene {

    private final ServerInfo server;

    public LocationConfigScene(ServerInfo server) {
        this.server = server;
        registerComponent(new LocationMenuComponent());
    }

    @Override
    public TextComponent getTitle() {
        return ChatUtil.combine(ChatColor.GRAY, "Editing Locations - ", ChatColor.RED, server.getName());
    }

    @Override
    public void onFinish() {
        String v = getValue("location").trim().toLowerCase();
        switch (v) {
            case "add":
                discardAndStart(new LocationSelectionScene(server, new LocationConfigScene(server)));
                break;
            case "finish":
                discardAndStart(new ConfigScene(server, true));
                break;
            case "remove":
                discardAndStart(new LocationRemovalScene(server, new LocationConfigScene(server)));
                break;
            case "list":
                discardAndStart(new LocationListScene(server, new LocationConfigScene(server)));
                break;
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
