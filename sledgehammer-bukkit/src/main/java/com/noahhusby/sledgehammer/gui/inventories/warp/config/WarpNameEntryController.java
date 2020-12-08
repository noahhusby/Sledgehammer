package com.noahhusby.sledgehammer.gui.inventories.warp.config;

import com.noahhusby.sledgehammer.gui.inventories.anvil.AnvilController;
import org.bukkit.entity.Player;

public class WarpNameEntryController extends AnvilController {
    public WarpNameEntryController(AnvilController controller) {
        super(controller);
        init();
    }

    public WarpNameEntryController(Player player) {
        super(player);
        init();
    }

    @Override
    public void init() {
        openChild(new WarpNameEntryAnvil());
    }
}
