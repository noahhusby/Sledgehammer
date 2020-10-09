/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CityScene.java
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

package com.noahhusby.sledgehammer.dialogs.scenes.location;

import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.SledgehammerServer;
import com.noahhusby.sledgehammer.dialogs.components.location.CityComponent;
import com.noahhusby.sledgehammer.dialogs.components.location.CountryComponent;
import com.noahhusby.sledgehammer.dialogs.components.location.StateComponent;
import com.noahhusby.sledgehammer.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.dialogs.toolbars.ExitSkipToolbar;
import com.noahhusby.sledgehammer.dialogs.toolbars.IToolbar;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import net.md_5.bungee.api.config.ServerInfo;

public class CityScene extends DialogScene {

    private ServerInfo server;
    private DialogScene scene;

    public CityScene(ServerInfo server) {
        this(server, null);
    }

    public CityScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new CityComponent());
        registerComponent(new StateComponent());
        registerComponent(new CountryComponent());
    }

    @Override
    public void onFinish() {
        Location l = new Location(Location.detail.state, getValue("city"), getValue("county"), getValue("state"), getValue("country"));

        SledgehammerServer s = ServerConfig.getInstance().getServer(server.getName());

        if(s == null) s = new SledgehammerServer(server.getName());

        s.locations.add(l);
        ServerConfig.getInstance().pushServer(s);

        if(scene != null) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), scene);
        }
    }

    @Override
    public IToolbar getToolbar() {
        return new ExitSkipToolbar();
    }

    @Override
    public void onToolbarAction(String m) {
        if(m.equals("exit")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new LocationSelectionScene(server));
        } else if(m.equals("@")) {
            progressDialog("");
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
