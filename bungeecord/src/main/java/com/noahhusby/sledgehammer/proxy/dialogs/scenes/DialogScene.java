/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.proxy.dialogs.scenes;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.proxy.utils.ChatUtil;
import com.noahhusby.sledgehammer.proxy.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.proxy.dialogs.components.IDialogComponent;
import com.noahhusby.sledgehammer.proxy.dialogs.toolbars.DefaultToolbar;
import com.noahhusby.sledgehammer.proxy.dialogs.toolbars.IToolbar;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Map;

public abstract class DialogScene implements IDialogScene {

    protected IDialogComponent currentComponent;
    protected CommandSender sender;

    protected final DialogHandler dialogHandler = DialogHandler.getInstance();
    private final Map<Integer, IDialogComponent> components = Maps.newHashMap();

    @Override
    public void init(CommandSender commandSender) {
        this.sender = commandSender;
        this.currentComponent = components.get(0);
        dialogHandler.progressDialog(this, false);
        onInitialization();
    }

    protected void registerComponent(IDialogComponent d) {
        components.put(components.size(), d);
    }

    @Override
    public IDialogComponent getCurrentComponent() {
        return currentComponent;
    }

    @Override
    public CommandSender getCommandSender() {
        return sender;
    }

    @Override
    public void onMessage(String m) {
        for (Map.Entry<String, String> x : getToolbar().getTools().entrySet()) {
            if (x.getKey().equals(m.toLowerCase().trim())) {
                onToolbarAction(m);
                return;
            }
        }

        progressDialog(m);
    }

    @Override
    public IToolbar getToolbar() {
        return new DefaultToolbar();
    }

    @Override
    public void onToolbarAction(String m) {
        if (m.equals("exit")) {
            dialogHandler.discardDialog(this);
            getCommandSender().sendMessage();
            getCommandSender().sendMessage(ChatUtil.combine(ChatColor.RED, "Exited the dialog!"));
        }
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public TextComponent getTitle() {
        return null;
    }

    @Override
    public void onComponentFinish() {
    }

    @Override
    public void onInitialization() {
    }

    public void progressDialog(String m) {
        progressDialog(m, false);
    }

    public void progressDialog(String m, boolean override) {
        if (currentComponent.validateResponse(m)) {
            currentComponent.setValue(m);
            if (currentComponent.isManual() && !override) {
                onComponentFinish();
                return;
            }
        } else {
            dialogHandler.progressDialog(this, true);
            return;
        }

        int x = components.size();
        for (Map.Entry<Integer, IDialogComponent> c : components.entrySet()) {
            if (c.getValue() == currentComponent) {
                if ((c.getKey() + 1) >= x) {
                    dialogHandler.discardDialog(this);
                    onFinish();
                    return;
                }
                currentComponent = components.get(c.getKey() + 1);
                dialogHandler.progressDialog(this, false);
                return;
            }
        }
        dialogHandler.progressDialog(this, false);
    }

    public String getValue(String key) {
        for (Map.Entry<Integer, IDialogComponent> s : components.entrySet()) {
            if (s.getValue().getKey().equalsIgnoreCase(key)) {
                return s.getValue().getValue();
            }
        }

        return "";
    }

    protected void discard() {
        DialogHandler.getInstance().discardDialog(this);
    }

    protected void start(DialogScene scene) {
        DialogHandler.getInstance().startDialog(getCommandSender(), scene);
    }

    protected void discardAndStart(DialogScene scene) {
        discard();
        start(scene);
    }
}
