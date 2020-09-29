/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SetupFragment.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.commands.fragments.admin;

import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.dialogs.scenes.setup.ConfigScene;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import net.md_5.bungee.api.CommandSender;

public class SetupFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        DialogHandler.getInstance().startDialog(sender, new ConfigScene(null));
    }

    @Override
    public String getName() {
        return "setup";
    }

    @Override
    public String getPurpose() {
        return "Run the automatic setup prompt";
    }

    @Override
    public String[] getArguments() {
        return null;
    }

}
