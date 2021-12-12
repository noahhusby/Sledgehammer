/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class MigrateFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Type ", ChatColor.YELLOW, "/sha migrate confirm ", ChatColor.GRAY, "to migrate local storage."));
            return;
        }

        if (args[0].equalsIgnoreCase("confirm")) {
            ConfigHandler.getInstance().migrate();
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.BLUE, "Successfully migrated data!"));
        } else {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Type ", ChatColor.YELLOW, "/sha migrate confirm ", ChatColor.GRAY, "to migrate local storage."));
        }
    }

    @Override
    public String getName() {
        return "migrate";
    }

    @Override
    public String getPurpose() {
        return "Migrate local storage to remote databases";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
