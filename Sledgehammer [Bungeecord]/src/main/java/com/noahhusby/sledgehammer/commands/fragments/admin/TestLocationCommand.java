package com.noahhusby.sledgehammer.commands.fragments.admin;

import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.TestLocationTask;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.ProxyUtil;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class TestLocationCommand implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        TransferPacket t = new TransferPacket(ProxyUtil.getServerFromPlayerName(sender.getName()), sender.getName());
        if(ServerConfig.getInstance().getLocationsFromServer(t.server.getName()) == null) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Testing is only available on configured sledgehammer servers!", ChatColor.GRAY)));
            return;
        }
        TaskHandler.getInstance().execute(new TestLocationTask(t));
    }

    @Override
    public String getName() {
        return "testlocation";
    }

    @Override
    public String getPurpose() {
        return "See what the API sees at the current location";
    }

    @Override
    public String getArguments() {
        return null;
    }
}
