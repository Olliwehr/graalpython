/*
 * Copyright (c) 2024, 2024, Oracle and/or its affiliates. All rights reserved.
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

import static com.oracle.graal.python.lib.PyObjectLookupAttr.readAttributeQuickly;

import com.oracle.graal.python.builtins.PythonBuiltinClassType;
import com.oracle.graal.python.builtins.objects.PNone;
import com.oracle.graal.python.builtins.objects.type.TpSlots;
import com.oracle.graal.python.builtins.objects.type.TpSlots.GetCachedTpSlotsNode;
import com.oracle.graal.python.builtins.objects.type.slots.TpSlotGetAttr.CallSlotGetAttrONode;
import com.oracle.graal.python.nodes.object.BuiltinClassProfiles.IsBuiltinObjectProfile;
import com.oracle.graal.python.nodes.object.GetClassNode;
import com.oracle.graal.python.runtime.exception.PException;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateCached;
import com.oracle.truffle.api.dsl.GenerateInline;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.strings.TruffleString;

/**
 * Like {@link PyObjectLookupAttr}, but accepts any object as a key.
 */
@GenerateUncached
@GenerateInline
@GenerateCached(false)
public abstract class PyObjectLookupAttrO extends Node {
    public static Object executeUncached(Object type, Object name) {
        return PyObjectLookupAttrONodeGen.getUncached().execute(null, null, type, name);
    }

    public abstract Object execute(Frame frame, Node inliningTarget, Object receiver, Object name);

    @Specialization
    static Object getDynamicAttr(Frame frame, Node inliningTarget, Object receiver, Object name,
                    @Cached GetClassNode getClass,
                    @Cached GetCachedTpSlotsNode getSlotsNode,
                    @Cached CallSlotGetAttrONode callGetattribute,
                    @Cached IsBuiltinObjectProfile errorProfile,
                    @Cached(inline = false) TruffleString.CodePointLengthNode codePointLengthNode,
                    @Cached(inline = false) TruffleString.CodePointAtIndexNode codePointAtIndexNode) {
        Object type = getClass.execute(inliningTarget, receiver);
        TpSlots slots = getSlotsNode.execute(inliningTarget, type);
        if (!codePointLengthNode.isAdoptable() && name instanceof TruffleString tsName) {
            // It pays to try this in the uncached case, avoiding a full call to tp_getattr(o)
            Object result = readAttributeQuickly(type, slots, receiver, tsName, codePointLengthNode, codePointAtIndexNode);
            if (result != null) {
                return result;
            }
            // Otherwise fallback to tp_getattr(o)
        }
        try {
            return callGetattribute.execute((VirtualFrame) frame, inliningTarget, slots, receiver, name);
        } catch (PException e) {
            e.expect(inliningTarget, PythonBuiltinClassType.AttributeError, errorProfile);
        }
        return PNone.NO_VALUE;
    }
}
