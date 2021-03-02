package com.noahhusby.sledgehammer.server.gui;

import lombok.Getter;
import lombok.Setter;
import net.wesjd.anvilgui.AnvilGUI;

@Getter
@Setter
public abstract class AnvilChild {
    private CloseMode closeMode = CloseMode.EXIT;
    private AnvilController controller;
    private String text;

    public abstract void build(AnvilGUI.Builder builder);

    public abstract void onLeftItemClick();

    public abstract void onRightItemClick();

    public abstract void onFinish(CloseMode mode);

    public enum CloseMode {
        FINISH, EXIT, LEFT, RIGHT
    }
}

