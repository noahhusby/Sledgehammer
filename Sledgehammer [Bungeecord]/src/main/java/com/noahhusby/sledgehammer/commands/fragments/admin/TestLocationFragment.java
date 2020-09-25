package com.noahhusby.sledgehammer.commands.fragments.admin;

import com.noahhusby.sledgehammer.SledgehammerUtils;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.TestLocationTask;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class TestLocationFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        TransferPacket t = new TransferPacket(SledgehammerUtils.getServerFromPlayerName(sender.getName()), sender.getName());
        if(args.length == 0) {
            TaskHandler.getInstance().execute(new TestLocationTask(t, -1));
        } else {
            try {
                int zoom = Integer.parseInt(args[0]);

                if(zoom < 1 || zoom > 19) {
                    throw new Exception();
                }

                TaskHandler.getInstance().execute(new TestLocationTask(t, zoom));
            } catch (Exception e) {
                sender.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("Invalid zoom level!", ChatColor.RED),
                        new TextElement(" Please enter a value between ", ChatColor.GRAY),
                        new TextElement("1", ChatColor.BLUE), new TextElement(" and ", ChatColor.GRAY), new TextElement("19", ChatColor.BLUE)));
            }
        }
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
    public String[] getArguments() {
        return new String[]{"[zoom]"};
    }
}
