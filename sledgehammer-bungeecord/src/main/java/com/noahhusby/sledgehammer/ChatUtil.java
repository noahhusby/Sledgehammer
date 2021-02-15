package com.noahhusby.sledgehammer;

import com.noahhusby.sledgehammer.config.ConfigHandler;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

@UtilityClass
public class ChatUtil {

    public static final TextComponent notSledgehammerServer = adminAndCombine(ChatColor.GRAY, "This server is not configured as a sledgehammer server! Please use",
            ChatColor.BLUE, " /sha server <server name> setsledgehammer true ", ChatColor.GRAY, "to enable it.");
    public static final TextComponent notEarthServer = adminAndCombine(ChatColor.GRAY, "The server is not configured as an earth server! Please use",
            ChatColor.BLUE, " /sha server <server name> setearth true ", ChatColor.GRAY, "to enable it.");

    public static TextComponent title() {
        return new TextComponent(ConfigHandler.messagePrefix.replace("&","\u00A7"));
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
        for(Object o : objects) {
            if(o instanceof TextComponent) {
                if(builder != null) {
                    textComponent.addExtra(new TextComponent(builder.toString()));
                    builder = null;
                }

                TextComponent component = (TextComponent) o;
                if(component.getColor() == null && lastFormat != null)
                    component.setColor(lastFormat);

                textComponent.addExtra(component);
            } else {
                if(o instanceof ChatColor)
                    lastFormat = (ChatColor) o;
                if(builder == null) builder = new StringBuilder();
                builder.append(o);
            }
        }

        if(builder != null)
            textComponent.addExtra(new TextComponent(builder.toString()));
        return textComponent;
    }

    public static TextComponent getNoPermission() {
        return combine(ChatColor.RED, "You do not have permission to use this command");
    }

    public static TextComponent getPlayerOnly() {
        return titleAndCombine(ChatColor.RED, "This command can only be executed by players!");
    }

    public static TextComponent getVersionMessage() {
        return titleAndCombine(ChatColor.RED, String.format("Sledgehammer v%s", Constants.VERSION),
                ChatColor.GRAY, " by ", ChatColor.BLUE + "Noah Husby");
    }

    public static TextComponent getNotProjection() {
        return titleAndCombine(ChatColor.RED, "That is not a valid location in the projection!");
    }

    public static void sendAuthCodeWarning(CommandSender sender) {
        sender.sendMessage(combine(ChatColor.DARK_RED, "----------------------------------------------"));
        sender.sendMessage();
        sender.sendMessage(combine(ChatColor.RED," The sledgehammer authentication code is not properly configured. " +
                "Please check the console for more details!"));
        sender.sendMessage(combine(ChatColor.GRAY, "Most sledgehammer features will be disabled"));
        sender.sendMessage();
        sender.sendMessage(combine(ChatColor.DARK_RED, "----------------------------------------------"));
    }

    public static TextComponent getValueMessage(String key, String value, String where) {
        return adminAndCombine(ChatColor.GRAY, "set value ", ChatColor.GOLD, key, ChatColor.GRAY,
                " to ", ChatColor.RED, value, ChatColor.GRAY, " on ", ChatColor.GOLD, where);
    }

    public static TextComponent getNotAvailable() {
        return combine(ConfigHandler.replaceNotAvailable ? (ChatColor.WHITE + "Unknown command. Type \"/help\" for help.") :
                (ChatColor.RED + "That command is not available."));
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
