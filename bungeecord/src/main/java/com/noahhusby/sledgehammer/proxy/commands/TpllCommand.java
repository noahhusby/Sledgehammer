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
import com.noahhusby.sledgehammer.common.utils.LatLngHeight;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
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

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static com.noahhusby.sledgehammer.proxy.SledgehammerUtil.*;

public class TpllCommand extends Command {

    private final static String USAGE = "Usage: /tpll <lat> <lon> [alt]";
    private final static String ADMIN_USAGE = "Usage: /tpll [target player] <lat> <lon> [alt]";

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
            player.chat("/tpll " + getRawArguments(args));
            return;
        }
        CompletableFuture<Permission> permissionFuture = SledgehammerPlayer.getPlayer(sender).getPermission("sledgehammer.tpll");
        permissionFuture.thenAccept(permission -> {
            if (permission.isLocal()) {
                boolean admin = hasPerms(sender, "admin");
                if (args.length == 0) {
                    usage(sender, admin);
                    return;
                }

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

                LatLngHeight coordinates;
                boolean selector = false;

                try {
                    coordinates = parseCoordinates(args);
                } catch (InvalidCoordinatesException ignored) {
                    usage(sender, admin);
                    return;
                }

                try {
                    LatLngHeight temp = parseCoordinates(selectArray(args, 1));
                    selector = coordinates.equals(temp);
                } catch (InvalidCoordinatesException ignored) {
                }

                Collection<SledgehammerPlayer> targets = getMatchingEntities(sender.getName());
                if (selector && admin) {
                    targets = getMatchingEntities(args[0]);
                    if (targets.isEmpty()) {
                        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "No players found with \"", args[0], "\""));
                        return;
                    }
                }

                ServerInfo server = OpenStreetMaps.getInstance().getServerFromLocation(coordinates.getLat(), coordinates.getLon());
                if (server == null) {
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "That location could not be found, or is not available on this server!"));
                    return;
                }
                if (!hasPerms(sender, "admin") && !(hasPerms(sender, server.getName()) || hasPerms(sender, "all"))) {
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, "You don't have permission to tpll to ", ChatColor.DARK_RED, sender.getName()));
                    return;
                }
                // Send each target to destination
                for (SledgehammerPlayer target : targets) {
                    if (getServerFromSender(target) != server) {
                        if (!sender.getName().equals(target.getName())) {
                            target.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "You were summoned to ",
                                    ChatColor.RED, server.getName(), ChatColor.GRAY, " by ", ChatColor.DARK_RED, sender.getName()));
                        } else {
                            target.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Sending you to ", ChatColor.RED, sender.getName()));
                        }
                        target.connect(server);
                    }
                    target.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Teleporting to ", ChatColor.RED, String.format("%s, %s", coordinates.getLon(), coordinates.getLat())));
                    getNetworkManager().send(new P2SLocationPacket(target.getName(), server.getName(), coordinates));
                    return;
                }
                SledgehammerPlayer.getPlayer(sender).getAttributes().put("TPLL_FAILS", 0);
            }
            sender.sendMessage(ChatUtil.getNoPermission());
        });
    }

    private void usage(CommandSender sender, boolean admin) {
        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.RED, admin ? ADMIN_USAGE : USAGE));
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
