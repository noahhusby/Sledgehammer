package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.maps.MapHandler;
import net.md_5.bungee.api.CommandSender;

public class WarpTemp extends Command {
    public WarpTemp() {
        super("warpmap", "");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        MapHandler.getInstance().newMapCommand(commandSender);
    }
}
