/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - IDialogScene.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.dialogs.scenes;

import com.noahhusby.sledgehammer.dialogs.components.IDialogComponent;
import com.noahhusby.sledgehammer.dialogs.toolbars.IToolbar;
import com.noahhusby.sledgehammer.chat.TextElement;
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
