package com.noahhusby.sledgehammer.util;

import com.noahhusby.sledgehammer.Sledgehammer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatHelper {

    private static ChatHelper mInstance = null;

    public static ChatHelper getInstance() {
        if(mInstance == null) mInstance = new ChatHelper();
        return mInstance;
    }

    public TextComponent makeTitleTextComponent(TextElement... text) {
        TextComponent bar = new TextComponent(Sledgehammer.configuration.getString("message-prefix"));
        bar.setColor(ChatColor.GOLD);
        bar.setBold(true);
        for(int x = 0; x < text.length; x++) {
            TextComponent temp = new TextComponent(text[x].text);
            temp.setColor(text[x].color);
            temp.setBold(text[0].bold);
            bar.addExtra(temp);
        }
        return bar;
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
