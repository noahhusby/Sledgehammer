package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.handlers.CommunicationHandler;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CsTpllCommand extends Command {

    public CsTpllCommand() {
        super("cs");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("This command can only be executed by a player.", ChatColor.RED)));
        }

        if (args.length == 0) {
            CommunicationHandler.executeCommand(ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo(), "command",
                    sender.getName(), "cs");
            return;
        } else if (!args[0].equals("tpll")) {
            CommunicationHandler.executeCommand(ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo(), "command",
                    sender.getName(), "cs", CommunicationHandler.getRawArguments(args));
            return;
        } else if (args.length == 1) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: /cs tpll <lat> <lon>", ChatColor.RED)));
            return;
        }

        String[] splitCoords = args[1].split(",");
        String alt = null;
        if (splitCoords.length == 2 && args.length < 3) { // lat and long in single arg
            if (args.length > 2) alt = args[2];
            args = splitCoords;
        } else if (args.length == 4) {
            alt = args[3];
        }
        if (args[1].endsWith(","))
            args[1] = args[1].substring(0, args[1].length() - 1);
        if (args.length > 2 && args[2].endsWith(","))
            args[2] = args[2].substring(0, args[2].length() - 1);
        if (args.length != 3 && args.length != 4) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: /cs tpll <lat> <lon>", ChatColor.RED)));
            return;
        }

        double lon;
        double lat;

        try {
            lat = Double.parseDouble(args[1]);
            lon = Double.parseDouble(args[2]);
        } catch (Exception e) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: /cs tpll <lat> <lon>", ChatColor.RED)));
            return;
        }

        ServerInfo server = OpenStreetMaps.getInstance().getServerFromLocation(lon, lat);

        if (server == null) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("That location could not be found, or is not available on this server!", ChatColor.RED)));
            return;
        }

        if (ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo() != server) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Sending you to: ", ChatColor.GOLD), new TextElement(server.getName(), ChatColor.RED)));
            ProxyServer.getInstance().getPlayer(sender.getName()).connect(server);
        }
        CommunicationHandler.executeCommand(server, "location", sender.getName(), String.valueOf(lat), String.valueOf(lon));
    }
}
