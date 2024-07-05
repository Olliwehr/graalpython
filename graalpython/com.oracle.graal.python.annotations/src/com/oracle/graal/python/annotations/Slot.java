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
package com.oracle.graal.python.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Slot.Slots.class)
public @interface Slot {
    SlotKind value();

    /**
     * The slot node either needs frame for execution (e.g., may call Python code) and/or is complex
     * enough that indirect call to partially evaluated code is preferred over uncached execution
     * without frame and without setting up indirect call context.
     * 
     * The slot call nodes AST inline slot nodes, but if the inline cache overflows or in the
     * uncached case, they need to either call uncached slot node or do indirect call if
     * {@code isComplex} is {@code true}.
     */
    boolean isComplex() default false;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Slots {
        Slot[] value();
    }

    /**
     * Some slots do not have fixed signature. This annotation must be used for such slots, the
     * semantics of its fields is the same as of the field with the same name in the
     * {@code @Builtin} annotation.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @interface SlotSignature {
        int minNumOfPositionalArgs() default 0;

        boolean takesVarArgs() default false;

        boolean takesVarKeywordArgs() default false;

        String[] parameterNames() default {};

        boolean needsFrame() default false;

        boolean alwaysNeedsCallerFrame() default false;

        String raiseErrorName() default "";
    }

    enum SlotKind {
        nb_bool,
        sq_length,
        sq_item,
        mp_length,
        mp_subscript,
        tp_descr_get,
        tp_descr_set,
        tp_getattro,
        tp_setattro,
    }
}
