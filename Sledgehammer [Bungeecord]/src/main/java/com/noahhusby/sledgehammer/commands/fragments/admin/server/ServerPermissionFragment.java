package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerPermissionFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            sender.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("Usage: /sha server <server name> permission_type <global/local>", ChatColor.RED)));
        } else {
            String arg = args[2].toLowerCase();
            if(arg.equals("global") || arg.equals("local")) {
                Server s = ServerConfig.getInstance().getServer(args[0]);

                s.permission_type = arg;
                ServerConfig.getInstance().pushServer(s);

                sender.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("Set value", ChatColor.GRAY),
                        new TextElement(" permission_type ", ChatColor.GOLD), new TextElement("to ", ChatColor.GRAY), new TextElement(arg, ChatColor.RED),
                        new TextElement(" on ", ChatColor.GRAY), new TextElement(s.name, ChatColor.GOLD)));
            } else {
                sender.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("Usage: /sha server <server name> permission_type <global/local>", ChatColor.RED)));
            }
        }
    }

    @Override
    public String getName() {
        return "setpermission";
    }

    @Override
    public String getPurpose() {
        return "Set the permission type of the server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<global/local>"};
    }
}
