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

package com.noahhusby.sledgehammer.proxy.commands;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.FragmentManager;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.MigrateFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.ReloadFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.ServerFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.SetupFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.TestLocationFragment;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class SledgehammerAdminCommand extends Command {

    private final FragmentManager manager = new FragmentManager("/sha", ChatColor.BLUE + "Sledgehammer");

    public SledgehammerAdminCommand() {
        super("sha", "sledgehammer.admin");
        manager.register(new ReloadFragment());
        manager.register(new SetupFragment());
        manager.register(new ServerFragment());
        manager.register(new TestLocationFragment());
        manager.register(new MigrateFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPerms(sender)) {
            sender.sendMessage(ChatUtil.getNotAvailable());
            return;
        }
        manager.execute(sender, args);
    }
}
