/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - TpllCommand.java
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
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.proxy.network.P2S.P2SLocationPacket;
import com.noahhusby.sledgehammer.proxy.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.proxy.permissions.PermissionRequest;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TpllCommand extends Command {

    public TpllCommand() {
        super("tpll", "sledgehammer.tpll");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatUtil.getNoPermission());
            return;
        }

        if (!isAllowed(sender)) {
            sender.sendMessage(ChatUtil.getNotAvailable());
            return;
        }


        PermissionHandler.getInstance().check(SledgehammerPlayer.getPlayer(sender), "sledgehammer.tpll", (code, global) -> {
            if (code == PermissionRequest.PermissionCode.PERMISSION) {
                if (args.length == 0) {
                    if (hasPerms(sender, "admin")) {
                        adminUsage(sender);
                    } else {
                        regularUsage(sender);
                    }
                    return;
                }

                SledgehammerPlayer recipient = SledgehammerPlayer.getPlayer(sender);
                if (args[0].equalsIgnoreCase("help")) {
                    TextComponent text = ChatUtil.title();

                    TextComponent interaction = new TextComponent(ChatColor.YELLOW + "Click here");
                    interaction.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://giant.gfycat.com/JitteryTerrificChimpanzee.webm"));
                    interaction.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("View the tpll guide").create()));

                    text.addExtra(interaction);
                    text.addExtra(new TextComponent(ChatColor.GRAY + " to see how to use " + ChatColor.BLUE + "/tpll"
                                                    + ChatColor.GRAY + "!"));

                    sender.sendMessage(text);
                    return;
                }
                String[] parseArgs = args;

                if (args.length == 3 && hasPerms(sender, "admin")) {
                    parseArgs = new String[]{ args[1], args[2] };
                    recipient = SledgehammerPlayer.getPlayer(args[0]);
                    if (recipient == null) {
                        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, args[0], " could not be found on the network!"));
                        return;
                    }
                }

                String[] splitCoords = parseArgs[0].split(",");
                if (splitCoords.length == 2 && parseArgs.length < 3) {
                    parseArgs = splitCoords;
                }
                if (parseArgs[0].endsWith(",")) {
                    parseArgs[0] = parseArgs[0].substring(0, parseArgs[0].length() - 1);
                }
                if (parseArgs.length > 1 && parseArgs[1].endsWith(",")) {
                    parseArgs[1] = parseArgs[1].substring(0, parseArgs[1].length() - 1);
                }
                if (parseArgs.length != 2 && parseArgs.length != 3) {
                    if (hasPerms(sender, "admin")) {
                        adminUsage(sender);
                    } else {
                        regularUsage(sender);
                    }
                    return;
                }

                double lon;
                double lat;

                try {
                    lat = Double.parseDouble(parseArgs[0]);
                    lon = Double.parseDouble(parseArgs[1]);
                } catch (Exception e) {
                    if (hasPerms(sender, "admin")) {
                        adminUsage(sender);
                    } else {
                        regularUsage(sender);
                    }
                    return;
                }

                ServerInfo server = OpenStreetMaps.getInstance().getServerFromLocation(lon, lat);

                if (server == null) {
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "That location could not be found, or is not available on this server!"));
                    return;
                }

                if (!hasPerms(sender, "admin") && !(hasPerms(sender, server.getName()) || hasPerms(sender, "all"))) {
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "You don't have permission to tpll to ", ChatColor.DARK_RED, sender.getName()));
                    return;
                }

                if (SledgehammerUtil.getServerFromSender(recipient) != server) {
                    if (!sender.getName().equals(recipient.getName())) {
                        recipient.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "You were summoned to ",
                                ChatColor.RED, server.getName(), ChatColor.GRAY, " by ", ChatColor.DARK_RED, sender.getName()));
                    } else {
                        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Sending you to ", ChatColor.RED, sender.getName()));
                    }
                    recipient.connect(server);
                }

                if (!recipient.equals(sender)) {
                    recipient.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Teleporting to ", ChatColor.RED, String.format("%s, %s", lat, lon)));
                } else {
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Teleporting to ", ChatColor.RED, String.format("%s, %s", lat, lon)));
                }
                double[] geo = { lat, lon };
                getNetworkManager().send(new P2SLocationPacket(recipient.getName(), server.getName(), geo));
                SledgehammerPlayer.getPlayer(sender).getAttributes().put("TPLL_FAILS", 0);
                return;
            }
            sender.sendMessage(ChatUtil.getNoPermission());
        });
    }

    private void adminUsage(CommandSender sender) {
        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "Usage: /tpll [target player] <lat> <lon>"));
        tpllHelp(sender);
    }

    private void regularUsage(CommandSender sender) {
        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "Usage: /tpll <lat> <lon>"));
        tpllHelp(sender);
    }

    private void tpllHelp(CommandSender sender) {
        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(sender);
        if (player != null) {
            if (player.getAttributes().containsKey("TPLL_FAILS")) {
                int x = ((Long) player.getAttributes().get("TPLL_FAILS")).intValue();
                if (x == 3) {
                    player.getAttributes().put("TPLL_FAILS", 0);

                    TextComponent text = new TextComponent(ChatColor.GRAY + "Having trouble? ");

                    TextComponent interaction = new TextComponent(ChatColor.YELLOW + "Click here");
                    interaction.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://giant.gfycat.com/JitteryTerrificChimpanzee.webm"));
                    interaction.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("View the tpll guide").create()));

                    text.addExtra(interaction);
                    text.addExtra(new TextComponent(ChatColor.GRAY + " to see how to use " + ChatColor.BLUE + "/tpll"
                                                    + ChatColor.GRAY + "!"));

                    sender.sendMessage(text);
                } else {
                    player.getAttributes().put("TPLL_FAILS", x + 1);
                }
            } else {
                player.getAttributes().put("TPLL_FAILS", 1);
            }
        }
    }
}
