/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationRemovalScene.java
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
import com.noahhusby.sledgehammer.proxy.config.ServerHandler;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerServer;
import com.noahhusby.sledgehammer.proxy.datasets.Location;
import com.noahhusby.sledgehammer.proxy.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.proxy.dialogs.components.location.LocationRemovalComponent;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.DialogScene;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;

public class LocationRemovalScene extends DialogScene {

    private final ServerInfo server;
    private final DialogScene scene;

    public LocationRemovalScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new LocationRemovalComponent(server));
    }

    @Override
    public TextComponent getTitle() {
        return ChatUtil.combine(ChatColor.GRAY, "Editing Locations - ", ChatColor.RED, server.getName());
    }

    @Override
    public void onFinish() {
        List<Location> locations = ServerHandler.getInstance().getLocationsFromServer(server.getName());
        List<Location> newLocations = ServerHandler.getInstance().getLocationsFromServer(server.getName());
        Location l = locations.get(Integer.parseInt(getValue("locationremove").trim()));
        newLocations.remove(l);

        SledgehammerServer s = ServerHandler.getInstance().getServer(server.getName());
        s.setLocations(newLocations);
        ServerHandler.getInstance().getServers().put(server.getName(), s);
        ServerHandler.getInstance().getServers().saveAsync();

        if (scene != null) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), scene);
            return;
        }

        String x = "";
        if (!l.city.equals("")) {
            x += ChatUtil.capitalize(l.city) + ", ";
        }
        if (!l.county.equals("")) {
            x += ChatUtil.capitalize(l.county) + ", ";
        }
        if (!l.state.equals("")) {
            x += ChatUtil.capitalize(l.state) + ", ";
        }
        if (!l.country.equals("")) {
            x += ChatUtil.capitalize(l.country);
        }
        sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Successfully removed location :",
                ChatColor.RED, ChatUtil.capitalize(l.detailType.name()) + " - ", ChatColor.GOLD, x));
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
