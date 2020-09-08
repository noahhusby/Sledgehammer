package com.noahhusby.sledgehammer.data.dialogs;

import com.noahhusby.sledgehammer.data.dialogs.components.IDialogComponent;
import net.md_5.bungee.api.CommandSender;

public interface IDialogScene {
    void init(CommandSender commandSender);
    void onMessage(String m);
    void onFinish();
    IDialogComponent getCurrentComponent();
    CommandSender getCommandSender();
}
