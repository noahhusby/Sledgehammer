/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerAdminCommand.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.fragments.CommandFragmentManager;
import com.noahhusby.sledgehammer.commands.fragments.admin.*;
import net.md_5.bungee.api.CommandSender;

public class SledgehammerAdminCommand extends CommandFragmentManager {
    public SledgehammerAdminCommand() {
        super("sha", "sledgehammer.admin");

        setCommandBase("sha");
        setTitle("Sledgehammer Admin Commands:");

        registerCommandFragment(new ReloadFragment());
        registerCommandFragment(new SetupFragment());
        registerCommandFragment(new ServerFragment());
        registerCommandFragment(new PermissionCheckFragment());
        registerCommandFragment(new TestLocationFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!hasPermissionAdmin(sender)) {
            sender.sendMessage(ChatConstants.noPermission);
            return;
        }
        executeFragment(sender, args);
    }
}
