/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ExitSkipToolbar.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.dialogs.toolbars;

public class ExitSkipToolbar extends Toolbar {
    public ExitSkipToolbar() {
        addTool("exit", "exit the dialog");
        addTool("@", "skip this prompt");
    }
}
