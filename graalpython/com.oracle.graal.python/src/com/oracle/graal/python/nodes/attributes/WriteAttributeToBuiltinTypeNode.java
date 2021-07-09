/*
 * Copyright (c) 2017, 2021, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.python.nodes.attributes;

import com.oracle.graal.python.PythonLanguage;
import com.oracle.graal.python.builtins.PythonBuiltinClassType;
import com.oracle.graal.python.builtins.objects.common.HashingStorageLibrary;
import com.oracle.graal.python.builtins.objects.object.PythonObject;
import com.oracle.graal.python.builtins.objects.object.PythonObjectLibrary;
import com.oracle.graal.python.builtins.objects.type.PythonBuiltinClass;
import com.oracle.graal.python.builtins.objects.type.PythonManagedClass;
import com.oracle.graal.python.runtime.PythonContext;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.CachedContext;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.profiles.BranchProfile;

/**
 * Variant of {@link WriteAttributeToObjectNode} that allows to set attributes of builtin types
 * after initialization has finished. This node does not call
 * {@link PythonManagedClass#onAttributeUpdate(Object, Object)} and should be used with caution,
 * because it does not invalidate any assumptions or caches related to MRO lookups.
 */
@GenerateUncached
public abstract class WriteAttributeToBuiltinTypeNode extends ObjectAttributeNode {

    public abstract void execute(Object primary, String key, Object value);

    protected static boolean isAttrWritable(PythonBuiltinClass self) {
        return (self.getShape().getFlags() & PythonObject.HAS_SLOTS_BUT_NO_DICT_FLAG) == 0;
    }

    @Specialization(guards = {"isAttrWritable(klass)", "!lib.hasDict(klass)"}, limit = "1")
    static void writeToDynamicStorage(PythonBuiltinClass klass, String key, Object value,
                    @CachedLibrary("klass") @SuppressWarnings("unused") PythonObjectLibrary lib,
                    @CachedLibrary(limit = "getAttributeAccessInlineCacheMaxDepth()") DynamicObjectLibrary dylib) {
        dylib.put(klass, key, value);
    }

    @Specialization(guards = "lib.hasDict(klass)", limit = "1")
    static void writeToDictNoType(PythonBuiltinClass klass, String key, Object value,
                    @CachedLibrary("klass") PythonObjectLibrary lib,
                    @Cached BranchProfile updateStorage,
                    @CachedLibrary(limit = "1") HashingStorageLibrary hlib) {
        WriteAttributeToObjectNode.writeToDict(lib.getDict(klass), key, value, updateStorage, hlib);
    }

    @Specialization
    static void doPBCT(PythonBuiltinClassType object, String key, Object value,
                    @CachedContext(PythonLanguage.class) ContextReference<PythonContext> contextRef,
                    @Cached WriteAttributeToBuiltinTypeNode recursive) {
        recursive.execute(contextRef.get().getCore().lookupType(object), key, value);
    }
}