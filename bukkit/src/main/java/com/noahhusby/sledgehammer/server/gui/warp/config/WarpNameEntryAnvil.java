package com.noahhusby.sledgehammer.server.gui.warp.config;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.Sledgehammer;
import com.noahhusby.sledgehammer.server.gui.AnvilChild;
import com.noahhusby.sledgehammer.server.gui.AnvilController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.S2P.S2PWarpConfigPacket;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WarpNameEntryAnvil extends AnvilChild {

    private final WarpConfigPayload payload;

    public WarpNameEntryAnvil(WarpConfigPayload payload) {
        this.payload = payload;
    }

    @Override
    public void build(AnvilGUI.Builder builder) {
        builder.text("Enter Warp Name")
                .title("Create a warp")
                .plugin(Sledgehammer.getInstance());

        ItemStack warp = new ItemStack(Material.WOOL, 1, (byte) 4);
        builder.itemLeft(warp);
    }

    @Override
    public void onLeftItemClick() {
        GUIRegistry.register(new ConfigMenu.ConfigMenuController(getController().getPlayer(), payload));
    }

    @Override
    public void onRightItemClick() {

    }

    @Override
    public void onFinish(CloseMode mode) {
        switch (mode) {
            case EXIT:
                break;
            case RIGHT:
            case LEFT:
                GUIRegistry.register(new ConfigMenu.ConfigMenuController(getController().getPlayer(), payload));
                break;
            case FINISH:
                if (getText().equals("")) {
                    GUIRegistry.register(new WarpNameEntryController(getController().getPlayer(), payload));
                } else {
                    JsonObject data = new JsonObject();
                    data.addProperty("warpName", getText());
                    NetworkHandler.getInstance().send(new S2PWarpConfigPacket(
                            S2PWarpConfigPacket.ProxyConfigAction.CREATE_WARP, getController().getPlayer(), payload.getSalt(),
                            data));
                }
                break;
        }
    }

    public static class WarpNameEntryController extends AnvilController {
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
}
