/*
 *  Copyright (c) 2021 Noah Husby
 *  Sledgehammer - LocationSceneUtil.java
 *
 *  Sledgehammer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Sledgehammer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.proxy.dialogs.scenes.location;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.datasets.Location;
import com.noahhusby.sledgehammer.proxy.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.IDialogScene;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * @author Noah Husby
 */
@UtilityClass
public class LocationSceneUtil {
    public static void completeScene(IDialogScene scene, ServerInfo server, Location location, DialogScene dialogScene) {
        SledgehammerServer s = ServerHandler.getInstance().getServers().containsKey(server.getName()) ? ServerHandler.getInstance().getServer(server.getName()) : new SledgehammerServer(server.getName());
        s.getLocations().add(location);
        ServerHandler.getInstance().getServers().put(server.getName(), s);
        ServerHandler.getInstance().getServers().saveAsync();
        if (dialogScene != null) {
            DialogHandler.getInstance().discardDialog(scene);
            DialogHandler.getInstance().startDialog(scene.getCommandSender(), dialogScene);
            return;
        }
        scene.getCommandSender().sendMessage(ChatUtil.combine(ChatColor.GRAY, "Successfully added ", ChatColor.RED, ChatUtil.capitalize(location.detailType.name()) + " - ", ChatColor.GOLD, location));
    }
}
