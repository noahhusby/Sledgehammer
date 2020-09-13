package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.commands.fragments.CommandFragmentManager;
import com.noahhusby.sledgehammer.commands.fragments.admin.PermissionListAdminCommand;
import com.noahhusby.sledgehammer.commands.fragments.admin.ServerAdminCommand;
import com.noahhusby.sledgehammer.commands.fragments.admin.SetupAdminCommand;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.commands.fragments.admin.TestLocationCommand;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SledgehammerAdminCommand extends CommandFragmentManager {
    public SledgehammerAdminCommand() {
        super("sha", "sledgehammer.admin");

        setCommandBase("sha");
        setTitle("Sledgehammer Admin Commands:");

        registerCommandFragment(new PermissionListAdminCommand());
        registerCommandFragment(new SetupAdminCommand());
        registerCommandFragment(new ServerAdminCommand());
        registerCommandFragment(new TestLocationCommand());
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
