package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class SledgehammerCommand extends Command {
    public SledgehammerCommand() {
        super("sledgehammer", "sledgehammer.admin", new String[]{"sh"});
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(hasPermissionAdmin(sender)) {
            ChatHelper.getInstance().adminInfoMessage(sender);
        } else {
            ChatHelper.getInstance().infoMessage(sender);
        }
    }
}
