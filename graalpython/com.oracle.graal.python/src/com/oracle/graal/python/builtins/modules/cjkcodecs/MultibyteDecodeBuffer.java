/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.python.builtins.modules.cjkcodecs;

import static com.oracle.graal.python.builtins.modules.cjkcodecs.MultibyteCodecUtil.writerInit;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.MemoryError;
import static com.oracle.graal.python.util.PythonUtils.toTruffleStringUncached;
import static com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import com.oracle.graal.python.builtins.objects.bytes.PBytes;
import com.oracle.graal.python.builtins.objects.exception.PBaseException;
import com.oracle.graal.python.nodes.PRaiseNode;
import com.oracle.graal.python.runtime.object.PythonObjectFactory;
import com.oracle.truffle.api.strings.TruffleString;

public class MultibyteDecodeBuffer {

    ByteBuffer inputBuffer;
    protected CharBuffer writer;
    protected PBaseException excobj;

    public MultibyteDecodeBuffer(byte[] inbuf) {
        this.inputBuffer = ByteBuffer.wrap(inbuf);
        this.writer = writerInit(inbuf.length);
        this.excobj = null;
    }

    protected int getInSize() {
        return inputBuffer.limit();
    }

    @TruffleBoundary
    protected void incInpos(int len) {
        setInpos(getInpos() + len);
    }

    protected int getInpos() {
        return inputBuffer.position();
    }

    @TruffleBoundary
    protected void setInpos(int pos) {
        inputBuffer.position(pos);
    }

    protected int remaining() {
        return inputBuffer.remaining();
    }

    @TruffleBoundary
    protected void getRemaining(byte[] dst, int dstOff, int len) {
        inputBuffer.get(dst, dstOff, len);
    }

    protected boolean isFull() {
        return !inputBuffer.hasRemaining();
    }

    protected PBytes createPBytes(PythonObjectFactory factory) {
        return factory.createBytes(inputBuffer.array(), getInpos());
    }

    protected void replaceInbuf(byte[] inbuf) {
        inputBuffer = ByteBuffer.wrap(inbuf);
    }

    protected int getOutpos() {
        return writer.position();
    }

    @TruffleBoundary
    protected void writeChar(char c) {
        writer.append(c);
    }

    @TruffleBoundary
    protected void writeStr(String str) {
        writer.append(str);
    }

    @TruffleBoundary
    protected TruffleString toTString() {
        return toTruffleStringUncached(writer.toString());
    }

    @TruffleBoundary
    protected void grow(PRaiseNode raiseNode) {
        int newCapacity = 2 * writer.capacity() + 1;
        if (newCapacity < 0) {
            throw raiseNode.raise(MemoryError);
        }
        CharBuffer newBuffer = CharBuffer.allocate(newCapacity);
        writer.flip();
        newBuffer.put(writer);
        writer = newBuffer;
    }
}
