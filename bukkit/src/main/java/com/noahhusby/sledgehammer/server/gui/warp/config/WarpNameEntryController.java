package com.noahhusby.sledgehammer.server.gui.warp.config;

import com.noahhusby.sledgehammer.server.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.gui.AnvilController;
import org.bukkit.entity.Player;

public class WarpNameEntryController extends AnvilController {
    private final WarpConfigPayload payload;

    public WarpNameEntryController(AnvilController controller, WarpConfigPayload payload) {
        super(controller);
        this.payload = payload;
        init();
    }

    public WarpNameEntryController(Player player, WarpConfigPayload payload) {
        super(player);
        this.payload = payload;
        init();
    }

    @Override
    public void init() {
        openChild(new WarpNameEntryAnvil(payload));
    }
}
