/*
 * Copyright (c) 2020, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oracle.graal.python.builtins.objects.bytes;

import java.nio.ByteOrder;

import com.oracle.graal.python.builtins.PythonBuiltinClassType;
import com.oracle.graal.python.builtins.objects.buffer.PythonBufferAccessLibrary;
import com.oracle.graal.python.builtins.objects.buffer.PythonBufferAcquireLibrary;
import com.oracle.graal.python.nodes.PRaiseNode;
import com.oracle.graal.python.nodes.util.CastToJavaIntExactNode;
import com.oracle.graal.python.runtime.sequence.PSequence;
import com.oracle.graal.python.runtime.sequence.storage.ByteSequenceStorage;
import com.oracle.graal.python.runtime.sequence.storage.NativeByteSequenceStorage;
import com.oracle.graal.python.runtime.sequence.storage.SequenceStorage;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Cached.Exclusive;
import com.oracle.truffle.api.dsl.Cached.Shared;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidBufferOffsetException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.Shape;

@SuppressWarnings("truffle-abstract-export")
@ExportLibrary(PythonBufferAcquireLibrary.class)
@ExportLibrary(PythonBufferAccessLibrary.class)
@ExportLibrary(InteropLibrary.class)
public abstract class PBytesLike extends PSequence {

    protected SequenceStorage store;

    public PBytesLike(Object cls, Shape instanceShape, byte[] bytes) {
        super(cls, instanceShape);
        store = new ByteSequenceStorage(bytes);
    }

    public PBytesLike(Object cls, Shape instanceShape, SequenceStorage store) {
        super(cls, instanceShape);
        this.store = store;
    }

    @Override
    public final SequenceStorage getSequenceStorage() {
        return store;
    }

    @Override
    public void setSequenceStorage(SequenceStorage store) {
        assert store instanceof ByteSequenceStorage || store instanceof NativeByteSequenceStorage;
        this.store = store;
    }

    @ExportMessage
    @SuppressWarnings("static-method")
    boolean hasBuffer() {
        return true;
    }

    @ExportMessage
    Object acquire(@SuppressWarnings("unused") int flags) {
        return this;
    }

    @ExportMessage
    @SuppressWarnings("static-method")
    boolean isBuffer() {
        return true;
    }

    @ExportMessage
    int getBufferLength(
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) {
        return bufferLib.getBufferLength(store);
    }

    @ExportMessage
    boolean hasInternalByteArray(
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) {
        return bufferLib.hasInternalByteArray(store);
    }

    @ExportMessage
    byte[] getInternalByteArray(
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) {
        return bufferLib.getInternalByteArray(store);
    }

    @ExportMessage
    byte readByte(int byteOffset,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) {
        return bufferLib.readByte(store, byteOffset);
    }

    @ExportMessage
    short readShortByteOrder(int byteOffset, ByteOrder byteOrder,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) {
        return bufferLib.readShortByteOrder(store, byteOffset, byteOrder);
    }

    @ExportMessage
    int readIntByteOrder(int byteOffset, ByteOrder byteOrder,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) {
        return bufferLib.readIntByteOrder(store, byteOffset, byteOrder);
    }

    @ExportMessage
    long readLongByteOrder(int byteOffset, ByteOrder byteOrder,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) {
        return bufferLib.readLongByteOrder(store, byteOffset, byteOrder);
    }

    @ExportMessage
    float readFloatByteOrder(int byteOffset, ByteOrder byteOrder,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) {
        return bufferLib.readFloatByteOrder(store, byteOffset, byteOrder);
    }

    @ExportMessage
    double readDoubleByteOrder(int byteOffset, ByteOrder byteOrder,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) {
        return bufferLib.readDoubleByteOrder(store, byteOffset, byteOrder);
    }

    @ExportMessage
    public boolean hasBufferElements() {
        return true;
    }

    @ExportMessage
    public boolean isBufferWritable() throws UnsupportedMessageException {
        return false;
    }

    @ExportMessage
    public long getBufferSize(@Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) throws UnsupportedMessageException {
        return bufferLib.getBufferLength(store);
    }

    @ExportMessage
    @SuppressWarnings("truffle-inlining")
    public byte readBufferByte(long byteOffset,
                    @Bind("$node") Node inliningTarget,
                    // GR-44020: make shared:
                    @Exclusive @Cached CastToJavaIntExactNode toIntNode,
                    // GR-44020: make shared:
                    @Exclusive @Cached PRaiseNode.Lazy raiseNode,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) throws UnsupportedMessageException, InvalidBufferOffsetException {
        int offset = toIntNode.executeWithThrow(inliningTarget, byteOffset, raiseNode, PythonBuiltinClassType.ValueError);
        return bufferLib.readByte(store, offset);
    }

    @ExportMessage
    public void writeBufferByte(long byteOffset, byte value) throws UnsupportedMessageException, InvalidBufferOffsetException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @SuppressWarnings("truffle-inlining")
    public short readBufferShort(ByteOrder order, long byteOffset,
                    @Bind("$node") Node inliningTarget,
                    // GR-44020: make shared:
                    @Exclusive @Cached CastToJavaIntExactNode toIntNode,
                    // GR-44020: make shared:
                    @Exclusive @Cached PRaiseNode.Lazy raiseNode,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) throws UnsupportedMessageException, InvalidBufferOffsetException {
        int offset = toIntNode.executeWithThrow(inliningTarget, byteOffset, raiseNode, PythonBuiltinClassType.ValueError);
        return bufferLib.readShortByteOrder(store, offset, order);
    }

    @ExportMessage
    public void writeBufferShort(ByteOrder order, long byteOffset, short value) throws UnsupportedMessageException, InvalidBufferOffsetException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @SuppressWarnings("truffle-inlining")
    public int readBufferInt(ByteOrder order, long byteOffset,
                    @Bind("$node") Node inliningTarget,
                    // GR-44020: make shared:
                    @Exclusive @Cached CastToJavaIntExactNode toIntNode,
                    // GR-44020: make shared:
                    @Exclusive @Cached PRaiseNode.Lazy raiseNode,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) throws UnsupportedMessageException, InvalidBufferOffsetException {
        int offset = toIntNode.executeWithThrow(inliningTarget, byteOffset, raiseNode, PythonBuiltinClassType.ValueError);
        return bufferLib.readIntByteOrder(store, offset, order);
    }

    @ExportMessage
    public void writeBufferInt(ByteOrder order, long byteOffset, int value) throws UnsupportedMessageException, InvalidBufferOffsetException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @SuppressWarnings("truffle-inlining")
    public long readBufferLong(ByteOrder order, long byteOffset,
                    @Bind("$node") Node inliningTarget,
                    // GR-44020: make shared:
                    @Exclusive @Cached CastToJavaIntExactNode toIntNode,
                    // GR-44020: make shared:
                    @Exclusive @Cached PRaiseNode.Lazy raiseNode,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) throws UnsupportedMessageException, InvalidBufferOffsetException {
        int offset = toIntNode.executeWithThrow(inliningTarget, byteOffset, raiseNode, PythonBuiltinClassType.ValueError);
        return bufferLib.readLongByteOrder(store, offset, order);
    }

    @ExportMessage
    public void writeBufferLong(ByteOrder order, long byteOffset, long value) throws UnsupportedMessageException, InvalidBufferOffsetException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @SuppressWarnings("truffle-inlining")
    public float readBufferFloat(ByteOrder order, long byteOffset,
                    @Bind("$node") Node inliningTarget,
                    // GR-44020: make shared:
                    @Exclusive @Cached CastToJavaIntExactNode toIntNode,
                    // GR-44020: make shared:
                    @Exclusive @Cached PRaiseNode.Lazy raiseNode,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) throws UnsupportedMessageException, InvalidBufferOffsetException {
        int offset = toIntNode.executeWithThrow(inliningTarget, byteOffset, raiseNode, PythonBuiltinClassType.ValueError);
        return bufferLib.readFloatByteOrder(store, offset, order);
    }

    @ExportMessage
    public void writeBufferFloat(ByteOrder order, long byteOffset, float value) throws UnsupportedMessageException, InvalidBufferOffsetException {
        throw UnsupportedMessageException.create();
    }

    @ExportMessage
    @SuppressWarnings("truffle-inlining")
    public double readBufferDouble(ByteOrder order, long byteOffset,
                    @Bind("$node") Node inliningTarget,
                    // GR-44020: make shared:
                    @Exclusive @Cached CastToJavaIntExactNode toIntNode,
                    // GR-44020: make shared:
                    @Exclusive @Cached PRaiseNode.Lazy raiseNode,
                    @Shared("bufferLib") @CachedLibrary(limit = "2") PythonBufferAccessLibrary bufferLib) throws UnsupportedMessageException, InvalidBufferOffsetException {
        int offset = toIntNode.executeWithThrow(inliningTarget, byteOffset, raiseNode, PythonBuiltinClassType.ValueError);
        return bufferLib.readDoubleByteOrder(store, offset, order);
    }

    @ExportMessage
    public void writeBufferDouble(ByteOrder order, long byteOffset, double value) throws UnsupportedMessageException, InvalidBufferOffsetException {
        throw UnsupportedMessageException.create();
    }
}
