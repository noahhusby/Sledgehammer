package com.noahhusby.sledgehammer.commands.fragments;

import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandFragmentManager extends Command {

    public CommandFragmentManager(String name, String node) {
        super(name, node);
    }

    public CommandFragmentManager(String name, String node, String[] alias) {
        super(name, node, alias);
    }

    private List<ICommandFragment> commandFragments = new ArrayList<>();
    private String title = "";
    private String commandBase = "";

    protected void registerCommandFragment(ICommandFragment c) {
        commandFragments.add(c);
    }

    protected void setTitle(String t) {
        this.title = " "+t;
    }

    protected void setCommandBase(String b) {
        this.commandBase = "/"+b+" ";
    }

    protected void executeFragment(CommandSender sender, String[] args) {
        if (args.length != 0) {
            ArrayList<String> dataList = new ArrayList<>();
            for (int x = 1; x < args.length; x++) dataList.add(args[x]);

            String[] data = dataList.toArray(new String[dataList.size()]);
            for (ICommandFragment f : commandFragments) {
                if (f.getName().equals(args[0].toLowerCase())) {
                    f.execute(sender, data);
                    return;
                }
            }
        }
        displayCommands(sender, args);
    }

    private void displayCommands(CommandSender sender, String[] args) {
        sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement(title+":", ChatColor.GRAY)));
        for(ICommandFragment f : commandFragments) {
            String arguments;
            if(f.getArguments() == null) {
                arguments = "";
            } else {
                arguments = " "+f.getArguments();
            }
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement(commandBase+f.getName()
                            +arguments, ChatColor.YELLOW),
                    new TextElement(" - ", ChatColor.GRAY), new TextElement(f.getPurpose(), ChatColor.RED)));
        }
    }
}
