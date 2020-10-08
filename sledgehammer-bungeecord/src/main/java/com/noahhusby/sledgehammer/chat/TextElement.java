/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - TextElement.java
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
 *
 */

package com.noahhusby.sledgehammer.chat;

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
