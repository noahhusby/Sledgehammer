/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
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

import com.noahhusby.sledgehammer.common.TpllMode;
import com.noahhusby.sledgehammer.common.exceptions.InvalidCoordinatesException;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.proxy.network.p2s.P2SLocationPacket;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.CompletableFuture;

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
        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(sender);
        if (player.getSledgehammerServer() != null && player.getSledgehammerServer().getTpllMode() == TpllMode.PASSTHROUGH) {
            player.chat("/tpll " + SledgehammerUtil.getRawArguments(args));
            return;
        }
        CompletableFuture<Permission> permissionFuture = SledgehammerPlayer.getPlayer(sender).getPermission("sledgehammer.tpll");
        permissionFuture.thenAccept(permission -> {
            if (permission.isLocal()) {
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
                    interaction.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("View the tpll guide")));

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

                try {
                    double[] coordinates = SledgehammerUtil.parseCoordinates(parseArgs);
                    ServerInfo server = OpenStreetMaps.getInstance().getServerFromLocation(coordinates[1], coordinates[0]);
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
                        recipient.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Teleporting to ", ChatColor.RED, String.format("%s, %s", coordinates[0], coordinates[1])));
                    } else {
                        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Teleporting to ", ChatColor.RED, String.format("%s, %s", coordinates[0], coordinates[1])));
                    }
                    getNetworkManager().send(new P2SLocationPacket(recipient.getName(), server.getName(), coordinates));
                    SledgehammerPlayer.getPlayer(sender).getAttributes().put("TPLL_FAILS", 0);
                } catch (InvalidCoordinatesException e) {
                    if (hasPerms(sender, "admin")) {
                        adminUsage(sender);
                    } else {
                        regularUsage(sender);
                    }
                }
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
                int x = ((Number) player.getAttributes().get("TPLL_FAILS")).intValue();
                if (x == 3) {
                    player.getAttributes().put("TPLL_FAILS", 0);

                    TextComponent text = new TextComponent(ChatColor.GRAY + "Having trouble? ");

                    TextComponent interaction = new TextComponent(ChatColor.YELLOW + "Click here");
                    interaction.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://giant.gfycat.com/JitteryTerrificChimpanzee.webm"));
                    interaction.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("View the tpll guide")));

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
