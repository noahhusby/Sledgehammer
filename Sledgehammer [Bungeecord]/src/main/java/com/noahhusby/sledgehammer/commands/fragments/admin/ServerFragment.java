package com.noahhusby.sledgehammer.commands.fragments.admin;

import com.noahhusby.sledgehammer.commands.fragments.FragmentManager;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.commands.fragments.admin.server.*;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Map;

import static net.md_5.bungee.api.ProxyServer.getInstance;

public class ServerFragment extends FragmentManager implements ICommandFragment {

    public ServerFragment() {
        setCommandBase("sha server <server name>");
        setTitle("Sledgehammer Server Commands");
        registerCommandFragment(new ServerInfoFragment());
        registerCommandFragment(new ServerAddLocationFragment());
        registerCommandFragment(new ServerRemoveLocationFragment());
        registerCommandFragment(new ServerListLocationFragment());
        registerCommandFragment(new ServerEarthModeFragment());
        registerCommandFragment(new ServerPermissionFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length != 0) {
            for(Map.Entry<String, ServerInfo> s : getInstance().getServers().entrySet()) {
                if(s.getValue().getName().toLowerCase().equals(args[0].toLowerCase())) {
                    executeFragment(sender, args, 1);
                    return;
                }
            }
            sender.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(
                    new TextElement(args[0], ChatColor.DARK_RED), new TextElement(" is not a bungeecord server!", ChatColor.RED)));
            return;
        }
        executeFragment(sender, args, 1);
    }

    @Override
    public String getName() {
        return "server";
    }

    @Override
    public String getPurpose() {
        return "Configure settings of sledgehammer server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<server name>"};
    }
}
