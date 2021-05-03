/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationListScene.java
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
import com.noahhusby.sledgehammer.proxy.dialogs.components.location.LocationListComponent;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.DialogScene;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

public class LocationListScene extends DialogScene {

    private final ServerInfo server;
    private final DialogScene scene;

    public LocationListScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new LocationListComponent(server));
    }

    @Override
    public TextComponent getTitle() {
        return ChatUtil.combine(ChatColor.GRAY, "Locations - ", ChatColor.RED, server.getName());
    }

    @Override
    public void onFinish() {
        discard();
        if (scene != null) {
            start(scene);
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
