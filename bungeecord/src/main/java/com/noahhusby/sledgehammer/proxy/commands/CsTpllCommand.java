/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CsTpllCommand.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.proxy.commands;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class CsTpllCommand extends Command {

    public CsTpllCommand() {
        super("cs", "");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatUtil.getPlayerOnly());
            return;
        }

        if (args.length == 0) {
            ((ProxiedPlayer) sender).chat("/cs");
            return;
        } else if (!args[0].equals("tpll")) {
            ((ProxiedPlayer) sender).chat("/cs " + SledgehammerUtil.getRawArguments(args));
            return;
        }

        ArrayList<String> dataList = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
        String[] data = dataList.toArray(new String[0]);

        new TpllCommand().execute(sender, data);
    }
}
