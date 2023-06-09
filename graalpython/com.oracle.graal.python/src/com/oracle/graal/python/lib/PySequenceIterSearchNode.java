/*
 * Copyright (c) 2022, 2023, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.python.lib;

import com.oracle.graal.python.builtins.PythonBuiltinClassType;
import com.oracle.graal.python.builtins.objects.PNone;
import com.oracle.graal.python.builtins.objects.type.SpecialMethodSlot;
import com.oracle.graal.python.nodes.ErrorMessages;
import com.oracle.graal.python.nodes.PNodeWithContext;
import com.oracle.graal.python.nodes.PRaiseNode;
import com.oracle.graal.python.nodes.call.special.CallUnaryMethodNode;
import com.oracle.graal.python.nodes.call.special.LookupSpecialMethodSlotNode;
import com.oracle.graal.python.nodes.object.BuiltinClassProfiles.IsBuiltinObjectProfile;
import com.oracle.graal.python.nodes.object.InlinedGetClassNode;
import com.oracle.graal.python.runtime.exception.PException;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.nodes.LoopNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.profiles.InlinedIntValueProfile;

/**
 * Equivalent of CPython's {@code _PySequence_IterSearch}.
 */
@GenerateUncached
@ImportStatic(SpecialMethodSlot.class)
public abstract class PySequenceIterSearchNode extends PNodeWithContext {
    // return # of times obj appears in seq.
    public static final int PY_ITERSEARCH_COUNT = 1;
    // return 0-based index of first occurrence of obj in seq
    public static final int PY_ITERSEARCH_INDEX = 2;
    // return 1 if obj in seq, else 0
    public static final int PY_ITERSEARCH_CONTAINS = 3;

    public abstract int execute(Frame frame, Object container, Object key, int operation);

    public final int execute(Object container, Object key, int operation) {
        return execute(null, container, key, operation);
    }

    @Specialization
    int search(Frame frame, Object container, Object key, int operation,
                    @Bind("this") Node inliningTarget,
                    @Cached PyObjectGetIter getIter,
                    @Cached IsBuiltinObjectProfile noIterProfile,
                    @Cached PRaiseNode raiseNode,
                    @Cached InlinedGetClassNode getIterClass,
                    @Cached(parameters = "Next") LookupSpecialMethodSlotNode lookupIternext,
                    @Cached IsBuiltinObjectProfile noNextProfile,
                    @Cached CallUnaryMethodNode callNext,
                    @Cached PyObjectRichCompareBool.EqNode eqNode,
                    @Cached IsBuiltinObjectProfile stopIterationProfile,
                    @Cached InlinedIntValueProfile opProfile) {
        Object iterator;
        try {
            iterator = getIter.execute(frame, container);
        } catch (PException e) {
            e.expectTypeError(inliningTarget, noIterProfile);
            throw raiseNode.raise(PythonBuiltinClassType.TypeError, ErrorMessages.IS_NOT_A_CONTAINER, container);
        }
        Object next = PNone.NO_VALUE;
        try {
            next = lookupIternext.execute(frame, getIterClass.execute(inliningTarget, iterator), iterator);
        } catch (PException e) {
            e.expect(inliningTarget, PythonBuiltinClassType.AttributeError, noNextProfile);
        }
        if (next instanceof PNone) {
            throw raiseNode.raise(PythonBuiltinClassType.TypeError, ErrorMessages.OBJ_NOT_ITERABLE, iterator);
        }
        int i = 0;
        int n = 0;
        boolean wrapped = false;
        while (true) {
            try {
                if (eqNode.execute(frame, callNext.executeObject(frame, next, iterator), key)) {
                    switch (opProfile.profile(inliningTarget, operation)) {
                        case PY_ITERSEARCH_COUNT:
                            n++;
                            break;
                        case PY_ITERSEARCH_INDEX:
                            if (CompilerDirectives.hasNextTier()) {
                                LoopNode.reportLoopCount(this, wrapped ? Integer.MAX_VALUE : i + 1);
                            }
                            if (wrapped) {
                                throw raiseNode.raise(PythonBuiltinClassType.OverflowError, ErrorMessages.INDEX_EXCEEDS_INT);
                            } else {
                                return i;
                            }
                        case PY_ITERSEARCH_CONTAINS:
                            if (CompilerDirectives.hasNextTier()) {
                                LoopNode.reportLoopCount(this, wrapped ? Integer.MAX_VALUE : i + 1);
                            }
                            return 1;
                    }
                }
            } catch (PException e) {
                e.expectStopIteration(inliningTarget, stopIterationProfile);
                if (CompilerDirectives.hasNextTier()) {
                    LoopNode.reportLoopCount(this, wrapped ? Integer.MAX_VALUE : i + 1);
                }
                if (opProfile.profile(inliningTarget, operation) == PY_ITERSEARCH_INDEX) {
                    throw raiseNode.raise(PythonBuiltinClassType.ValueError, ErrorMessages.X_NOT_IN_SEQUENCE);
                }
                return n;
            }
            if (opProfile.profile(inliningTarget, operation) == PY_ITERSEARCH_INDEX && i == Integer.MAX_VALUE) {
                wrapped = true;
            }
            i++;
        }
    }
}
