/*
 *  Copyright (c) 2021 Noah Husby
 *  Sledgehammer - SledgehammerVersion.java
 *
 *  Sledgehammer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Sledgehammer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Noah Husby
 * A class represtenting a single version of Sledgehammer
 */
@Getter
public class SledgehammerVersion implements Comparable<SledgehammerVersion> {
    private int majorVersion;
    private int minorVersion;
    private int buildVersion;

    private boolean isDevBuild = false;

    public SledgehammerVersion(String version) {
        if(version == null) {
            majorVersion = minorVersion = buildVersion = 0;
            isDevBuild = true;
            return;
        }
        try {
            String[] splitVersion = version.split("-");
            String[] versions = splitVersion[0].split("\\.");
            majorVersion = Integer.parseInt(versions[0]);
            minorVersion = Integer.parseInt(versions[1]);
            buildVersion = Integer.parseInt(versions[2]);
        } catch (Exception e) {
            majorVersion = minorVersion = buildVersion = 0;
            isDevBuild = true;
        }
    }

    @Override
    public int compareTo(SledgehammerVersion other) {
        if(other == null) {
            return Integer.MAX_VALUE;
        }

        if(this.isDevBuild() && other.isDevBuild()) {
            return 0;
        } else if(this.isDevBuild) {
            return Integer.MAX_VALUE;
        } else if(other.isDevBuild) {
            return Integer.MIN_VALUE;
        }

        int majorCompare = this.majorVersion - other.majorVersion;
        if(majorCompare != 0) {
            return majorCompare;
        }

        int minorCompare = this.minorVersion - other.minorVersion;
        if(minorCompare != 0) {
            return minorCompare;
        }

        return this.buildVersion - other.buildVersion;
    }
}
