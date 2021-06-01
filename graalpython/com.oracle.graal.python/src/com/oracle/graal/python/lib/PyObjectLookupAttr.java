/*
 * Copyright (c) 2021, Oracle and/or its affiliates. All rights reserved.
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
import com.oracle.graal.python.nodes.attributes.GetAttributeNode.GetFixedAttributeNode;
import com.oracle.graal.python.nodes.call.special.CallBinaryMethodNode;
import com.oracle.graal.python.nodes.call.special.LookupSpecialMethodSlotNode;
import com.oracle.graal.python.nodes.object.GetClassNode;
import com.oracle.graal.python.nodes.object.IsBuiltinClassProfile;
import com.oracle.graal.python.runtime.exception.PException;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;

/**
 * Equivalent to use for the various PyObject_LookupAttr* functions available in CPython. Note that
 * these functions clear the exception if it's an attribute error. This node does the same, only
 * raising non AttributeError exceptions.
 *
 * Similar to the CPython equivalent, this node returns {@code PNone.NO_VALUE} when the attribute
 * doesn't exist.
 */
@GenerateUncached
@ImportStatic(SpecialMethodSlot.class)
public abstract class PyObjectLookupAttr extends Node {
    public abstract Object execute(Frame frame, Object receiver, Object name);

    @Specialization(guards = "name == cachedName", limit = "1")
    static Object getFixedAttr(VirtualFrame frame, Object receiver, @SuppressWarnings("unused") String name,
                    @SuppressWarnings("unused") @Cached("name") String cachedName,
                    @Cached("create(name)") GetFixedAttributeNode getAttrNode,
                    @Cached IsBuiltinClassProfile errorProfile) {
        try {
            return getAttrNode.execute(frame, receiver);
        } catch (PException e) {
            e.expect(PythonBuiltinClassType.AttributeError, errorProfile);
            return PNone.NO_VALUE;
        }
    }

    @Specialization(replaces = "getFixedAttr")
    static Object getDynamicAttr(Frame frame, Object receiver, Object name,
                    @Cached GetClassNode getClass,
                    @Cached(parameters = "GetAttribute") LookupSpecialMethodSlotNode lookupGetattribute,
                    @Cached(parameters = "GetAttr") LookupSpecialMethodSlotNode lookupGetattr,
                    @Cached CallBinaryMethodNode callGetattribute,
                    @Cached CallBinaryMethodNode callGetattr,
                    @Cached IsBuiltinClassProfile errorProfile) {
        Object type = getClass.execute(receiver);
        Object getattribute = lookupGetattribute.execute(frame, type, receiver);
        try {
            return callGetattribute.executeObject(frame, getattribute, receiver, name);
        } catch (PException e) {
            e.expect(PythonBuiltinClassType.AttributeError, errorProfile);
        }
        Object getattr = lookupGetattr.execute(frame, type, receiver);
        if (getattr != PNone.NO_VALUE) {
            try {
                return callGetattr.executeObject(frame, getattr, receiver, name);
            } catch (PException e) {
                e.expect(PythonBuiltinClassType.AttributeError, errorProfile);
            }
        }
        return PNone.NO_VALUE;
    }
}
