/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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

package com.noahhusby.sledgehammer.proxy.terramap;

import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;

/**
 * Manages player display preferences
 *
 * @author SmylerMC
 */
public final class PlayerDisplayPreferences {

    private final static String SHOW_ATTRIBUTE = "TerraShow";
    private final static String HIDE_ATTRIBUTE = "TerraHide";

    /**
     * Indicates if the player should be visible on the map to others
     *
     * @param player - the player
     * @return a boolean
     */
    public static boolean shouldDisplayPlayer(SledgehammerPlayer player) {
        return !player.checkAttribute("TERRA_HIDE", false);
    }

    /**
     * Set whether or not a player should be visible on the map to others
     *
     * @param player - the player
     * @param yesNo
     */
    public static void setShouldDisplayPlayer(SledgehammerPlayer player, boolean yesNo) {
        player.getAttributes().put("TERRA_HIDE", !yesNo);
    }

}