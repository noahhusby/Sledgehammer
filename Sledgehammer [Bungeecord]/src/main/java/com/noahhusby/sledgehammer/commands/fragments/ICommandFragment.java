package com.noahhusby.sledgehammer.commands.fragments;

import net.md_5.bungee.api.CommandSender;

public interface ICommandFragment {
    void execute(CommandSender sender, String[] args);

    String getName();

    String getPurpose();

    String[] getArguments();
}
