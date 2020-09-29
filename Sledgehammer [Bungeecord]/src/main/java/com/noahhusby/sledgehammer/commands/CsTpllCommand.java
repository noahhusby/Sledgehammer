/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CsTpllCommand.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.network.P2S.P2SCommandPacket;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class CsTpllCommand extends Command {

    public CsTpllCommand() {
        super("cs", "");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatConstants.issueByPlayer);
        }

        if (args.length == 0) {
            getNetworkManager().sendPacket(new P2SCommandPacket(sender.getName(), SledgehammerUtil.getServerNameByPlayer(sender), "cs"));
            return;
        } else if (!args[0].equals("tpll")) {

            getNetworkManager().sendPacket(new P2SCommandPacket(sender.getName(), SledgehammerUtil.getServerNameByPlayer(sender), "cs",
                    SledgehammerUtil.getRawArguments(args)));
            return;
        }

        ArrayList<String> dataList = new ArrayList<>();
        for(int x = 1; x < args.length; x++) dataList.add(args[x]);

        String[] data = dataList.toArray(new String[dataList.size()]);

        new TpllCommand().execute(sender, data);
    }
}
