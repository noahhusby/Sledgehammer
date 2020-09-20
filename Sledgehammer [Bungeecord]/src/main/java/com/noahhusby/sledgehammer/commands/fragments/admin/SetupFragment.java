package com.noahhusby.sledgehammer.commands.fragments.admin;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.commands.data.SetupAdminTracker;
import com.noahhusby.sledgehammer.commands.data.SetupField;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.data.dialogs.scenes.setup.ConfigScene;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetupFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        DialogHandler.getInstance().startDialog(sender, new ConfigScene(null));
    }

    @Override
    public String getName() {
        return "setup";
    }

    @Override
    public String getPurpose() {
        return "Run the automatic setup prompt";
    }

    @Override
    public String getArguments() {
        return "";
    }

}
