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
        if (version == null) {
            majorVersion = minorVersion = buildVersion = 0;
            isDevBuild = true;
            return;
        }
        String[] versions = version.split("\\.");
        if (versions.length < 3) {
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
        if (!(other instanceof SledgehammerVersion)) {
            return false;
        }
        return this.compareTo((SledgehammerVersion) other) == 0;
    }

    @Override
    public int compareTo(SledgehammerVersion other) {
        if (other == null) {
            return Integer.MAX_VALUE;
        }

        if (this.isDevBuild() && other.isDevBuild()) {
            return 0;
        } else if (this.isDevBuild) {
            return Integer.MAX_VALUE;
        } else if (other.isDevBuild) {
            return Integer.MIN_VALUE;
        }

        int majorCompare = this.majorVersion - other.majorVersion;
        if (majorCompare != 0) {
            return majorCompare;
        }

        int minorCompare = this.minorVersion - other.minorVersion;
        if (minorCompare != 0) {
            return minorCompare;
        }

        return this.buildVersion - other.buildVersion;
    }

    @Override
    public String toString() {
        String version;
        if (isDevBuild) {
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
