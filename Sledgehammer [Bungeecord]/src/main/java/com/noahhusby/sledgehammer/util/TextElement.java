/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - TextElement.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.util;

import net.md_5.bungee.api.ChatColor;

public class TextElement {
    public final String text;
    public final ChatColor color;
    public final boolean bold;

    public TextElement(String text, ChatColor color) {
        this(text, color, false);
    }

    public TextElement(String text, ChatColor color, boolean bold) {
        this.text = text;
        this.color = color;
        this.bold = bold;
    }
}
