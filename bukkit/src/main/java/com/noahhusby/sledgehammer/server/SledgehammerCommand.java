/*
 *  Copyright (c) 2021 Noah Husby
 *  Sledgehammer - SledgehammerCommand.java
 *
 *  Sledgehammer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Sledgehammer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.server;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Noah Husby
 */
public class SledgehammerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.spigot().sendMessage(new TextComponent(ChatColor.BLUE + "" + ChatColor.BOLD + "SH " + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "> " + ChatColor.RESET + "" + ChatColor.RED + "Sledgehammer " + Constants.VERSION + " " + ChatColor.GRAY + "by " + ChatColor.BLUE + "Noah Husby"));
        return true;
    }
}
