package com.noahhusby.sledgehammer.commands.data;

import net.md_5.bungee.api.CommandSender;

public interface IAdminCommand {
    void execute(CommandSender sender, String[] args);

    String getName();

    String getPurpose();
}
