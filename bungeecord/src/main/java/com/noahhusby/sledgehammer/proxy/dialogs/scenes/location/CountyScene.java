/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CountyScene.java
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
import com.noahhusby.sledgehammer.proxy.dialogs.components.location.CountyComponent;
import com.noahhusby.sledgehammer.proxy.dialogs.components.location.StateComponent;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.proxy.dialogs.toolbars.ExitSkipToolbar;
import com.noahhusby.sledgehammer.proxy.dialogs.toolbars.IToolbar;
import net.md_5.bungee.api.config.ServerInfo;

public class CountyScene extends DialogScene {

    private final ServerInfo server;
    private final DialogScene scene;

    public CountyScene(ServerInfo server) {
        this(server, null);
    }

    public CountyScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new CountyComponent());
        registerComponent(new StateComponent());
        registerComponent(new CountryComponent());
    }

    @Override
    public void onFinish() {
        Location l = new Location(Location.Detail.state, "", getValue("county"), getValue("state"), getValue("country"));
        LocationSceneUtil.completeScene(this, server, l, scene);
    }

    @Override
    public IToolbar getToolbar() {
        return new ExitSkipToolbar();
    }

    @Override
    public void onToolbarAction(String m) {
        if (m.equals("exit")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new LocationSelectionScene(server));
        } else if (m.equals("@")) {
            progressDialog("");
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
