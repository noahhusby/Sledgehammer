package com.noahhusby.sledgehammer.common.warps;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class WarpPayload {
    @Setter
    private Page defaultPage;
    private final boolean editAccess;
    private final boolean local;
    private final String localGroup;
    private final String salt;
    private final Map<Integer, Warp> waypoints;
    private final Map<String, WarpGroupPayload> groups;
    private final Map<String, List<Integer>> servers;
}
