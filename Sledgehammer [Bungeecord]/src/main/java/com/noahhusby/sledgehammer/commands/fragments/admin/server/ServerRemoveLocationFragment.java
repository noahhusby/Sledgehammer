package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.data.dialogs.scenes.location.LocationSelectionScene;
import com.noahhusby.sledgehammer.data.dialogs.scenes.setup.LocationRemovalScene;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerRemoveLocationFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("This command can only be executed by a player!", ChatColor.DARK_RED)));
            return;
        }
        DialogHandler.getInstance().startDialog(sender, new LocationRemovalScene(ProxyServer.getInstance().getServerInfo(args[0]), null));
    }

    @Override
    public String getName() {
        return "removelocation";
    }

    @Override
    public String getPurpose() {
        return "Remove a location on the server";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
