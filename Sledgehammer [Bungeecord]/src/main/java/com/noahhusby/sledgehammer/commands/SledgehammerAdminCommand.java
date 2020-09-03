package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.commands.admin.PermissionListAdminCommand;
import com.noahhusby.sledgehammer.commands.admin.SetupAdminCommand;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.commands.data.IAdminCommand;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import com.sun.org.apache.regexp.internal.RE;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class SledgehammerAdminCommand extends Command {
    public SledgehammerAdminCommand() {
        super("sha", "sledgehammer.admin");

        registerAdminCommand(new PermissionListAdminCommand());
        registerAdminCommand(new SetupAdminCommand());
    }

    List<IAdminCommand> adminCommandList = new ArrayList<>();

    private void registerAdminCommand(IAdminCommand command) {
        this.adminCommandList.add(command);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!hasPermissionAdmin(sender)) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("You don't have permission to run this command!", ChatColor.RED)));
            return;
        }
        if(args.length == 0) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Sledgehammer Admin Commands:", ChatColor.GOLD)));
            for(IAdminCommand a : adminCommandList) {
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("/sha "+a.getName(), ChatColor.YELLOW),
                        new TextElement(" - ", ChatColor.GRAY), new TextElement(a.getPurpose(), ChatColor.RED)));
            }
        } else {
            ArrayList<String> dataList = new ArrayList<>();
            for(int x = 1; x < args.length; x++) dataList.add(args[x]);

            String[] data = dataList.toArray(new String[dataList.size()]);
            for(IAdminCommand a : adminCommandList) {
                if(a.getName().equals(args[0].toLowerCase())) {
                    a.execute(sender, data);
                }
            }
        }
    }
}
