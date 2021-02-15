/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - IDialogScene.java
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

package com.noahhusby.sledgehammer.dialogs.scenes;

import com.noahhusby.sledgehammer.dialogs.components.IDialogComponent;
import com.noahhusby.sledgehammer.dialogs.toolbars.IToolbar;
import net.md_5.bungee.api.CommandSender;

public interface IDialogScene {
    void init(CommandSender commandSender);
    void onMessage(String m);
    void onFinish();
    void onToolbarAction(String m);
    void onComponentFinish();
    void onInitialization();
    boolean isAdmin();
    TextElement[] getTitle();
    IToolbar getToolbar();
    IDialogComponent getCurrentComponent();
    CommandSender getCommandSender();
}
