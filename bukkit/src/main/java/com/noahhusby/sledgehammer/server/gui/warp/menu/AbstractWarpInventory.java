package com.noahhusby.sledgehammer.server.gui.warp.menu;

import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.util.WarpGUIUtil;
import org.bukkit.Material;

/**
 * @author Noah Husby
 */
public abstract class AbstractWarpInventory extends GUIChild {
    @Override
    public void init() {
        fillInventory(createItem(Material.STAINED_GLASS_PANE, 1, (byte) 15, null));
        setItem(49, WarpGUIUtil.generateExit());
        setItem(45, WarpGUIUtil.generateWarpSort());
        if (getWarpController().getPayload().isEditAccess()) {
            setItem(46, WarpGUIUtil.generateWarpAnvil());
        }
    }

    protected AbstractWarpInventoryController<? extends AbstractWarpInventory> getWarpController() {
        return (AbstractWarpInventoryController<? extends AbstractWarpInventory>) getController();
    }

    public abstract int getPage();
}
