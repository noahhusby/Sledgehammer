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

package com.noahhusby.sledgehammer.proxy;

import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;

@UtilityClass
public class ChatUtil {

    public static final TextComponent notSledgehammerServer = adminAndCombine(ChatColor.GRAY, "This server is not configured as a sledgehammer server! Please use",
            ChatColor.BLUE, " /sha server <server name> setsledgehammer true ", ChatColor.GRAY, "to enable it.");
    public static final TextComponent notEarthServer = adminAndCombine(ChatColor.GRAY, "The server is not configured as an earth server! Please use",
            ChatColor.BLUE, " /sha server <server name> setearth true ", ChatColor.GRAY, "to enable it.");

    public static TextComponent title() {
        return new TextComponent(SledgehammerConfig.general.messagePrefix.replace("&", "\u00A7"));
    }

    public static TextComponent adminTitle() {
        return new TextComponent(Constants.adminMessagePrefix.replace("&", "\u00A7"));
    }

    public static TextComponent titleAndCombine(Object... objects) {
        return combine(title(), objects);
    }

    public static TextComponent adminAndCombine(Object... objects) {
        return combine(adminTitle(), objects);
    }

    public static TextComponent combine(Object... objects) {
        return combine(null, objects);
    }

    public static TextComponent combine(TextComponent title, Object... objects) {
        TextComponent textComponent = title == null ? new TextComponent() : title;
        StringBuilder builder = null;
        ChatColor lastFormat = null;
        for (Object o : objects) {
            if (o instanceof TextComponent) {
                if (builder != null) {
                    textComponent.addExtra(new TextComponent(builder.toString()));
                    builder = null;
                }

                TextComponent component = (TextComponent) o;
                if (component.getColor() == null && lastFormat != null) {
                    component.setColor(lastFormat);
                }

                textComponent.addExtra(component);
            } else {
                if (o instanceof ChatColor) {
                    lastFormat = (ChatColor) o;
                }
                if (builder == null) {
                    builder = new StringBuilder();
                }
                builder.append(o);
            }
        }

        if (builder != null) {
            textComponent.addExtra(new TextComponent(builder.toString()));
        }
        return textComponent;
    }

    public static TextComponent getNoPermission() {
        return combine(ChatColor.RED, "You do not have permission to use this command");
    }

    public static TextComponent getPlayerOnly() {
        return titleAndCombine(ChatColor.RED, "This command can only be executed by players!");
    }

    public static TextComponent getVersionMessage() {
        return adminAndCombine(ChatColor.RED, String.format("Sledgehammer %s", Constants.VERSION),
                ChatColor.GRAY, " by ", ChatColor.BLUE + "Noah Husby");
    }

    public static TextComponent getNotProjection() {
        return titleAndCombine(ChatColor.RED, "That is not a valid location in the projection!");
    }

    public static void sendAuthCodeWarning(CommandSender sender) {
        sendMessageBox(sender, ChatColor.DARK_RED + "Warning", combine(ChatColor.RED, "The sledgehammer authentication code is not properly configured. " +
                                                                                      "Please check the console for more details!\n", ChatColor.GRAY, "Most sledgehammer features will be disabled"));
    }

    public static TextComponent getValueMessage(String key, String value, String where) {
        return adminAndCombine(ChatColor.GRAY, "set value ", ChatColor.GOLD, key, ChatColor.GRAY,
                " to ", ChatColor.RED, value, ChatColor.GRAY, " on ", ChatColor.GOLD, where);
    }

    public static TextComponent getNotAvailable() {
        return combine(SledgehammerConfig.general.replaceNotAvailable ? (ChatColor.WHITE + "Unknown command. Type \"/help\" for help.") :
                (ChatColor.RED + "That command is not available."));
    }

    public static void sendMessageBox(CommandSender sender, String title, TextComponent text) {
        sendMessageBox(sender, title, () -> sender.sendMessage(text));
    }

    public static void sendMessageBox(CommandSender sender, String title, Runnable runnable) {
        sender.sendMessage(combine(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH, "==============", ChatColor.RESET, " " + ChatColor.BOLD + title + " ", ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH, "=============="));
        sender.sendMessage();

        runnable.run();

        int length = ChatColor.stripColor(title).length();
        char[] array = new char[length];
        Arrays.fill(array, '=');
        String bottom = "==============================" + new String(array);
        sender.sendMessage();
        sender.sendMessage(combine(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH, bottom));
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
