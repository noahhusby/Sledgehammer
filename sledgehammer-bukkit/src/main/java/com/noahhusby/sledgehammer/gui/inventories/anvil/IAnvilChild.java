package com.noahhusby.sledgehammer.gui.inventories.anvil;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public interface IAnvilChild {
    AnvilGUI.Builder build(AnvilGUI.Builder builder);
    void onLeftItemClick();
    void onRightItemClick();
    void onFinish();
}
