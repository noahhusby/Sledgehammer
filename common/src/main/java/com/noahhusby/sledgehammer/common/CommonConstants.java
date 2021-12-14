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

package com.noahhusby.sledgehammer.common;

import com.noahhusby.sledgehammer.common.exceptions.VersionParseException;

/**
 * @author Noah Husby
 */
public abstract class CommonConstants {
    public static final SledgehammerVersion VERSION;

    static {
        SledgehammerVersion tempVersion;
        try {
            tempVersion = new SledgehammerVersion(CommonConstants.class.getPackage().getImplementationVersion());
        } catch (VersionParseException ignored) {
            tempVersion = new SledgehammerVersion(0, 0, 0, true);
        }
        VERSION = tempVersion;
    }

    public static final double SCALE = 7318261.522857145;

    public static final String serverChannel = "sledgehammer:server";

    public static final String teleportID = "teleport";
    public static final String setwarpID = "warp_position";
    public static final String locationID = "location";
    public static final String testLocationID = "test_location";
    public static final String initID = "init";
    public static final String warpGUIID = "warp_gui";
    public static final String warpID = "warp";
    public static final String warpConfigID = "warp_config";
    public static final String warpGroupConfigID = "warp_group_config";
    public static final String playerUpdateID = "player_update";
    public static final String permissionCheckID = "permission_check";
}
