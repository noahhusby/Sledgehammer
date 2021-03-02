package com.noahhusby.sledgehammer.common.warps;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
public class WarpPayload {
    @Setter
    private Page defaultPage;
    @Setter
    private boolean override;
    private final boolean editAccess;
    private final boolean local;
    private final String localGroup;
    private final String salt;
    private final List<WarpGroup> groups;

    public enum Page {
        ALL, PINNED, GROUPS
    }
}
