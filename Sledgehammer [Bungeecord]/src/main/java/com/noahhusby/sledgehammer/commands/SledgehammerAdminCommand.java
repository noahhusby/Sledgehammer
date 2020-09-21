package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.commands.fragments.CommandFragmentManager;
import com.noahhusby.sledgehammer.commands.fragments.admin.*;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
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
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("You don't have permission to run this command!", ChatColor.DARK_RED)));
            return;
        }
        executeFragment(sender, args);
    }
}
