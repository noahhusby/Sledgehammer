package com.noahhusby.sledgehammer.tasks;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.gui.GUIRegistry;
import com.noahhusby.sledgehammer.gui.IGUIListener;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Skull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarpGUITask extends Task implements IResponse {

    JSONObject response = new JSONObject();

    public WarpGUITask(TransferPacket t, JSONObject data) {
        super(t, data);
    }

    @Override
    public String getResponseCommand() {
        return Constants.warpGUITask;
    }

    @Override
    public JSONObject response() {
        return generateTaskHeader(response);
    }

    @Override
    public String getCommandName() {
        return Constants.warpGUITask;
    }

    @Override
    public void execute() {
        Player p = Bukkit.getPlayer(getTransferPacket().sender);
        if(p == null) {
            throwNoSender();
            return;
        }

        if(!p.isOnline()) {
            throwNoSender();
            return;
        }

        JSONObject data = getData();
        JSONArray waypoints = (JSONArray) data.get("waypoints");
        boolean web = (boolean) data.get("web");

        GUITracker g = new GUITracker(waypoints.size(), web, this);

        g.setWarps(waypoints);

        g.register();
        g.open(p);
    }

    @Override
    public IResponse getResponse() {
        return this;
    }

    @Override
    public void build(TransferPacket t, JSONObject data) {
        TaskHandler.getInstance().queueTask(new WarpGUITask(t, data));
    }

    public class GUITracker implements IGUIListener {
        private List<WarpGUI> warpGUIs = new ArrayList<>();
        private Inventory inventory = Bukkit.createInventory(null, 54, "Warps");
        private WarpGUI currentGUI;

        private WarpGUITask task;
        private boolean web;
        private Player player;
        private int size;

        public GUITracker(int size, boolean web, WarpGUITask task) {
            this.size = size;
            this.web = web;
            this.task = task;
        }

        public void setWarps(JSONArray o) {
            if(warpGUIs.isEmpty()) {
                WarpGUI w = new WarpGUI(0, size, web);
                currentGUI = w;
                warpGUIs.add(w);
            }

            for(Object ob : o) {
                JSONObject warp = (JSONObject) ob;
                if (currentGUI.isFull()) {
                    WarpGUI w = new WarpGUI(warpGUIs.size(), size, web);
                    currentGUI = w;
                    warpGUIs.add(w);
                }
                currentGUI.addWarp(warp);
            }
        }

        public void open(Player p) {
            this.player = p;
            currentGUI = warpGUIs.get(0);
            inventory.setContents(currentGUI.getInventory().getContents());
            p.openInventory(inventory);
        }

        public void register() {
            GUIRegistry.register(this);
        }

        @Override
        public void onInventoryClick(InventoryClickEvent e) {
            e.setCancelled(true);
            if(e.getCurrentItem() == null) return;
            if(SledgehammerUtil.compare(e.getCurrentItem().getItemMeta().getDisplayName(), "Left")) {
                if(currentGUI.getPage() - 1 < 0) return;
                for(WarpGUI w : warpGUIs) {
                    if(w.getPage() == currentGUI.getPage() - 1) {
                        currentGUI = w;
                        inventory.setContents(w.getInventory().getContents());
                        player.updateInventory();
                        return;
                    }
                }
            }

            if(SledgehammerUtil.compare(e.getCurrentItem().getItemMeta().getDisplayName(), "Right")) {
                //if(currentGUI.getPage() - 1 < 0) return;
                for(WarpGUI w : warpGUIs) {
                    if(w.getPage() == currentGUI.getPage() + 1) {
                        currentGUI = w;
                        inventory.setContents(w.getInventory().getContents());
                        player.updateInventory();
                        return;
                    }
                }
            }

            if(SledgehammerUtil.compare(e.getCurrentItem().getItemMeta().getDisplayName(), ChatColor.RED + "" + ChatColor.BOLD + "Close GUI")) {
                player.closeInventory();
                return;
            }

            if(SledgehammerUtil.compare(e.getCurrentItem().getItemMeta().getDisplayName(), ChatColor.GREEN + "Open Web Map")) {
                player.closeInventory();
                response.put("action", "webgui");
                TaskHandler.getInstance().sendResponse(task);
                return;
            }


        }

        @Override
        public void onInventoryClose(InventoryCloseEvent e) {
            if(response.isEmpty()) {
                response.put("action", "none");
                TaskHandler.getInstance().sendResponse(task);
            }
        }

        @Override
        public List<Inventory> getInventories() {
            List<Inventory> i = new ArrayList<>();
            i.add(inventory);
            return i;
        }
    }

    public class WarpGUI {
        private Inventory i;
        private final int page;
        private final int size;
        private int current = 9;

        public WarpGUI(int page, int size, boolean web) {
            this.page = page;
            this.size = size;

            i = Bukkit.createInventory(null, 54, "Warps - Page "+(page+1));
            init(web);
        }

        private void init(boolean web) {
            for(int x = 0; x < 54; x++) {
                ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
                i.setItem(x, glass);
            }

            i.setItem(49, generateExit());

            if(web) {
                i.setItem(40, getSkull(Constants.globeHead, ChatColor.GREEN + "Open Web Map"));
            }

            if(page != 0) {
                i.setItem(45, getSkull(Constants.arrowLeftHead, "Left"));
            }

            if(size > (page + 1) * Constants.warpsPerPage) {
                i.setItem(53, getSkull(Constants.arrowRightHead, "Right"));
            }
        }

        public void addWarp(JSONObject o) {
            ItemStack warp = new ItemStack(Material.WOOL);
            ItemMeta meta = warp.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + (String) o.get("name"));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Server: " + (String) o.get("server"));
            lore.add(ChatColor.DARK_GRAY + "> " + ChatColor.GREEN + "Click to warp.");
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            meta.setLore(lore);

            warp.setItemMeta(meta);
            i.setItem(current, warp);
            current++;
        }

        public boolean isFull() {
            return current > 35;
        }

        public int getPage() {
            return page;
        }

        public Inventory getInventory() {
            return i;
        }

        private ItemStack generateExit() {
            ItemStack exit = new ItemStack(Material.BARRIER);
            ItemMeta m = exit.getItemMeta();
            m.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close GUI");
            exit.setItemMeta(m);

            return exit;
        }

        public ItemStack getSkull(String h, String n) {
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            mutateItemMeta(meta, h);
            meta.setDisplayName(n);
            head.setItemMeta(meta);
            return head;
        }

        private void mutateItemMeta(SkullMeta meta, String b64) {
            Method metaSetProfileMethod = null;
            Field metaProfileField = null;
            try {
                if (metaSetProfileMethod == null) {
                    metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                    metaSetProfileMethod.setAccessible(true);
                }
                metaSetProfileMethod.invoke(meta, makeProfile(b64));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                // if in an older API where there is no setProfile method,
                // we set the profile field directly.
                try {
                    if (metaProfileField == null) {
                        metaProfileField = meta.getClass().getDeclaredField("profile");
                        metaProfileField.setAccessible(true);
                    }
                    metaProfileField.set(meta, makeProfile(b64));

                } catch (NoSuchFieldException | IllegalAccessException ex2) {
                    ex2.printStackTrace();
                }
            }
        }

        private GameProfile makeProfile(String b64) {
            UUID id = new UUID(
                    b64.substring(b64.length() - 20).hashCode(),
                    b64.substring(b64.length() - 10).hashCode()
            );
            GameProfile profile = new GameProfile(id, "aaaaa");
            profile.getProperties().put("textures", new Property("textures", b64));
            return profile;
        }

    }
}
