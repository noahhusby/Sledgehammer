/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ReloadFragment.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.commands.fragments.admin;

import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ReloadFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        ConfigHandler.getInstance().reload();
        sender.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("Reloaded the config", ChatColor.BLUE)));
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPurpose() {
        return "Reloads the config";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
