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
