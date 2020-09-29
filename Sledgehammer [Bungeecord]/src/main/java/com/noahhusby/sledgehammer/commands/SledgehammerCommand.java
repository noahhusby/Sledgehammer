/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerCommand.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import net.md_5.bungee.api.CommandSender;

public class SledgehammerCommand extends Command {
    public SledgehammerCommand() {
        super("sledgehammer", "sledgehammer.admin", new String[]{"sh"});
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(hasPermissionAdmin(sender)) {
            ChatHelper.adminInfoMessage(sender);
        } else {
            ChatHelper.infoMessage(sender);
        }
    }
}
