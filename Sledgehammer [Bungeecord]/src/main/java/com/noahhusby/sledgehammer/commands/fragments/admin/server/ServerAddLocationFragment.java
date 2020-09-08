package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.commands.fragments.FragmentManager;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.data.dialogs.CountryScene;
import com.noahhusby.sledgehammer.data.dialogs.LocationSelectionScene;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerAddLocationFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("This command can only be executed by a player!", ChatColor.DARK_RED)));
            return;
        }
        DialogHandler.getInstance().startDialog(sender, new LocationSelectionScene(args[0]));
    }

    @Override
    public String getName() {
        return "addlocation";
    }

    @Override
    public String getPurpose() {
        return "Add a location to the server";
    }

    @Override
    public String getArguments() {
        return null;
    }
}
