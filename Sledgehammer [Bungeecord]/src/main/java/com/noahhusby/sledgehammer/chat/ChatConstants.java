/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ChatConstants.java
 * All rights reserved.
 */
package com.noahhusby.sledgehammer.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatConstants {
    public static final String logInitPacket = "Received initialization packet from ";

    public static final TextComponent noPermission = ChatHelper.makeTextComponent(new TextElement("You don't have permission to run this command!", ChatColor.DARK_RED));
    public static final TextComponent issueByPlayer = ChatHelper.makeTextComponent(new TextElement("This command can only be executed by a player!", ChatColor.DARK_RED));

    public static TextComponent getValueMessage(String key, String value, String where) {
        return ChatHelper.makeAdminTextComponent(new TextElement("Set value", ChatColor.GRAY),
                new TextElement(" " + key + " ", ChatColor.GOLD), new TextElement("to ", ChatColor.GRAY), new TextElement(value, ChatColor.RED),
                new TextElement(" on ", ChatColor.GRAY), new TextElement(where, ChatColor.GOLD));
    }
}
