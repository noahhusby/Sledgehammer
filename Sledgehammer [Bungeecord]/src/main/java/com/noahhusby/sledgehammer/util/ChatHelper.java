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
}
