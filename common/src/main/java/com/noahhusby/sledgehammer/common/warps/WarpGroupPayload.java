package com.noahhusby.sledgehammer.common.warps;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Noah Husby
 */
@Getter
@RequiredArgsConstructor
public class WarpGroupPayload {
    private final String id;
    private final String name;
    private final String headId;
    private final List<Integer> warps;
}