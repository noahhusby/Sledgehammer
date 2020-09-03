package com.noahhusby.sledgehammer.commands.data;

import com.noahhusby.sledgehammer.commands.admin.SetupAdminCommand;
import net.md_5.bungee.api.chat.TextComponent;

public class SetupField {
    public SetupAdminCommand.dialogAction dialogAction;
    public TextComponent title;
    public TextComponent menu;
    public String[] acceptable;
    public String[] deniable;

    public SetupField(SetupAdminCommand.dialogAction d, TextComponent title, TextComponent menu, String[] acceptable, String[] deniable) {
        this.dialogAction = d;
        this.title = title;
        this.menu = menu;
        this.acceptable = acceptable;
        this.deniable = deniable;
    }
}
