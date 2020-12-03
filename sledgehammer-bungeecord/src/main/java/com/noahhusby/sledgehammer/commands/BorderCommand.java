/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - BorderCommands.java
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

package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.players.PlayerManager;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class BorderCommand extends Command {
    public BorderCommand() {
        super("border", "");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        SledgehammerPlayer p = PlayerManager.getInstance().getPlayer(sender);
        if(args.length == 0) {
            if(p.getAttributes().contains("NO_BORDER")) {
                sender.sendMessage(ChatHelper.makeTitleTextComponent(
                        new TextElement("Border teleportation is currently set to ", ChatColor.GRAY), new TextElement("off!", ChatColor.RED)));
                sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Use ", ChatColor.GRAY),
                        new TextElement("/border on", ChatColor.YELLOW), new TextElement(" to turn it on.", ChatColor.GRAY)));
                return;
            }

            sender.sendMessage(ChatHelper.makeTitleTextComponent(
                    new TextElement("Border teleportation is currently set to ", ChatColor.GRAY), new TextElement("on!", ChatColor.GREEN)));
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Use ", ChatColor.GRAY),
                    new TextElement("/border off", ChatColor.YELLOW), new TextElement(" to turn it off.", ChatColor.GRAY)));
            return;
        }

        String command = args[0];

        if(command.equalsIgnoreCase("on")) {
            sender.sendMessage(ChatHelper.makeTitleTextComponent(
                    new TextElement("Border teleportation has been set to ", ChatColor.GRAY), new TextElement("on!", ChatColor.GREEN)));
            p.getAttributes().remove("NO_BORDER");
            return;
        }

        if(command.equalsIgnoreCase("off")) {
            sender.sendMessage(ChatHelper.makeTitleTextComponent(
                    new TextElement("Border teleportation has been set to ", ChatColor.GRAY), new TextElement("off!", ChatColor.RED)));
            if(!p.getAttributes().contains("NO_BORDER")) p.getAttributes().add("NO_BORDER");
            return;
        }

        sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Usage: /border [on/off]", ChatColor.RED)));
    }
}
