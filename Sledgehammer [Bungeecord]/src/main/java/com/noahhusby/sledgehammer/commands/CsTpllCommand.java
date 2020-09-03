package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.handlers.CommunicationHandler;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.CommandTask;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.ProxyUtil;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CsTpllCommand extends Command {

    public CsTpllCommand() {
        super("cs", "");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("This command can only be executed by a player.", ChatColor.RED)));
        }

        if (args.length == 0) {
            TransferPacket t = new TransferPacket(ProxyUtil.getServerFromPlayerName(sender.getName()), sender.getName());
            TaskHandler.getInstance().execute(new CommandTask(t, "cs"));
            return;
        } else if (!args[0].equals("tpll")) {
            TransferPacket t = new TransferPacket(ProxyUtil.getServerFromPlayerName(sender.getName()), sender.getName());
            TaskHandler.getInstance().execute(new CommandTask(t, "cs", TaskHandler.getRawArguments(args)));
            return;
        }
        ArrayList<String> dataList = new ArrayList<>();
        for(int x = 1; x < args.length; x++) dataList.add(args[x]);

        String[] data = dataList.toArray(new String[dataList.size()]);

        new TpllCommand().execute(sender, data);
    }
}
