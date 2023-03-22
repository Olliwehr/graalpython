/*
 * Copyright (c) 2021, 2023, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.python.builtins.modules.cext;

import static com.oracle.graal.python.builtins.modules.cext.PythonCextBuiltins.CApiCallPath.Direct;
import static com.oracle.graal.python.builtins.modules.cext.PythonCextBuiltins.CApiCallPath.Ignored;
import static com.oracle.graal.python.builtins.objects.cext.capi.transitions.ArgDescriptor.Int;
import static com.oracle.graal.python.builtins.objects.cext.capi.transitions.ArgDescriptor.PY_GIL_STATE_STATE;
import static com.oracle.graal.python.builtins.objects.cext.capi.transitions.ArgDescriptor.PyObjectBorrowed;
import static com.oracle.graal.python.builtins.objects.cext.capi.transitions.ArgDescriptor.PyThreadState;
import static com.oracle.graal.python.builtins.objects.cext.capi.transitions.ArgDescriptor.Py_ssize_t;
import static com.oracle.graal.python.builtins.objects.cext.capi.transitions.ArgDescriptor.Void;

import com.oracle.graal.python.builtins.modules.cext.PythonCextBuiltins.CApiBuiltin;
import com.oracle.graal.python.builtins.modules.cext.PythonCextBuiltins.CApiNullaryBuiltinNode;
import com.oracle.graal.python.builtins.modules.cext.PythonCextBuiltins.CApiUnaryBuiltinNode;
import com.oracle.graal.python.builtins.objects.cext.capi.PThreadState;
import com.oracle.graal.python.builtins.objects.dict.PDict;
import com.oracle.graal.python.builtins.objects.ints.PInt;
import com.oracle.graal.python.nodes.util.CannotCastException;
import com.oracle.graal.python.runtime.GilNode;
import com.oracle.graal.python.runtime.PythonContext;
import com.oracle.graal.python.runtime.object.PythonObjectFactory;
import com.oracle.graal.python.util.OverflowException;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public final class PythonCextPyStateBuiltins {

    @CApiBuiltin(ret = PY_GIL_STATE_STATE, args = {}, acquiresGIL = false, call = Direct)
    abstract static class PyGILState_Ensure extends CApiNullaryBuiltinNode {

        @Specialization
        static Object save(@Cached GilNode gil) {
            // TODO allow acquiring from foreign thread
            boolean acquired = gil.acquire();
            return acquired ? 1 : 0;
        }
    }

    @CApiBuiltin(ret = Int, args = {}, acquiresGIL = false, call = Direct)
    abstract static class PyGILState_Check extends CApiNullaryBuiltinNode {

        @Specialization
        Object check() {
            return PythonContext.get(this).ownsGil() ? 1 : 0;
        }
    }

    @CApiBuiltin(ret = Void, args = {PY_GIL_STATE_STATE}, acquiresGIL = false, call = Direct)
    abstract static class PyGILState_Release extends CApiUnaryBuiltinNode {

        @Specialization
        static Object restore(int state,
                        @Cached GilNode gil) {
            gil.release(state == 1);
            return 0;
        }
    }

    @CApiBuiltin(ret = PyThreadState, args = {}, call = Direct, inlined = true)
    abstract static class PyThreadState_Get extends CApiNullaryBuiltinNode {

        @Specialization
        PThreadState get() {
            return PThreadState.getThreadState(getLanguage(), getContext());
        }
    }

    @CApiBuiltin(ret = PyObjectBorrowed, args = {}, call = Direct)
    abstract static class PyThreadState_GetDict extends CApiNullaryBuiltinNode {

        @Specialization
        @TruffleBoundary
        PDict get(@Cached PythonObjectFactory factory) {

            PThreadState state = PThreadState.getThreadState(getLanguage(), getContext());
            PDict threadStateDict = state.getThreadState().getDict();
            if (threadStateDict == null) {
                threadStateDict = factory.createDict();
                state.getThreadState().setDict(threadStateDict);
            }
            return threadStateDict;
        }
    }

    @CApiBuiltin(ret = PyObjectBorrowed, args = {Py_ssize_t}, call = Ignored)
    abstract static class PyTruffleState_FindModule extends CApiUnaryBuiltinNode {

        @Specialization
        Object doGeneric(long mIndex) {
            try {
                int i = PInt.intValueExact(mIndex);
                Object result = getCApiContext().getModuleByIndex(i);
                if (result == null) {
                    return getNativeNull();
                }
                return result;
            } catch (CannotCastException | OverflowException e) {
                return getNativeNull();
            }
        }
    }
}
