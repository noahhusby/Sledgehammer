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

import com.noahhusby.sledgehammer.common.exceptions.VersionParseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * @author Noah Husby
 * A class represtenting a single version of Sledgehammer
 */
@Getter
@RequiredArgsConstructor
public class SledgehammerVersion implements Comparable<SledgehammerVersion> {
    private final int majorVersion;
    private final int minorVersion;
    private final int buildVersion;

    private final boolean isDevBuild;

    public SledgehammerVersion(int major, int minor, int build) {
        this(major, minor, build, false);
    }

    public SledgehammerVersion(String version) throws VersionParseException {
        if(version == null) {
            majorVersion = minorVersion = buildVersion = 0;
            isDevBuild = true;
            return;
        }
        String[] versions = version.split("\\.");
        if(versions.length < 3) {
            throw new VersionParseException(String.format("Invalid version input: %s", version));
        }
        try {
            majorVersion = Integer.parseInt(versions[0]);
            minorVersion = Integer.parseInt(versions[1]);
            buildVersion = Integer.parseInt(versions[2]);
            isDevBuild = false;
        } catch (NumberFormatException e) {
            throw new VersionParseException(String.format("Invalid version input: %s", version));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorVersion, minorVersion, buildVersion);
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof SledgehammerVersion)) {
            return false;
        }
        return this.compareTo((SledgehammerVersion) other) == 0;
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

    @Override
    public String toString() {
        String version;
        if(isDevBuild) {
            version = "[Development Build]";
        } else {
            version = String.format("%d.%d.%d", majorVersion, minorVersion, buildVersion);
        }
        return version;
    }

    /**
     * Checks whether another version is newer than the local version
     *
     * @param other {@link SledgehammerVersion}
     * @return True if compared version is newer, false if not
     */
    public boolean isNewer(SledgehammerVersion other) {
        return this.compareTo(other) > 0;
    }

    /**
     * Checks whether another version is older than the local version
     *
     * @param other {@link SledgehammerVersion}
     * @return True if compared version is older, false if not
     */
    public boolean isOlder(SledgehammerVersion other) {
        return this.compareTo(other) < 0;
    }

    /**
     * Checks whether another version is the same as the local version
     *
     * @param other {@link SledgehammerVersion}
     * @return True if compared version is the same, false if not
     */
    public boolean isSame(SledgehammerVersion other) {
        return this.compareTo(other) == 0;
    }
}
