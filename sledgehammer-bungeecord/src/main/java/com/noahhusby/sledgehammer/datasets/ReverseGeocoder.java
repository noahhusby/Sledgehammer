/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ReverseGeocoder.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.datasets;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Class to perform a reverse geocode operation.
 *
 * Notice: this class is <em>not thread-safe</em>. Synchronize if necessary!
 *
 * @author Erich Schubert
 */
public class ReverseGeocoder implements AutoCloseable {
    /** Number of header bytes total. */
    private static final int HEADER_SIZE = 32;

    /** Decoder */
    private static final CharsetDecoder DECODER = StandardCharsets.UTF_8.newDecoder();

    /** Empty array - no match */
    private static final String[] EMPTY = new String[0];

    /** File name */
    private final File filename;

    /** Java file object */
    private RandomAccessFile file;

    /** Java memory map */
    private MappedByteBuffer buffer;

    /** Map size */
    private int width, height;

    /** Map extends */
    private float xscale, yscale, xshift, yshift;

    /** Number of entries in the map. */
    private int numentries;

    /** String cache, so we only have to decode UTF-8 once. */
    private String[][] cache;

    /**
     * Constructor.
     *
     * @param f Index file
     * @throws IOException
     */
    public ReverseGeocoder(File f) throws IOException {
        this.filename = f;
        reopen();
    }

    /**
     * Reopen the index file.
     *
     * @throws IOException
     */
    public void reopen() throws IOException {
        file = new RandomAccessFile(filename, "r");
        buffer = file.getChannel().map(MapMode.READ_ONLY, 0, file.length());
        int magic = buffer.getInt();
        if(magic != 0x6e06e001) {
            throw new IOException("Index file does not have the correct type or version.");
        }
        width = buffer.getInt();
        height = buffer.getInt();
        xscale = width / buffer.getFloat();
        yscale = height / buffer.getFloat();
        xshift = buffer.getFloat();
        yshift = buffer.getFloat();
        numentries = buffer.getInt();
        assert (buffer.position() == HEADER_SIZE);

        cache = new String[numentries][];
    }

    /**
     * Lookup a longitude and latitude coordinate pair.
     *
     * @param lon Longitude
     * @param lat Latitude
     * @return Decoded string
     */
    public String[] lookup(float lon, float lat) {
        return lookupEntry(lookupUncached(lon, lat));
    }

    /**
     * Lookup a longitude and latitude coordinate pair.
     *
     * @param lon Longitude
     * @param lat Latitude
     * @return Index
     */
    public int lookupUncached(float lon, float lat) {
        int x = (int) Math.floor((lon + xshift) * xscale);
        int y = (int) Math.floor((lat + yshift) * yscale);
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return 0;
        }
        // Find the row position
        buffer.limit(buffer.capacity());
        buffer.position(HEADER_SIZE + (y << 2));
        int rowpos = buffer.getInt();
        // Seek to row
        buffer.position(rowpos);
        for(int i = 0; i <= x;) {
            int c = readUnsignedVarint(buffer);
            i += readUnsignedVarint(buffer) + 1;
            if(x < i) {
                return c;
            }
        }
        return 0;
    }

    /**
     * Read an unsigned integer.
     *
     * @param buffer Buffer to read from
     * @return Integer value
     */
    private static int readUnsignedVarint(ByteBuffer buffer) {
        int val = 0;
        int bits = 0;
        while(true) {
            final int data = buffer.get();
            val |= (data & 0x7F) << bits;
            if((data & 0x80) == 0) {
                return val;
            }
            bits += 7;
            if(bits > 35) {
                throw new RuntimeException("Variable length quantity is too long for expected integer.");
            }
        }
    }

    /**
     * Lookup an index to its metadata string.
     *
     * @param idx Index to lookup
     * @return String
     */
    public String[] lookupEntry(int idx) {
        if(idx < 0 || idx >= numentries) {
            return EMPTY;
        }
        return (cache[idx] != null) ? cache[idx] : //
                (cache[idx] = lookupEntryUncached(idx));
    }

    /**
     * Lookup an index entry, uncached.
     *
     * @param idx Index entry
     * @return Decoded data.
     */
    public String[] lookupEntryUncached(int idx) {
        if(idx < 0 || idx >= numentries) {
            return EMPTY;
        }
        // Find the row position
        buffer.limit(buffer.capacity());
        buffer.position(HEADER_SIZE + ((height + idx) << 2));
        int start = buffer.getInt(), endp = buffer.getInt();
        if(start == endp) {
            return EMPTY;
        }
        try {
            // Decode charset:
            buffer.position(start).limit(endp);
            CharBuffer decoded = DECODER.decode(buffer);
            // Count the number of 0-delimited entries
            int nummeta = 0, end = decoded.length();
            for(int i = 0; i < end; i++) {
                if(decoded.get(i) == '\0') {
                    ++nummeta;
                }
            }
            String[] ret = new String[nummeta];
            for(int i = 0, j = 0, k = 0; i < end; i++) {
                if(decoded.get(i) == '\0') {
                    ret[k++] = decoded.subSequence(j, i).toString();
                    j = i + 1;
                }
            }
            return ret;
        }
        catch(CharacterCodingException e) {
            throw new RuntimeException("Invalid encoding in index for entry: " + idx, e);
        }
    }

    @SuppressWarnings("restriction")
    @Override
    public void close() throws IOException {
        cache = null;
        if(buffer != null) {
            // Restricted API, but e.g. the JMH benchmarks fail if we do not unmap.
            //sun.misc.Cleaner cleaner = ((sun.nio.ch.DirectBuffer) buffer).cleaner();
            //cleaner.clean();
            buffer = null;
        }
        if(file != null) {
            file.close();
            file = null;
        }
    }

    /**
     * @return The number of entries in the geocoder.
     */
    public int getNumberOfEntries() {
        return numentries;
    }
}