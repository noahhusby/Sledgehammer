package com.noahhusby.sledgehammer.gui.inventories.warp.config;

import com.noahhusby.sledgehammer.gui.inventories.general.GUIChild;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIController;
import org.bukkit.entity.Player;

public class ConfirmationController extends GUIController {
    private final String warpName;
    private final boolean removed;
    public ConfirmationController(Player player, String warpName, boolean removed) {
        super(27, "Warp Confirmation", player);
        this.warpName = warpName;
        this.removed = removed;
        init();
    }

    public ConfirmationController(GUIController controller, String warpName, boolean removed) {
        super(controller);
        this.warpName = warpName;
        this.removed = removed;
        init();
    }

    @Override
    public void init() {
        GUIChild child = removed ? new RemovalConfirmationInventory(warpName) : new CreationConfirmationInventory(warpName);
        child.initFromController(this, getPlayer(), getInventory());
        openChild(child);
    }
}
