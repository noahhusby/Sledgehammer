/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SetupFragment.java
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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin;

import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.setup.ConfigScene;
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
