/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CountryScene.java
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

package com.noahhusby.sledgehammer.proxy.dialogs.scenes.location;

import com.noahhusby.sledgehammer.proxy.datasets.Location;
import com.noahhusby.sledgehammer.proxy.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.proxy.dialogs.components.location.CountryComponent;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.DialogScene;
import net.md_5.bungee.api.config.ServerInfo;

public class CountryScene extends DialogScene {

    private final ServerInfo server;
    private final DialogScene scene;

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
        Location l = new Location(Location.Detail.country, "", "", "", getValue("country"));
        LocationSceneUtil.completeScene(this, server, l, scene);
    }

    @Override
    public void onToolbarAction(String m) {
        if (m.equals("exit")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new LocationSelectionScene(server));
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
