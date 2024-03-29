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
