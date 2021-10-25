/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerCommand.java
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
import net.md_5.bungee.api.CommandSender;

public class SledgehammerCommand extends Command {
    public SledgehammerCommand() {
        super("sledgehammer", "sledgehammer.admin", new String[]{ "sh" });
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatUtil.getVersionMessage());
    }
}
