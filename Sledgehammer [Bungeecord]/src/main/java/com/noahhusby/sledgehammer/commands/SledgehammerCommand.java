package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.util.ChatHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class SledgehammerCommand extends Command {
    public SledgehammerCommand() {
        super("sledgehammer");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ChatHelper.getInstance().infoMessage(commandSender);
    }
}
