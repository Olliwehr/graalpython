/*
 * Copyright (c) 2018, 2023, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.python.nodes.function.builtins;

import com.oracle.graal.python.builtins.PythonBuiltinClassType;
import com.oracle.graal.python.builtins.objects.function.PKeyword;
import com.oracle.graal.python.nodes.PRaiseNode;
import com.oracle.graal.python.nodes.function.PythonBuiltinBaseNode;
import com.oracle.graal.python.runtime.exception.PException;
import com.oracle.graal.python.runtime.object.PythonObjectFactory;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ControlFlowException;
import com.oracle.truffle.api.strings.TruffleString;

/**
 * Subclasses must override {@link #varArgExecute(VirtualFrame, Object, Object[], PKeyword[])} to
 * call the e.g. {@link #execute(VirtualFrame, Object, Object[], PKeyword[])} or whatever is right
 * for them, otherwise they will never be on the direct call path.
 */
public abstract class PythonVarargsBuiltinNode extends PythonBuiltinBaseNode {

    @Child private PythonObjectFactory objectFactory;
    @Child private PRaiseNode raiseNode;

    protected final PythonObjectFactory factory() {
        if (objectFactory == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            if (isAdoptable()) {
                objectFactory = insert(PythonObjectFactory.create());
            } else {
                objectFactory = getContext().factory();
            }
        }
        return objectFactory;
    }

    protected final PRaiseNode getRaiseNode() {
        if (raiseNode == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            if (isAdoptable()) {
                raiseNode = insert(PRaiseNode.create());
            } else {
                raiseNode = PRaiseNode.getUncached();
            }
        }
        return raiseNode;
    }

    public PException raise(PythonBuiltinClassType type, TruffleString string) {
        return getRaiseNode().raise(type, string);
    }

    public final PException raise(PythonBuiltinClassType type, TruffleString format, Object... arguments) {
        return getRaiseNode().raise(type, format, arguments);
    }

    public static final class VarargsBuiltinDirectInvocationNotSupported extends ControlFlowException {
        public static final VarargsBuiltinDirectInvocationNotSupported INSTANCE = new VarargsBuiltinDirectInvocationNotSupported();
        private static final long serialVersionUID = 1L;

        private VarargsBuiltinDirectInvocationNotSupported() {
        }
    }

    /**
     * {@code frame} may be null. This function must not be called "execute"
     */
    @SuppressWarnings("unused")
    public Object varArgExecute(VirtualFrame frame, Object self, Object[] arguments, PKeyword[] keywords)
                    throws VarargsBuiltinDirectInvocationNotSupported {
        throw VarargsBuiltinDirectInvocationNotSupported.INSTANCE;
    }

    /**
     * {@code frame} may be null. Most varargs invocations will be (self, *args, *kwargs), so this
     * execute method won't hurt.
     */
    public abstract Object execute(VirtualFrame frame, Object self, Object[] arguments, PKeyword[] keywords);
}
