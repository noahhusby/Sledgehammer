package com.noahhusby.sledgehammer.server.gui.warp.config;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.Sledgehammer;
import com.noahhusby.sledgehammer.server.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.gui.AnvilChild;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.network.S2P.S2PWarpConfigPacket;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
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
        GUIRegistry.register(new ConfigMenuController(getController().getPlayer(), payload));
    }

    @Override
    public void onRightItemClick() {

    }

    @Override
    public void onFinish(CloseMode mode) {
        switch (mode)  {
            case EXIT:
                break;
            case RIGHT:
            case LEFT:
                GUIRegistry.register(new ConfigMenuController(getController().getPlayer(), payload));
                break;
            case FINISH:
                if(getText().equals("")) {
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
}
