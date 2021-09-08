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
    public static final String commandID = "command";
    public static final String testLocationID = "test_location";
    public static final String initID = "init";
    public static final String warpGUIID = "warp_gui";
    public static final String webmapID = "webmap";
    public static final String warpID = "warp";
    public static final String warpConfigID = "warp_config";
    public static final String playerUpdateID = "player_update";
    public static final String permissionCheckID = "permission_check";
}
