/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ChatHelper.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.chat;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatHelper {

    public static TextComponent makeTitleTextComponent(TextElement... text) {
        TextComponent bar = new TextComponent(ConfigHandler.messagePrefix.replace("&","\u00A7"));
        for(int x = 0; x < text.length; x++) {
            TextComponent temp = new TextComponent(text[x].text);
            temp.setColor(text[x].color);
            temp.setBold(text[0].bold);
            bar.addExtra(temp);
        }
        return bar;
    }

    public static TextComponent makeAdminTextComponent(TextElement... text) {
        TextComponent bar = new TextComponent(Constants.adminMessagePrefix.replace("&","\u00A7"));
        for(int x = 0; x < text.length; x++) {
            TextComponent temp = new TextComponent(text[x].text);
            temp.setColor(text[x].color);
            temp.setBold(text[0].bold);
            bar.addExtra(temp);
        }
        return bar;
    }

    public static TextComponent makeTitleMapComponent(TextElement text, String url) {
        TextComponent bar = new TextComponent(ConfigHandler.messagePrefix.replace("&","\u00A7"));
        TextComponent temp = new TextComponent(text.text);
        temp.setColor(text.color);
        temp.setBold(text.bold);
        bar.addExtra(temp);

        bar.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        bar.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Goto the warp map!").create()));
        return bar;
    }

    public static TextComponent makeTextComponent(TextElement... text) {
        TextComponent bar = new TextComponent();
        for(int x = 0; x < text.length; x++) {
            TextComponent temp = new TextComponent(text[x].text);
            temp.setColor(text[x].color);
            temp.setBold(text[0].bold);
            bar.addExtra(temp);
        }
        return bar;
    }

    public static void infoMessage(CommandSender sender) {
        sender.sendMessage(makeTextComponent(new TextElement("-----------------------", ChatColor.GRAY),
                new TextElement("\nSledgehammer ", ChatColor.BLUE), new TextElement("v."+ Constants.VERSION, ChatColor.RED),
                new TextElement("\nDeveloped by: ", ChatColor.BLUE), new TextElement("Noah Husby", ChatColor.RED),
                new TextElement("\n-----------------------",ChatColor.GRAY)));
    }

    public static void adminInfoMessage(CommandSender sender) {
        sender.sendMessage(makeTextComponent(
                new TextElement("Sledgehammer ", ChatColor.BLUE), new TextElement("v."+ Constants.VERSION, ChatColor.RED),
                new TextElement("\nDeveloped by: ", ChatColor.BLUE), new TextElement("Noah Husby", ChatColor.RED),
                new TextElement("\n", ChatColor.RESET), new TextElement("\nCommands: ", ChatColor.GRAY),
                new TextElement("\n/sha ", ChatColor.YELLOW), new TextElement("- Sledgehammer admin command", ChatColor.RED)));
    }

    public static String capitalize(final String str) {
        final int strLen = length(str);
        if (strLen == 0) {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toTitleCase(firstCodepoint);
        if (firstCodepoint == newCodePoint) {
            // already capitalized
            return str;
        }

        final int[] newCodePoints = new int[strLen]; // cannot be longer than the char array
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint; // copy the first codepoint
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint; // copy the remaining ones
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }
}
