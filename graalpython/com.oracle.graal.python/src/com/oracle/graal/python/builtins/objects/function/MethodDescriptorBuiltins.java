/*
 * Copyright (c) 2022, 2024, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.python.builtins.objects.function;

import static com.oracle.graal.python.nodes.SpecialMethodNames.J___REPR__;

import java.util.List;

import com.oracle.graal.python.annotations.Slot;
import com.oracle.graal.python.annotations.Slot.SlotKind;
import com.oracle.graal.python.builtins.Builtin;
import com.oracle.graal.python.builtins.CoreFunctions;
import com.oracle.graal.python.builtins.PythonBuiltinClassType;
import com.oracle.graal.python.builtins.PythonBuiltins;
import com.oracle.graal.python.builtins.objects.PNone;
import com.oracle.graal.python.builtins.objects.method.PBuiltinMethod;
import com.oracle.graal.python.builtins.objects.method.PMethod;
import com.oracle.graal.python.builtins.objects.str.StringUtils;
import com.oracle.graal.python.builtins.objects.type.TpSlots;
import com.oracle.graal.python.builtins.objects.type.TypeNodes;
import com.oracle.graal.python.nodes.function.PythonBuiltinBaseNode;
import com.oracle.graal.python.nodes.function.builtins.PythonTernaryBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonUnaryBuiltinNode;
import com.oracle.graal.python.nodes.truffle.PythonArithmeticTypes;
import com.oracle.graal.python.runtime.object.PythonObjectFactory;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Cached.Shared;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.strings.TruffleString;

@CoreFunctions(extendClasses = PythonBuiltinClassType.PBuiltinFunction)
public final class MethodDescriptorBuiltins extends PythonBuiltins {
    public static final TpSlots SLOTS = MethodDescriptorBuiltinsSlotsGen.SLOTS;

    @Override
    protected List<? extends NodeFactory<? extends PythonBuiltinBaseNode>> getNodeFactories() {
        return MethodDescriptorBuiltinsFactory.getFactories();
    }

    @Slot(SlotKind.tp_descr_get)
    @GenerateUncached
    @GenerateNodeFactory
    @SuppressWarnings("unused")
    public abstract static class GetNode extends PythonTernaryBuiltinNode {
        @Specialization(guards = {"!isPNone(instance)"})
        static PMethod doMethod(PFunction self, Object instance, Object klass,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createMethod(instance, self);
        }

        @Specialization
        static Object doFunction(PFunction self, PNone instance, Object klass) {
            return self;
        }

        @Specialization(guards = {"!isPNone(instance)", "!self.needsDeclaringType()"})
        static PBuiltinMethod doBuiltinMethod(PBuiltinFunction self, Object instance, Object klass,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createBuiltinMethod(instance, self);
        }

        @Specialization(guards = {"!isPNone(instance)", "self.needsDeclaringType()"})
        static PBuiltinMethod doBuiltinMethodWithDeclaringClass(PBuiltinFunction self, Object instance, Object klass,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createBuiltinMethod(instance, self, self.getEnclosingType());
        }

        @Specialization
        static Object doBuiltinFunction(PBuiltinFunction self, PNone instance, Object klass) {
            return self;
        }
    }

    @Builtin(name = J___REPR__, minNumOfPositionalArgs = 1)
    @TypeSystemReference(PythonArithmeticTypes.class)
    @GenerateNodeFactory
    abstract static class ReprNode extends PythonUnaryBuiltinNode {
        @Specialization(guards = "self.getEnclosingType() == null")
        static TruffleString reprModuleFunction(PBuiltinFunction self,
                        @Cached.Shared("formatter") @Cached StringUtils.SimpleTruffleStringFormatNode simpleTruffleStringFormatNode) {
            // (tfel): these really shouldn't be accessible, I think
            return simpleTruffleStringFormatNode.format("<built-in function %s>", self.getName());
        }

        @Specialization(guards = "self.getEnclosingType() != null")
        static TruffleString reprClassFunction(PBuiltinFunction self,
                        @Cached.Shared("formatter") @Cached StringUtils.SimpleTruffleStringFormatNode simpleTruffleStringFormatNode) {
            return simpleTruffleStringFormatNode.format("<method '%s' of '%s' objects>", self.getName(), TypeNodes.GetNameNode.doSlowPath(self.getEnclosingType()));
        }
    }
}
