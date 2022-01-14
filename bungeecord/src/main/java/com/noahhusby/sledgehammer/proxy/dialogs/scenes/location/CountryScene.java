/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
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
