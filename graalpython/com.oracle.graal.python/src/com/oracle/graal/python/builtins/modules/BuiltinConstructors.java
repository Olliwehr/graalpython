/*
 * Copyright (c) 2017, 2024, Oracle and/or its affiliates.
 * Copyright (c) 2013, Regents of the University of California
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.oracle.graal.python.builtins.modules;

import static com.oracle.graal.python.builtins.objects.cext.capi.NativeCAPISymbol.FUN_BYTES_SUBTYPE_NEW;
import static com.oracle.graal.python.builtins.objects.cext.capi.NativeCAPISymbol.FUN_PY_OBJECT_NEW;
import static com.oracle.graal.python.nodes.BuiltinNames.J_BOOL;
import static com.oracle.graal.python.nodes.BuiltinNames.J_BYTEARRAY;
import static com.oracle.graal.python.nodes.BuiltinNames.J_BYTES;
import static com.oracle.graal.python.nodes.BuiltinNames.J_CLASSMETHOD;
import static com.oracle.graal.python.nodes.BuiltinNames.J_COMPLEX;
import static com.oracle.graal.python.nodes.BuiltinNames.J_DICT;
import static com.oracle.graal.python.nodes.BuiltinNames.J_DICT_ITEMITERATOR;
import static com.oracle.graal.python.nodes.BuiltinNames.J_DICT_ITEMS;
import static com.oracle.graal.python.nodes.BuiltinNames.J_DICT_KEYITERATOR;
import static com.oracle.graal.python.nodes.BuiltinNames.J_DICT_KEYS;
import static com.oracle.graal.python.nodes.BuiltinNames.J_DICT_VALUEITERATOR;
import static com.oracle.graal.python.nodes.BuiltinNames.J_DICT_VALUES;
import static com.oracle.graal.python.nodes.BuiltinNames.J_ENUMERATE;
import static com.oracle.graal.python.nodes.BuiltinNames.J_FLOAT;
import static com.oracle.graal.python.nodes.BuiltinNames.J_FROZENSET;
import static com.oracle.graal.python.nodes.BuiltinNames.J_GETSET_DESCRIPTOR;
import static com.oracle.graal.python.nodes.BuiltinNames.J_INSTANCEMETHOD;
import static com.oracle.graal.python.nodes.BuiltinNames.J_INT;
import static com.oracle.graal.python.nodes.BuiltinNames.J_LIST;
import static com.oracle.graal.python.nodes.BuiltinNames.J_MAP;
import static com.oracle.graal.python.nodes.BuiltinNames.J_MEMBER_DESCRIPTOR;
import static com.oracle.graal.python.nodes.BuiltinNames.J_MEMORYVIEW;
import static com.oracle.graal.python.nodes.BuiltinNames.J_MODULE;
import static com.oracle.graal.python.nodes.BuiltinNames.J_OBJECT;
import static com.oracle.graal.python.nodes.BuiltinNames.J_PROPERTY;
import static com.oracle.graal.python.nodes.BuiltinNames.J_RANGE;
import static com.oracle.graal.python.nodes.BuiltinNames.J_REVERSED;
import static com.oracle.graal.python.nodes.BuiltinNames.J_SET;
import static com.oracle.graal.python.nodes.BuiltinNames.J_STATICMETHOD;
import static com.oracle.graal.python.nodes.BuiltinNames.J_STR;
import static com.oracle.graal.python.nodes.BuiltinNames.J_SUPER;
import static com.oracle.graal.python.nodes.BuiltinNames.J_TUPLE;
import static com.oracle.graal.python.nodes.BuiltinNames.J_TYPE;
import static com.oracle.graal.python.nodes.BuiltinNames.J_WRAPPER_DESCRIPTOR;
import static com.oracle.graal.python.nodes.BuiltinNames.J_ZIP;
import static com.oracle.graal.python.nodes.BuiltinNames.T_EXCEPTION_GROUP;
import static com.oracle.graal.python.nodes.BuiltinNames.T_GETSET_DESCRIPTOR;
import static com.oracle.graal.python.nodes.BuiltinNames.T_LAMBDA_NAME;
import static com.oracle.graal.python.nodes.BuiltinNames.T_MEMBER_DESCRIPTOR;
import static com.oracle.graal.python.nodes.BuiltinNames.T_NOT_IMPLEMENTED;
import static com.oracle.graal.python.nodes.BuiltinNames.T_WRAPPER_DESCRIPTOR;
import static com.oracle.graal.python.nodes.BuiltinNames.T_ZIP;
import static com.oracle.graal.python.nodes.ErrorMessages.ARG_MUST_NOT_BE_ZERO;
import static com.oracle.graal.python.nodes.PGuards.isInteger;
import static com.oracle.graal.python.nodes.PGuards.isNoValue;
import static com.oracle.graal.python.nodes.SpecialAttributeNames.T___ABSTRACTMETHODS__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.J___INDEX__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.J___TRUNC__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.T_JOIN;
import static com.oracle.graal.python.nodes.SpecialMethodNames.T_SORT;
import static com.oracle.graal.python.nodes.SpecialMethodNames.T___BYTES__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.T___COMPLEX__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.T___INT__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.T___MRO_ENTRIES__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.T___TRUNC__;
import static com.oracle.graal.python.nodes.StringLiterals.T_COMMA_SPACE;
import static com.oracle.graal.python.nodes.StringLiterals.T_EMPTY_STRING;
import static com.oracle.graal.python.nodes.StringLiterals.T_STRICT;
import static com.oracle.graal.python.nodes.StringLiterals.T_UTF8;
import static com.oracle.graal.python.nodes.truffle.TruffleStringMigrationHelpers.assertNoJavaString;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.DeprecationWarning;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.NotImplementedError;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.OverflowError;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.RuntimeError;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.TypeError;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.ValueError;
import static com.oracle.graal.python.util.PythonUtils.TS_ENCODING;
import static com.oracle.graal.python.util.PythonUtils.addExact;
import static com.oracle.graal.python.util.PythonUtils.multiplyExact;
import static com.oracle.graal.python.util.PythonUtils.negateExact;
import static com.oracle.graal.python.util.PythonUtils.objectArrayToTruffleStringArray;
import static com.oracle.graal.python.util.PythonUtils.subtractExact;

import java.math.BigInteger;
import java.util.List;

import com.oracle.graal.python.annotations.ArgumentClinic;
import com.oracle.graal.python.builtins.Builtin;
import com.oracle.graal.python.builtins.CoreFunctions;
import com.oracle.graal.python.builtins.Python3Core;
import com.oracle.graal.python.builtins.PythonBuiltinClassType;
import com.oracle.graal.python.builtins.PythonBuiltins;
import com.oracle.graal.python.builtins.modules.WarningsModuleBuiltins.WarnNode;
import com.oracle.graal.python.builtins.modules.BuiltinConstructorsFactory.FloatNodeFactory.NonPrimitiveFloatNodeGen;
import com.oracle.graal.python.builtins.modules.BuiltinConstructorsFactory.ObjectNodeFactory.ReportAbstractClassNodeGen;
import com.oracle.graal.python.builtins.objects.PNone;
import com.oracle.graal.python.builtins.objects.PNotImplemented;
import com.oracle.graal.python.builtins.objects.buffer.PythonBufferAccessLibrary;
import com.oracle.graal.python.builtins.objects.buffer.PythonBufferAcquireLibrary;
import com.oracle.graal.python.builtins.objects.bytes.BytesNodes;
import com.oracle.graal.python.builtins.objects.bytes.PByteArray;
import com.oracle.graal.python.builtins.objects.bytes.PBytes;
import com.oracle.graal.python.builtins.objects.bytes.PBytesLike;
import com.oracle.graal.python.builtins.objects.cell.PCell;
import com.oracle.graal.python.builtins.objects.cext.PythonAbstractNativeObject;
import com.oracle.graal.python.builtins.objects.cext.PythonNativeVoidPtr;
import com.oracle.graal.python.builtins.objects.cext.capi.CExtNodes;
import com.oracle.graal.python.builtins.objects.cext.capi.CExtNodes.PCallCapiFunction;
import com.oracle.graal.python.builtins.objects.cext.capi.ExternalFunctionNodes;
import com.oracle.graal.python.builtins.objects.cext.capi.NativeCAPISymbol;
import com.oracle.graal.python.builtins.objects.cext.capi.transitions.CApiTransitions.NativeToPythonTransferNode;
import com.oracle.graal.python.builtins.objects.cext.capi.transitions.CApiTransitions.PythonToNativeNode;
import com.oracle.graal.python.builtins.objects.cext.common.CArrayWrappers.CByteArrayWrapper;
import com.oracle.graal.python.builtins.objects.code.CodeNodes;
import com.oracle.graal.python.builtins.objects.code.PCode;
import com.oracle.graal.python.builtins.objects.common.HashingCollectionNodes;
import com.oracle.graal.python.builtins.objects.common.HashingStorage;
import com.oracle.graal.python.builtins.objects.common.SequenceNodes.GetObjectArrayNode;
import com.oracle.graal.python.builtins.objects.common.SequenceStorageNodes;
import com.oracle.graal.python.builtins.objects.complex.PComplex;
import com.oracle.graal.python.builtins.objects.dict.PDict;
import com.oracle.graal.python.builtins.objects.ellipsis.PEllipsis;
import com.oracle.graal.python.builtins.objects.enumerate.PEnumerate;
import com.oracle.graal.python.builtins.objects.floats.FloatUtils;
import com.oracle.graal.python.builtins.objects.floats.PFloat;
import com.oracle.graal.python.builtins.objects.frame.PFrame;
import com.oracle.graal.python.builtins.objects.function.PBuiltinFunction;
import com.oracle.graal.python.builtins.objects.function.PFunction;
import com.oracle.graal.python.builtins.objects.function.PKeyword;
import com.oracle.graal.python.builtins.objects.ints.PInt;
import com.oracle.graal.python.builtins.objects.iterator.PBigRangeIterator;
import com.oracle.graal.python.builtins.objects.iterator.PZip;
import com.oracle.graal.python.builtins.objects.list.PList;
import com.oracle.graal.python.builtins.objects.map.PMap;
import com.oracle.graal.python.builtins.objects.memoryview.PMemoryView;
import com.oracle.graal.python.builtins.objects.method.PBuiltinMethod;
import com.oracle.graal.python.builtins.objects.namespace.PSimpleNamespace;
import com.oracle.graal.python.builtins.objects.object.ObjectBuiltinsFactory;
import com.oracle.graal.python.builtins.objects.object.PythonObject;
import com.oracle.graal.python.builtins.objects.property.PProperty;
import com.oracle.graal.python.builtins.objects.range.PBigRange;
import com.oracle.graal.python.builtins.objects.range.PIntRange;
import com.oracle.graal.python.builtins.objects.range.RangeNodes;
import com.oracle.graal.python.builtins.objects.range.RangeNodes.LenOfIntRangeNodeExact;
import com.oracle.graal.python.builtins.objects.set.PFrozenSet;
import com.oracle.graal.python.builtins.objects.set.PSet;
import com.oracle.graal.python.builtins.objects.str.PString;
import com.oracle.graal.python.builtins.objects.traceback.PTraceback;
import com.oracle.graal.python.builtins.objects.tuple.PTuple;
import com.oracle.graal.python.builtins.objects.type.PythonBuiltinClass;
import com.oracle.graal.python.builtins.objects.type.PythonManagedClass;
import com.oracle.graal.python.builtins.objects.type.SpecialMethodSlot;
import com.oracle.graal.python.builtins.objects.type.TypeBuiltins;
import com.oracle.graal.python.builtins.objects.type.TypeFlags;
import com.oracle.graal.python.builtins.objects.type.TypeNodes;
import com.oracle.graal.python.builtins.objects.type.TypeNodes.CreateTypeNode;
import com.oracle.graal.python.builtins.objects.type.TypeNodes.IsAcceptableBaseNode;
import com.oracle.graal.python.builtins.objects.type.TypeNodes.IsTypeNode;
import com.oracle.graal.python.builtins.objects.type.TypeNodes.NeedsNativeAllocationNode;
import com.oracle.graal.python.builtins.objects.types.PGenericAlias;
import com.oracle.graal.python.lib.CanBeDoubleNode;
import com.oracle.graal.python.lib.PyBytesCheckNode;
import com.oracle.graal.python.lib.PyCallableCheckNode;
import com.oracle.graal.python.lib.PyComplexCheckExactNode;
import com.oracle.graal.python.lib.PyFloatAsDoubleNode;
import com.oracle.graal.python.lib.PyFloatFromString;
import com.oracle.graal.python.lib.PyIndexCheckNode;
import com.oracle.graal.python.lib.PyLongCheckExactNode;
import com.oracle.graal.python.lib.PyLongFromDoubleNode;
import com.oracle.graal.python.lib.PyMappingCheckNode;
import com.oracle.graal.python.lib.PyMemoryViewFromObject;
import com.oracle.graal.python.lib.PyNumberAsSizeNode;
import com.oracle.graal.python.lib.PyNumberFloatNode;
import com.oracle.graal.python.lib.PyNumberIndexNode;
import com.oracle.graal.python.lib.PyObjectCallMethodObjArgs;
import com.oracle.graal.python.lib.PyObjectGetAttr;
import com.oracle.graal.python.lib.PyObjectGetIter;
import com.oracle.graal.python.lib.PyObjectIsTrueNode;
import com.oracle.graal.python.lib.PyObjectLookupAttr;
import com.oracle.graal.python.lib.PyObjectSizeNode;
import com.oracle.graal.python.lib.PyObjectStrAsObjectNode;
import com.oracle.graal.python.lib.PySequenceCheckNode;
import com.oracle.graal.python.lib.PySequenceSizeNode;
import com.oracle.graal.python.lib.PySliceNew;
import com.oracle.graal.python.lib.PyUnicodeCheckExactNode;
import com.oracle.graal.python.nodes.BuiltinNames;
import com.oracle.graal.python.nodes.ErrorMessages;
import com.oracle.graal.python.nodes.PGuards;
import com.oracle.graal.python.nodes.PNodeWithContext;
import com.oracle.graal.python.nodes.PRaiseNode;
import com.oracle.graal.python.nodes.attributes.LookupCallableSlotInMRONode;
import com.oracle.graal.python.nodes.attributes.ReadAttributeFromObjectNode;
import com.oracle.graal.python.nodes.builtins.ListNodes;
import com.oracle.graal.python.nodes.builtins.TupleNodes;
import com.oracle.graal.python.nodes.call.CallNode;
import com.oracle.graal.python.nodes.call.special.CallUnaryMethodNode;
import com.oracle.graal.python.nodes.call.special.LookupAndCallTernaryNode;
import com.oracle.graal.python.nodes.call.special.LookupAndCallUnaryNode;
import com.oracle.graal.python.nodes.call.special.LookupSpecialMethodSlotNode;
import com.oracle.graal.python.nodes.classes.IsSubtypeNode;
import com.oracle.graal.python.nodes.function.PythonBuiltinBaseNode;
import com.oracle.graal.python.nodes.function.PythonBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonBinaryBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonClinicBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonQuaternaryBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonQuaternaryClinicBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonTernaryBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonVarargsBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.clinic.ArgumentClinicProvider;
import com.oracle.graal.python.nodes.object.BuiltinClassProfiles;
import com.oracle.graal.python.nodes.object.BuiltinClassProfiles.IsAnyBuiltinClassProfile;
import com.oracle.graal.python.nodes.object.BuiltinClassProfiles.IsBuiltinClassExactProfile;
import com.oracle.graal.python.nodes.object.BuiltinClassProfiles.IsBuiltinObjectProfile;
import com.oracle.graal.python.nodes.object.GetClassNode;
import com.oracle.graal.python.nodes.util.CannotCastException;
import com.oracle.graal.python.nodes.util.CastToJavaIntExactNode;
import com.oracle.graal.python.nodes.util.CastToJavaStringNode;
import com.oracle.graal.python.nodes.util.CastToTruffleStringNode;
import com.oracle.graal.python.nodes.util.SplitArgsNode;
import com.oracle.graal.python.runtime.IndirectCallData;
import com.oracle.graal.python.runtime.PythonContext;
import com.oracle.graal.python.runtime.exception.PException;
import com.oracle.graal.python.runtime.object.PythonObjectFactory;
import com.oracle.graal.python.runtime.object.PythonObjectSlowPathFactory;
import com.oracle.graal.python.util.OverflowException;
import com.oracle.graal.python.util.PythonUtils;
import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.HostCompilerDirectives.InliningCutoff;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Cached.Exclusive;
import com.oracle.truffle.api.dsl.Cached.Shared;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.GenerateCached;
import com.oracle.truffle.api.dsl.GenerateInline;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.NeverDefault;
import com.oracle.truffle.api.dsl.ReportPolymorphism;
import com.oracle.truffle.api.dsl.ReportPolymorphism.Megamorphic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.LoopNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.profiles.InlinedBranchProfile;
import com.oracle.truffle.api.profiles.InlinedConditionProfile;
import com.oracle.truffle.api.profiles.InlinedLoopConditionProfile;
import com.oracle.truffle.api.strings.TruffleString;

@CoreFunctions(defineModule = BuiltinNames.J_BUILTINS, isEager = true)
public final class BuiltinConstructors extends PythonBuiltins {

    @Override
    protected List<com.oracle.truffle.api.dsl.NodeFactory<? extends PythonBuiltinBaseNode>> getNodeFactories() {
        return BuiltinConstructorsFactory.getFactories();
    }

    @Override
    public void initialize(Python3Core core) {
        super.initialize(core);
        addBuiltinConstant(T_NOT_IMPLEMENTED, PNotImplemented.NOT_IMPLEMENTED);
    }

    // bytes([source[, encoding[, errors]]])
    @Builtin(name = J_BYTES, minNumOfPositionalArgs = 1, parameterNames = {"$self", "source", "encoding", "errors"}, constructsClass = PythonBuiltinClassType.PBytes, doc = """
                    bytes(iterable_of_ints) -> bytes
                    bytes(string, encoding[, errors]) -> bytes
                    bytes(bytes_or_buffer) -> immutable copy of bytes_or_buffer
                    bytes(int) -> bytes object of size given by the parameter initialized with null bytes
                    bytes() -> empty bytes object

                    Construct an immutable array of bytes from:
                      - an iterable yielding integers in range(256)
                      - a text string encoded using the specified encoding
                      - any object implementing the buffer API.
                      - an integer""")
    @ArgumentClinic(name = "encoding", conversionClass = BytesNodes.ExpectStringNode.class, args = "\"bytes()\"")
    @ArgumentClinic(name = "errors", conversionClass = BytesNodes.ExpectStringNode.class, args = "\"bytes()\"")
    @GenerateNodeFactory
    @ImportStatic(SpecialMethodSlot.class)
    public abstract static class BytesNode extends PythonQuaternaryClinicBuiltinNode {

        @Override
        protected ArgumentClinicProvider getArgumentClinic() {
            return BuiltinConstructorsClinicProviders.BytesNodeClinicProviderGen.INSTANCE;
        }

        @SuppressWarnings("unused")
        @Specialization(guards = "isNoValue(source)")
        static Object doEmpty(Object cls, PNone source, PNone encoding, PNone errors,
                        @Bind("this") Node inliningTarget,
                        @Exclusive @Cached CreateBytes createBytes) {
            return createBytes.execute(inliningTarget, cls, PythonUtils.EMPTY_BYTE_ARRAY);
        }

        @Specialization(guards = "!isNoValue(source)")
        static Object doCallBytes(VirtualFrame frame, Object cls, Object source, PNone encoding, PNone errors,
                        @Bind("this") Node inliningTarget,
                        @Cached GetClassNode getClassNode,
                        @Cached InlinedConditionProfile hasBytes,
                        @Cached("create(Bytes)") LookupSpecialMethodSlotNode lookupBytes,
                        @Cached CallUnaryMethodNode callBytes,
                        @Cached BytesNodes.ToBytesNode toBytesNode,
                        @Cached PyBytesCheckNode check,
                        @Exclusive @Cached BytesNodes.BytesInitNode bytesInitNode,
                        @Exclusive @Cached CreateBytes createBytes,
                        @Cached PRaiseNode.Lazy raiseNode) {
            Object bytesMethod = lookupBytes.execute(frame, getClassNode.execute(inliningTarget, source), source);
            if (hasBytes.profile(inliningTarget, bytesMethod != PNone.NO_VALUE)) {
                Object bytes = callBytes.executeObject(frame, bytesMethod, source);
                if (check.execute(inliningTarget, bytes)) {
                    if (cls == PythonBuiltinClassType.PBytes) {
                        return bytes;
                    } else {
                        return createBytes.execute(inliningTarget, cls, toBytesNode.execute(frame, bytes));
                    }
                } else {
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.RETURNED_NONBYTES, T___BYTES__, bytes);
                }
            }
            return createBytes.execute(inliningTarget, cls, bytesInitNode.execute(frame, inliningTarget, source, encoding, errors));
        }

        @Specialization(guards = {"isNoValue(source) || (!isNoValue(encoding) || !isNoValue(errors))"})
        static Object dontCallBytes(VirtualFrame frame, Object cls, Object source, Object encoding, Object errors,
                        @Bind("this") Node inliningTarget,
                        @Exclusive @Cached BytesNodes.BytesInitNode bytesInitNode,
                        @Exclusive @Cached CreateBytes createBytes) {
            return createBytes.execute(inliningTarget, cls, bytesInitNode.execute(frame, inliningTarget, source, encoding, errors));
        }

        @GenerateInline
        @GenerateCached(false)
        abstract static class CreateBytes extends PNodeWithContext {
            abstract Object execute(Node inliningTarget, Object cls, byte[] bytes);

            @Specialization(guards = "!needsNativeAllocationNode.execute(inliningTarget, cls)")
            static PBytes doManaged(@SuppressWarnings("unused") Node inliningTarget, Object cls, byte[] bytes,
                            @SuppressWarnings("unused") @Shared @Cached TypeNodes.NeedsNativeAllocationNode needsNativeAllocationNode,
                            @Cached(inline = false) PythonObjectFactory factory) {
                return factory.createBytes(cls, bytes);
            }

            @Specialization(guards = "needsNativeAllocationNode.execute(inliningTarget, cls)")
            static Object doNative(@SuppressWarnings("unused") Node inliningTarget, Object cls, byte[] bytes,
                            @SuppressWarnings("unused") @Shared @Cached TypeNodes.NeedsNativeAllocationNode needsNativeAllocationNode,
                            @Cached(inline = false) PythonToNativeNode toNative,
                            @Cached(inline = false) NativeToPythonTransferNode toPython,
                            @Cached(inline = false) PCallCapiFunction call) {
                CByteArrayWrapper wrapper = new CByteArrayWrapper(bytes);
                try {
                    return toPython.execute(call.call(FUN_BYTES_SUBTYPE_NEW, toNative.execute(cls), wrapper, bytes.length));
                } finally {
                    wrapper.free();
                }
            }
        }
    }

    @Builtin(name = J_BYTEARRAY, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PByteArray, doc = """
                    bytearray(iterable_of_ints) -> bytearray
                    bytearray(string, encoding[, errors]) -> bytearray
                    bytearray(bytes_or_buffer) -> mutable copy of bytes_or_buffer
                    bytearray(int) -> bytes array of size given by the parameter initialized with null bytes
                    bytearray() -> empty bytes array

                    Construct a mutable bytearray object from:
                      - an iterable yielding integers in range(256)
                      - a text string encoded using the specified encoding
                      - a bytes or a buffer object
                      - any object implementing the buffer API.
                      - an integer""")
    @GenerateNodeFactory
    public abstract static class ByteArrayNode extends PythonBuiltinNode {
        @Specialization
        public PByteArray setEmpty(Object cls, @SuppressWarnings("unused") Object arg,
                        @Cached PythonObjectFactory factory) {
            // data filled in subsequent __init__ call - see BytesCommonBuiltins.InitNode
            return factory.createByteArray(cls, PythonUtils.EMPTY_BYTE_ARRAY);
        }

        // TODO: native allocation?
    }

    // complex([real[, imag]])
    @Builtin(name = J_COMPLEX, minNumOfPositionalArgs = 1, constructsClass = PythonBuiltinClassType.PComplex, parameterNames = {"$cls", "real", "imag"}, doc = """
                    Create a complex number from a real part and an optional imaginary part.

                    This is equivalent to (real + imag*1j) where imag defaults to 0.""")
    @GenerateNodeFactory
    public abstract static class ComplexNode extends PythonTernaryBuiltinNode {
        @Child private LookupAndCallUnaryNode callReprNode;
        @Child private LookupAndCallUnaryNode callComplexNode;
        @Child private WarnNode warnNode;

        @GenerateInline
        @GenerateCached(false)
        @GenerateUncached
        abstract static class CreateComplexNode extends Node {
            public abstract Object execute(Node inliningTarget, Object cls, double real, double imaginary);

            public static Object executeUncached(Object cls, double real, double imaginary) {
                return BuiltinConstructorsFactory.ComplexNodeFactory.CreateComplexNodeGen.getUncached().execute(null, cls, real, imaginary);
            }

            @Specialization(guards = "!needsNativeAllocationNode.execute(inliningTarget, cls)", limit = "1")
            static PComplex doManaged(@SuppressWarnings("unused") Node inliningTarget, Object cls, double real, double imaginary,
                            @SuppressWarnings("unused") @Cached NeedsNativeAllocationNode needsNativeAllocationNode,
                            @Cached(inline = false) PythonObjectFactory factory) {
                return factory.createComplex(cls, real, imaginary);
            }

            @Fallback
            static Object doNative(Node inliningTarget, Object cls, double real, double imaginary,
                            @Cached(inline = false) PCallCapiFunction callCapiFunction,
                            @Cached(inline = false) PythonToNativeNode toNativeNode,
                            @Cached(inline = false) NativeToPythonTransferNode toPythonNode,
                            @Cached(inline = false) ExternalFunctionNodes.DefaultCheckFunctionResultNode checkFunctionResultNode) {
                NativeCAPISymbol symbol = NativeCAPISymbol.FUN_COMPLEX_SUBTYPE_FROM_DOUBLES;
                Object nativeResult = callCapiFunction.call(symbol, toNativeNode.execute(cls), real, imaginary);
                return toPythonNode.execute(checkFunctionResultNode.execute(PythonContext.get(inliningTarget), symbol.getTsName(), nativeResult));
            }
        }

        @Specialization(guards = {"isNoValue(real)", "isNoValue(imag)"})
        @SuppressWarnings("unused")
        static Object complexFromNone(Object cls, PNone real, PNone imag,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, 0, 0);
        }

        @Specialization
        static Object complexFromIntInt(Object cls, int real, int imaginary,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, real, imaginary);
        }

        @Specialization
        static Object complexFromLongLong(Object cls, long real, long imaginary,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, real, imaginary);
        }

        @Specialization
        static Object complexFromLongLong(Object cls, PInt real, PInt imaginary,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, real.doubleValueWithOverflow(inliningTarget),
                            imaginary.doubleValueWithOverflow(inliningTarget));
        }

        @Specialization
        static Object complexFromDoubleDouble(Object cls, double real, double imaginary,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, real, imaginary);
        }

        @Specialization(guards = "isNoValue(imag)")
        static Object complexFromDouble(Object cls, double real, @SuppressWarnings("unused") PNone imag,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, real, 0);
        }

        @Specialization(guards = "isNoValue(imag)")
        Object complexFromDouble(VirtualFrame frame, Object cls, PFloat real, @SuppressWarnings("unused") PNone imag,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode,
                        @Shared @Cached CanBeDoubleNode canBeDoubleNode,
                        @Shared("floatAsDouble") @Cached PyFloatAsDoubleNode asDoubleNode,
                        @Shared("isComplex") @Cached PyComplexCheckExactNode isComplexType,
                        @Shared("isComplexResult") @Cached PyComplexCheckExactNode isResultComplexType,
                        @Shared("isPrimitive") @Cached IsBuiltinClassExactProfile isPrimitiveProfile,
                        @Shared("isBuiltinObj") @Cached PyComplexCheckExactNode isBuiltinObjectProfile,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            return complexFromObject(frame, cls, real, imag, inliningTarget, createComplexNode, canBeDoubleNode, asDoubleNode, isComplexType, isResultComplexType, isPrimitiveProfile,
                            isBuiltinObjectProfile, factory,
                            raiseNode);
        }

        @Specialization(guards = "isNoValue(imag)")
        static Object complexFromInt(Object cls, int real, @SuppressWarnings("unused") PNone imag,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, real, 0);
        }

        @Specialization(guards = "isNoValue(imag)")
        static Object complexFromLong(Object cls, long real, @SuppressWarnings("unused") PNone imag,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, real, 0);
        }

        @Specialization(guards = "isNoValue(imag)")
        Object complexFromLong(VirtualFrame frame, Object cls, PInt real, @SuppressWarnings("unused") PNone imag,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode,
                        @Shared @Cached CanBeDoubleNode canBeDoubleNode,
                        @Shared("floatAsDouble") @Cached PyFloatAsDoubleNode asDoubleNode,
                        @Shared("isComplex") @Cached PyComplexCheckExactNode isComplexType,
                        @Shared("isComplexResult") @Cached PyComplexCheckExactNode isResultComplexType,
                        @Shared("isPrimitive") @Cached IsBuiltinClassExactProfile isPrimitiveProfile,
                        @Shared("isBuiltinObj") @Cached PyComplexCheckExactNode complexCheck,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            return complexFromObject(frame, cls, real, imag, inliningTarget, createComplexNode, canBeDoubleNode, asDoubleNode, isComplexType, isResultComplexType, isPrimitiveProfile, complexCheck,
                            factory, raiseNode);
        }

        @Specialization(guards = {"isNoValue(imag)", "!isNoValue(number)", "!isString(number)"})
        Object complexFromObject(VirtualFrame frame, Object cls, Object number, @SuppressWarnings("unused") PNone imag,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode,
                        @Shared @Cached CanBeDoubleNode canBeDoubleNode,
                        @Shared("floatAsDouble") @Cached PyFloatAsDoubleNode asDoubleNode,
                        @Shared("isComplex") @Cached PyComplexCheckExactNode isComplexType,
                        @Shared("isComplexResult") @Cached PyComplexCheckExactNode isResultComplexType,
                        @Shared("isPrimitive") @Cached IsBuiltinClassExactProfile isPrimitiveProfile,
                        @Shared("isBuiltinObj") @Cached PyComplexCheckExactNode complexCheck,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            PComplex value = getComplexNumberFromObject(frame, number, inliningTarget, isComplexType, isResultComplexType, raiseNode);
            if (value == null) {
                if (canBeDoubleNode.execute(inliningTarget, number)) {
                    return createComplexNode.execute(inliningTarget, cls, asDoubleNode.execute(frame, inliningTarget, number), 0.0);
                } else {
                    throw raiseFirstArgError(number, raiseNode.get(inliningTarget));
                }
            }
            if (isPrimitiveProfile.profileClass(inliningTarget, cls, PythonBuiltinClassType.PComplex)) {
                if (complexCheck.execute(inliningTarget, value)) {
                    return value;
                }
                return factory.createComplex(value.getReal(), value.getImag());
            }
            return createComplexNode.execute(inliningTarget, cls, value.getReal(), value.getImag());
        }

        @Specialization
        static Object complexFromLongComplex(Object cls, long one, PComplex two,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, one - two.getImag(), two.getReal());
        }

        @Specialization
        static Object complexFromPIntComplex(Object cls, PInt one, PComplex two,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, one.doubleValueWithOverflow(inliningTarget) - two.getImag(), two.getReal());
        }

        @Specialization
        static Object complexFromDoubleComplex(Object cls, double one, PComplex two,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode) {
            return createComplexNode.execute(inliningTarget, cls, one - two.getImag(), two.getReal());
        }

        @Specialization(guards = "!isString(one)")
        Object complexFromComplexLong(VirtualFrame frame, Object cls, Object one, long two,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode,
                        @Shared @Cached CanBeDoubleNode canBeDoubleNode,
                        @Shared("floatAsDouble") @Cached PyFloatAsDoubleNode asDoubleNode,
                        @Shared("isComplex") @Cached PyComplexCheckExactNode isComplexType,
                        @Shared("isComplexResult") @Cached PyComplexCheckExactNode isResultComplexType,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            PComplex value = getComplexNumberFromObject(frame, one, inliningTarget, isComplexType, isResultComplexType, raiseNode);
            if (value == null) {
                if (canBeDoubleNode.execute(inliningTarget, one)) {
                    return createComplexNode.execute(inliningTarget, cls, asDoubleNode.execute(frame, inliningTarget, one), two);
                } else {
                    throw raiseFirstArgError(one, raiseNode.get(inliningTarget));
                }
            }
            return createComplexNode.execute(inliningTarget, cls, value.getReal(), value.getImag() + two);
        }

        @Specialization(guards = "!isString(one)")
        Object complexFromComplexDouble(VirtualFrame frame, Object cls, Object one, double two,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode,
                        @Shared @Cached CanBeDoubleNode canBeDoubleNode,
                        @Shared("floatAsDouble") @Cached PyFloatAsDoubleNode asDoubleNode,
                        @Shared("isComplex") @Cached PyComplexCheckExactNode isComplexType,
                        @Shared("isComplexResult") @Cached PyComplexCheckExactNode isResultComplexType,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            PComplex value = getComplexNumberFromObject(frame, one, inliningTarget, isComplexType, isResultComplexType, raiseNode);
            if (value == null) {
                if (canBeDoubleNode.execute(inliningTarget, one)) {
                    return createComplexNode.execute(inliningTarget, cls, asDoubleNode.execute(frame, inliningTarget, one), two);
                } else {
                    throw raiseFirstArgError(one, raiseNode.get(inliningTarget));
                }
            }
            return createComplexNode.execute(inliningTarget, cls, value.getReal(), value.getImag() + two);
        }

        @Specialization(guards = "!isString(one)")
        Object complexFromComplexPInt(VirtualFrame frame, Object cls, Object one, PInt two,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode,
                        @Shared @Cached CanBeDoubleNode canBeDoubleNode,
                        @Shared("floatAsDouble") @Cached PyFloatAsDoubleNode asDoubleNode,
                        @Shared("isComplex") @Cached PyComplexCheckExactNode isComplexType,
                        @Shared("isComplexResult") @Cached PyComplexCheckExactNode isResultComplexType,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            PComplex value = getComplexNumberFromObject(frame, one, inliningTarget, isComplexType, isResultComplexType, raiseNode);
            if (value == null) {
                if (canBeDoubleNode.execute(inliningTarget, one)) {
                    return createComplexNode.execute(inliningTarget, cls, asDoubleNode.execute(frame, inliningTarget, one), two.doubleValueWithOverflow(this));
                } else {
                    throw raiseFirstArgError(one, raiseNode.get(inliningTarget));
                }
            }
            return createComplexNode.execute(inliningTarget, cls, value.getReal(), value.getImag() + two.doubleValueWithOverflow(this));
        }

        @Specialization(guards = "!isString(one)")
        Object complexFromComplexComplex(VirtualFrame frame, Object cls, Object one, PComplex two,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode,
                        @Shared @Cached CanBeDoubleNode canBeDoubleNode,
                        @Shared("floatAsDouble") @Cached PyFloatAsDoubleNode asDoubleNode,
                        @Shared("isComplex") @Cached PyComplexCheckExactNode isComplexType,
                        @Shared("isComplexResult") @Cached PyComplexCheckExactNode isResultComplexType,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            PComplex value = getComplexNumberFromObject(frame, one, inliningTarget, isComplexType, isResultComplexType, raiseNode);
            if (value == null) {
                if (canBeDoubleNode.execute(inliningTarget, one)) {
                    return createComplexNode.execute(inliningTarget, cls, asDoubleNode.execute(frame, inliningTarget, one) - two.getImag(), two.getReal());
                } else {
                    throw raiseFirstArgError(one, raiseNode.get(inliningTarget));
                }
            }
            return createComplexNode.execute(inliningTarget, cls, value.getReal() - two.getImag(), value.getImag() + two.getReal());
        }

        @Specialization(guards = {"!isString(one)", "!isNoValue(two)", "!isPComplex(two)"})
        @SuppressWarnings("truffle-static-method")
        Object complexFromComplexObject(VirtualFrame frame, Object cls, Object one, Object two,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CreateComplexNode createComplexNode,
                        @Shared @Cached CanBeDoubleNode canBeDoubleNode,
                        @Shared("floatAsDouble") @Cached PyFloatAsDoubleNode asDoubleNode,
                        @Shared("isComplex") @Cached PyComplexCheckExactNode isComplexType,
                        @Shared("isComplexResult") @Cached PyComplexCheckExactNode isResultComplexType,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            PComplex oneValue = getComplexNumberFromObject(frame, one, inliningTarget, isComplexType, isResultComplexType, raiseNode);
            if (canBeDoubleNode.execute(inliningTarget, two)) {
                double twoValue = asDoubleNode.execute(frame, inliningTarget, two);
                if (oneValue == null) {
                    if (canBeDoubleNode.execute(inliningTarget, one)) {
                        return createComplexNode.execute(inliningTarget, cls, asDoubleNode.execute(frame, inliningTarget, one), twoValue);
                    } else {
                        throw raiseFirstArgError(one, raiseNode.get(inliningTarget));
                    }
                }
                return createComplexNode.execute(inliningTarget, cls, oneValue.getReal(), oneValue.getImag() + twoValue);
            } else {
                throw raiseSecondArgError(two, raiseNode.get(inliningTarget));
            }
        }

        @Specialization
        Object complexFromString(VirtualFrame frame, Object cls, TruffleString real, Object imaginary,
                        @Bind("this") Node inliningTarget,
                        @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            if (imaginary != PNone.NO_VALUE) {
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.COMPLEX_CANT_TAKE_ARG);
            }
            return convertStringToComplex(frame, inliningTarget, toJavaStringNode.execute(real), cls, real, raiseNode);
        }

        @Specialization
        Object complexFromString(VirtualFrame frame, Object cls, PString real, Object imaginary,
                        @Bind("this") Node inliningTarget,
                        @Cached CastToJavaStringNode castToStringNode,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            if (imaginary != PNone.NO_VALUE) {
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.COMPLEX_CANT_TAKE_ARG);
            }
            return convertStringToComplex(frame, inliningTarget, castToStringNode.execute(real), cls, real, raiseNode);
        }

        private Object callComplex(VirtualFrame frame, Object object) {
            if (callComplexNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                callComplexNode = insert(LookupAndCallUnaryNode.create(T___COMPLEX__));
            }
            return callComplexNode.executeObject(frame, object);
        }

        private WarnNode getWarnNode() {
            if (warnNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                warnNode = insert(WarnNode.create());
            }
            return warnNode;
        }

        private static PException raiseFirstArgError(Object x, PRaiseNode raiseNode) {
            throw raiseNode.raise(PythonBuiltinClassType.TypeError, ErrorMessages.ARG_MUST_BE_STRING_OR_NUMBER, "complex() first", x);
        }

        private static PException raiseSecondArgError(Object x, PRaiseNode raiseNode) {
            throw raiseNode.raise(PythonBuiltinClassType.TypeError, ErrorMessages.ARG_MUST_BE_NUMBER, "complex() second", x);
        }

        private PComplex getComplexNumberFromObject(VirtualFrame frame, Object object, Node inliningTarget,
                        PyComplexCheckExactNode isComplexType, PyComplexCheckExactNode isResultComplexType, PRaiseNode.Lazy raiseNode) {
            if (isComplexType.execute(inliningTarget, object)) {
                return (PComplex) object;
            } else {
                Object result = callComplex(frame, object);
                if (result instanceof PComplex) {
                    if (!isResultComplexType.execute(inliningTarget, result)) {
                        getWarnNode().warnFormat(frame, null, PythonBuiltinClassType.DeprecationWarning, 1,
                                        ErrorMessages.WARN_P_RETURNED_NON_P,
                                        object, "__complex__", "complex", result, "complex");
                    }
                    return (PComplex) result;
                } else if (result != PNone.NO_VALUE) {
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.COMPLEX_RETURNED_NON_COMPLEX, result);
                }
                if (object instanceof PComplex) {
                    // the class extending PComplex but doesn't have __complex__ method
                    return (PComplex) object;
                }
                return null;
            }
        }

        @Fallback
        @SuppressWarnings("unused")
        static Object complexGeneric(Object cls, Object realObj, Object imaginaryObj,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.IS_NOT_TYPE_OBJ, "complex.__new__(X): X", cls);
        }

        // Adapted from CPython's complex_subtype_from_string
        private Object convertStringToComplex(VirtualFrame frame, Node inliningTarget, String src, Object cls, Object origObj, PRaiseNode.Lazy raiseNode) {
            String str = FloatUtils.removeUnicodeAndUnderscores(src);
            if (str == null) {
                if (callReprNode == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    callReprNode = insert(LookupAndCallUnaryNode.create(SpecialMethodSlot.Repr));
                }
                Object strStr = callReprNode.executeObject(frame, origObj);
                if (PGuards.isString(strStr)) {
                    throw raiseNode.get(inliningTarget).raise(ValueError, ErrorMessages.COULD_NOT_CONVERT_STRING_TO_COMPLEX, strStr);
                } else {
                    // During the formatting of "ValueError: invalid literal ..." exception,
                    // CPython attempts to raise "TypeError: __repr__ returned non-string",
                    // which gets later overwitten with the original "ValueError",
                    // but without any message (since the message formatting failed)
                    throw raiseNode.get(inliningTarget).raise(ValueError);
                }
            }
            Object c = convertStringToComplexOrNull(str, cls);
            if (c == null) {
                throw raiseNode.get(inliningTarget).raise(ValueError, ErrorMessages.COMPLEX_ARG_IS_MALFORMED_STR);
            }
            return c;
        }

        // Adapted from CPython's complex_from_string_inner
        @TruffleBoundary
        private Object convertStringToComplexOrNull(String str, Object cls) {
            int len = str.length();

            // position on first nonblank
            int i = FloatUtils.skipAsciiWhitespace(str, 0, len);

            boolean gotBracket;
            if (i < len && str.charAt(i) == '(') {
                // Skip over possible bracket from repr().
                gotBracket = true;
                i = FloatUtils.skipAsciiWhitespace(str, i + 1, len);
            } else {
                gotBracket = false;
            }

            double x, y;
            boolean expectJ;

            // first look for forms starting with <float>
            FloatUtils.StringToDoubleResult res1 = FloatUtils.stringToDouble(str, i, len);
            if (res1 != null) {
                // all 4 forms starting with <float> land here
                i = res1.position;
                char ch = i < len ? str.charAt(i) : '\0';
                if (ch == '+' || ch == '-') {
                    // <float><signed-float>j | <float><sign>j
                    x = res1.value;
                    FloatUtils.StringToDoubleResult res2 = FloatUtils.stringToDouble(str, i, len);
                    if (res2 != null) {
                        // <float><signed-float>j
                        y = res2.value;
                        i = res2.position;
                    } else {
                        // <float><sign>j
                        y = ch == '+' ? 1.0 : -1.0;
                        i++;
                    }
                    expectJ = true;
                } else if (ch == 'j' || ch == 'J') {
                    // <float>j
                    i++;
                    y = res1.value;
                    x = 0;
                    expectJ = false;
                } else {
                    // <float>
                    x = res1.value;
                    y = 0;
                    expectJ = false;
                }
            } else {
                // not starting with <float>; must be <sign>j or j
                char ch = i < len ? str.charAt(i) : '\0';
                if (ch == '+' || ch == '-') {
                    // <sign>j
                    y = ch == '+' ? 1.0 : -1.0;
                    i++;
                } else {
                    // j
                    y = 1.0;
                }
                x = 0;
                expectJ = true;
            }

            if (expectJ) {
                char ch = i < len ? str.charAt(i) : '\0';
                if (!(ch == 'j' || ch == 'J')) {
                    return null;
                }
                i++;
            }

            // trailing whitespace and closing bracket
            i = FloatUtils.skipAsciiWhitespace(str, i, len);
            if (gotBracket) {
                // if there was an opening parenthesis, then the corresponding
                // closing parenthesis should be right here
                if (i >= len || str.charAt(i) != ')') {
                    return null;
                }
                i = FloatUtils.skipAsciiWhitespace(str, i + 1, len);
            }

            // we should now be at the end of the string
            if (i != len) {
                return null;
            }
            return CreateComplexNode.executeUncached(cls, x, y);
        }
    }

    // dict(**kwarg)
    // dict(mapping, **kwarg)
    // dict(iterable, **kwarg)
    @Builtin(name = J_DICT, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PDict, doc = """
                    dict() -> new empty dictionary
                    dict(mapping) -> new dictionary initialized from a mapping object's
                        (key, value) pairs
                    dict(iterable) -> new dictionary initialized as if via:
                        d = {}
                        for k, v in iterable:
                            d[k] = v
                    dict(**kwargs) -> new dictionary initialized with the name=value pairs
                        in the keyword argument list.  For example:  dict(one=1, two=2)""")
    @GenerateNodeFactory
    public abstract static class DictionaryNode extends PythonBuiltinNode {
        @Specialization(guards = "isBuiltinDict(cls)")
        @SuppressWarnings("unused")
        static PDict builtinDict(Object cls, Object[] args, PKeyword[] keywordArgs,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createDict();
        }

        @Specialization(replaces = "builtinDict")
        @SuppressWarnings("unused")
        static PDict dict(Object cls, Object[] args, PKeyword[] keywordArgs,
                        @Bind("this") Node inliningTarget,
                        @Cached InlinedConditionProfile orderedProfile,
                        @Cached IsSubtypeNode isSubtypeNode,
                        @Shared @Cached PythonObjectFactory factory) {
            if (orderedProfile.profile(inliningTarget, isSubtypeNode.execute(cls, PythonBuiltinClassType.POrderedDict))) {
                return factory.createOrderedDict(cls);
            }
            return factory.createDict(cls);
        }

        protected static boolean isBuiltinDict(Object cls) {
            return cls == PythonBuiltinClassType.PDict;
        }
    }

    // enumerate(iterable, start=0)
    @Builtin(name = J_ENUMERATE, minNumOfPositionalArgs = 2, parameterNames = {"cls", "iterable", "start"}, constructsClass = PythonBuiltinClassType.PEnumerate, doc = """
                    Return an enumerate object.

                      iterable
                        an object supporting iteration

                    The enumerate object yields pairs containing a count (from start, which
                    defaults to zero) and a value yielded by the iterable argument.

                    enumerate is useful for obtaining an indexed list:
                        (0, seq[0]), (1, seq[1]), (2, seq[2]), ...""")
    @GenerateNodeFactory
    public abstract static class EnumerateNode extends PythonBuiltinNode {

        @Specialization
        static PEnumerate doNone(VirtualFrame frame, Object cls, Object iterable, @SuppressWarnings("unused") PNone keywordArg,
                        @Bind("this") Node inliningTarget,
                        @Shared("getIter") @Cached PyObjectGetIter getIter,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createEnumerate(cls, getIter.execute(frame, inliningTarget, iterable), 0);
        }

        @Specialization
        static PEnumerate doInt(VirtualFrame frame, Object cls, Object iterable, int start,
                        @Bind("this") Node inliningTarget,
                        @Shared("getIter") @Cached PyObjectGetIter getIter,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createEnumerate(cls, getIter.execute(frame, inliningTarget, iterable), start);
        }

        @Specialization
        static PEnumerate doLong(VirtualFrame frame, Object cls, Object iterable, long start,
                        @Bind("this") Node inliningTarget,
                        @Shared("getIter") @Cached PyObjectGetIter getIter,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createEnumerate(cls, getIter.execute(frame, inliningTarget, iterable), start);
        }

        @Specialization
        static PEnumerate doPInt(VirtualFrame frame, Object cls, Object iterable, PInt start,
                        @Bind("this") Node inliningTarget,
                        @Shared("getIter") @Cached PyObjectGetIter getIter,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createEnumerate(cls, getIter.execute(frame, inliningTarget, iterable), start);
        }

        static boolean isIntegerIndex(Object idx) {
            return isInteger(idx) || idx instanceof PInt;
        }

        @Specialization(guards = "!isIntegerIndex(start)")
        static void enumerate(@SuppressWarnings("unused") Object cls, @SuppressWarnings("unused") Object iterable, Object start,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.OBJ_CANNOT_BE_INTERPRETED_AS_INTEGER, start);
        }
    }

    // reversed(seq)
    @Builtin(name = J_REVERSED, minNumOfPositionalArgs = 2, constructsClass = PythonBuiltinClassType.PReverseIterator, doc = "Return a reverse iterator over the values of the given sequence.")
    @GenerateNodeFactory
    @ImportStatic(SpecialMethodSlot.class)
    public abstract static class ReversedNode extends PythonBuiltinNode {

        @Specialization
        static PythonObject reversed(@SuppressWarnings("unused") Object cls, PIntRange range,
                        @Bind("this") Node inliningTarget,
                        @Cached InlinedBranchProfile overflowProfile,
                        @Shared @Cached PythonObjectFactory factory) {
            int lstart = range.getIntStart();
            int lstep = range.getIntStep();
            int ulen = range.getIntLength();
            try {
                int new_stop = subtractExact(lstart, lstep);
                int new_start = addExact(new_stop, multiplyExact(ulen, lstep));
                return factory.createIntRangeIterator(new_start, new_stop, negateExact(lstep), ulen);
            } catch (OverflowException e) {
                overflowProfile.enter(inliningTarget);
                return handleOverflow(lstart, lstep, ulen, PythonContext.get(inliningTarget).factory());
            }
        }

        @TruffleBoundary
        private static PBigRangeIterator handleOverflow(int lstart, int lstep, int ulen, PythonObjectSlowPathFactory factory) {
            BigInteger bstart = BigInteger.valueOf(lstart);
            BigInteger bstep = BigInteger.valueOf(lstep);
            BigInteger blen = BigInteger.valueOf(ulen);
            BigInteger new_stop = bstart.subtract(bstep);
            BigInteger new_start = new_stop.add(blen.multiply(bstep));

            return factory.createBigRangeIterator(new_start, new_stop, bstep.negate(), blen);
        }

        @Specialization
        @TruffleBoundary
        PythonObject reversed(@SuppressWarnings("unused") Object cls, PBigRange range) {
            BigInteger lstart = range.getBigIntegerStart();
            BigInteger lstep = range.getBigIntegerStep();
            BigInteger ulen = range.getBigIntegerLength();

            BigInteger new_stop = lstart.subtract(lstep);
            BigInteger new_start = new_stop.add(ulen.multiply(lstep));

            return getContext().factory().createBigRangeIterator(new_start, new_stop, lstep.negate(), ulen);
        }

        @Specialization
        static PythonObject reversed(Object cls, PString value,
                        @Bind("this") Node inliningTarget,
                        @Cached CastToTruffleStringNode castToStringNode,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createStringReverseIterator(cls, castToStringNode.execute(inliningTarget, value));
        }

        @Specialization
        static PythonObject reversed(Object cls, TruffleString value,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createStringReverseIterator(cls, value);
        }

        @Specialization(guards = {"!isString(sequence)", "!isPRange(sequence)"})
        static Object reversed(VirtualFrame frame, Object cls, Object sequence,
                        @Bind("this") Node inliningTarget,
                        @Cached GetClassNode getClassNode,
                        @Cached("create(Reversed)") LookupSpecialMethodSlotNode lookupReversed,
                        @Cached CallUnaryMethodNode callReversed,
                        @Cached PySequenceSizeNode pySequenceSizeNode,
                        @Cached InlinedConditionProfile noReversedProfile,
                        @Cached PySequenceCheckNode pySequenceCheck,
                        @Shared @Cached PythonObjectFactory factory,
                        @Cached PRaiseNode.Lazy raiseNode) {
            Object sequenceKlass = getClassNode.execute(inliningTarget, sequence);
            Object reversed = lookupReversed.execute(frame, sequenceKlass, sequence);
            if (noReversedProfile.profile(inliningTarget, reversed == PNone.NO_VALUE)) {
                if (!pySequenceCheck.execute(inliningTarget, sequence)) {
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.OBJ_ISNT_REVERSIBLE, sequence);
                } else {
                    int lengthHint = pySequenceSizeNode.execute(frame, inliningTarget, sequence);
                    return factory.createSequenceReverseIterator(cls, sequence, lengthHint);
                }
            } else {
                return callReversed.executeObject(frame, reversed, sequence);
            }
        }
    }

    // float([x])
    @Builtin(name = J_FLOAT, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 2, constructsClass = PythonBuiltinClassType.PFloat, doc = "Convert a string or number to a floating point number, if possible.")
    @GenerateNodeFactory
    @ReportPolymorphism
    abstract static class FloatNode extends PythonBinaryBuiltinNode {

        @Child NonPrimitiveFloatNode nonPrimitiveFloatNode;

        @Specialization
        Object doIt(VirtualFrame frame, Object cls, Object arg,
                        @Bind("this") Node inliningTarget,
                        @Cached IsBuiltinClassExactProfile isPrimitiveFloatProfile,
                        @Cached PrimitiveFloatNode primitiveFloatNode,
                        @Cached NeedsNativeAllocationNode needsNativeAllocationNode) {
            if (isPrimitiveFloat(inliningTarget, cls, isPrimitiveFloatProfile)) {
                return primitiveFloatNode.execute(frame, inliningTarget, arg);
            } else {
                boolean needsNativeAllocation = needsNativeAllocationNode.execute(inliningTarget, cls);
                if (nonPrimitiveFloatNode == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    nonPrimitiveFloatNode = insert(NonPrimitiveFloatNodeGen.create());
                }
                return nonPrimitiveFloatNode.execute(frame, cls, arg, needsNativeAllocation);
            }
        }

        @GenerateCached(false)
        @GenerateInline
        @ImportStatic(PGuards.class)
        abstract static class PrimitiveFloatNode extends Node {
            abstract double execute(VirtualFrame frame, Node inliningTarget, Object arg);

            @Specialization
            static double floatFromDouble(double arg) {
                return arg;
            }

            @Specialization
            static double floatFromInt(int arg) {
                return arg;
            }

            static double floatFromLong(long arg) {
                return arg;
            }

            @Specialization
            static double floatFromBoolean(boolean arg) {
                return arg ? 1d : 0d;
            }

            @Specialization(guards = "isNoValue(obj)")
            static double floatFromNoValue(@SuppressWarnings("unused") PNone obj) {
                return 0.0;
            }

            @Fallback
            @InliningCutoff
            static double floatFromObject(VirtualFrame frame, Node inliningTarget, Object obj,
                            @Cached PyUnicodeCheckExactNode stringCheck,
                            @Cached PyFloatFromString fromString,
                            @Cached PyNumberFloatNode pyNumberFloat) {
                if (stringCheck.execute(inliningTarget, obj)) {
                    return fromString.execute(frame, inliningTarget, obj);
                }
                return pyNumberFloat.execute(frame, inliningTarget, obj);
            }
        }

        @ImportStatic(PGuards.class)
        @GenerateInline(false) // intentionally lazy
        abstract static class NonPrimitiveFloatNode extends Node {
            abstract Object execute(VirtualFrame frame, Object cls, Object arg, boolean needsNativeAllocation);

            @Specialization(guards = {"!needsNativeAllocation", "isNoValue(obj)"})
            @InliningCutoff
            Object floatFromNoneManagedSubclass(Object cls, PNone obj,
                            @SuppressWarnings("unused") boolean needsNativeAllocation,
                            @Shared @Cached PythonObjectFactory factory) {
                return factory.createFloat(cls, PrimitiveFloatNode.floatFromNoValue(obj));
            }

            @Specialization(guards = "!needsNativeAllocation")
            @InliningCutoff
            Object floatFromObjectManagedSubclass(VirtualFrame frame, Object cls, Object obj, @SuppressWarnings("unused") boolean needsNativeAllocation,
                            @Bind("this") @SuppressWarnings("unused") Node inliningTarget,
                            @Shared @Cached PythonObjectFactory factory,
                            @Shared @Cached PrimitiveFloatNode recursiveCallNode) {
                return factory.createFloat(cls, recursiveCallNode.execute(frame, inliningTarget, obj));
            }

            // logic similar to float_subtype_new(PyTypeObject *type, PyObject *x) from CPython
            // floatobject.c we have to first create a temporary float, then fill it into
            // a natively allocated subtype structure
            @Specialization(guards = {"needsNativeAllocation", //
                            "isSubtypeOfFloat(frame, isSubtype, cls)"}, limit = "1")
            @InliningCutoff
            static Object floatFromObjectNativeSubclass(VirtualFrame frame, Object cls, Object obj, @SuppressWarnings("unused") boolean needsNativeAllocation,
                            @Bind("this") @SuppressWarnings("unused") Node inliningTarget,
                            @Cached @SuppressWarnings("unused") IsSubtypeNode isSubtype,
                            @Cached CExtNodes.FloatSubtypeNew subtypeNew,
                            @Shared @Cached PrimitiveFloatNode recursiveCallNode) {
                return subtypeNew.call(cls, recursiveCallNode.execute(frame, inliningTarget, obj));
            }

            protected static boolean isSubtypeOfFloat(VirtualFrame frame, IsSubtypeNode isSubtypeNode, Object cls) {
                return isSubtypeNode.execute(frame, cls, PythonBuiltinClassType.PFloat);
            }
        }

        protected static boolean isPrimitiveFloat(Node inliningTarget, Object cls, IsBuiltinClassExactProfile isPrimitiveProfile) {
            return isPrimitiveProfile.profileClass(inliningTarget, cls, PythonBuiltinClassType.PFloat);
        }
    }

    // frozenset([iterable])
    @Builtin(name = J_FROZENSET, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 2, constructsClass = PythonBuiltinClassType.PFrozenSet, doc = """
                    frozenset() -> empty frozenset object
                    frozenset(iterable) -> frozenset object

                    Build an immutable unordered collection of unique elements.""")
    @GenerateNodeFactory
    public abstract static class FrozenSetNode extends PythonBinaryBuiltinNode {

        @Specialization(guards = "isNoValue(arg)")
        static PFrozenSet frozensetEmpty(Object cls, @SuppressWarnings("unused") PNone arg,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createFrozenSet(cls);
        }

        @Specialization(guards = "isBuiltinClass.profileIsAnyBuiltinClass(inliningTarget, cls)")
        static PFrozenSet frozensetIdentity(@SuppressWarnings("unused") Object cls, PFrozenSet arg,
                        @SuppressWarnings("unused") @Bind("this") Node inliningTarget,
                        @Shared("isBuiltinProfile") @SuppressWarnings("unused") @Cached IsAnyBuiltinClassProfile isBuiltinClass) {
            return arg;
        }

        @Specialization(guards = "!isBuiltinClass.profileIsAnyBuiltinClass(inliningTarget, cls)")
        static PFrozenSet subFrozensetIdentity(Object cls, PFrozenSet arg,
                        @SuppressWarnings("unused") @Bind("this") Node inliningTarget,
                        @Shared("isBuiltinProfile") @SuppressWarnings("unused") @Cached IsAnyBuiltinClassProfile isBuiltinClass,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createFrozenSet(cls, arg.getDictStorage());
        }

        @Specialization(guards = {"!isNoValue(iterable)", "!isPFrozenSet(iterable)"})
        static PFrozenSet frozensetIterable(VirtualFrame frame, Object cls, Object iterable,
                        @Bind("this") Node inliningTarget,
                        @Cached HashingCollectionNodes.GetClonedHashingStorageNode getHashingStorageNode,
                        @Shared @Cached PythonObjectFactory factory) {
            HashingStorage storage = getHashingStorageNode.doNoValue(frame, inliningTarget, iterable);
            return factory.createFrozenSet(cls, storage);
        }
    }

    // int(x=0)
    // int(x, base=10)
    @Builtin(name = J_INT, minNumOfPositionalArgs = 1, parameterNames = {"cls", "x", "base"}, numOfPositionalOnlyArgs = 2, constructsClass = PythonBuiltinClassType.PInt, doc = """
                    int([x]) -> integer
                    int(x, base=10) -> integer

                    Convert a number or string to an integer, or return 0 if no arguments
                    are given.  If x is a number, return x.__int__().  For floating point
                    numbers, this truncates towards zero.

                    If x is not a number or if base is given, then x must be a string,
                    bytes, or bytearray instance representing an integer literal in the
                    given base.  The literal can be preceded by '+' or '-' and be surrounded
                    by whitespace.  The base defaults to 10.  Valid bases are 0 and 2-36.
                    Base 0 means to interpret the base from the string as an integer literal.""")
    @GenerateNodeFactory
    public abstract static class IntNode extends PythonTernaryBuiltinNode {
        @Child private BytesNodes.ToBytesNode toByteArrayNode;
        @Child private LookupAndCallUnaryNode callIndexNode;
        @Child private LookupAndCallUnaryNode callTruncNode;
        @Child private LookupAndCallUnaryNode callReprNode;
        @Child private LookupAndCallUnaryNode callIntNode;
        @Child private WarnNode warnNode;

        public final Object executeWith(VirtualFrame frame, Object number) {
            return execute(frame, PythonBuiltinClassType.PInt, number, 10);
        }

        public final Object executeWith(VirtualFrame frame, Object number, Object base) {
            return execute(frame, PythonBuiltinClassType.PInt, number, base);
        }

        @TruffleBoundary
        private static Object stringToIntInternal(String num, int base, PythonContext context) {
            try {
                BigInteger bi = asciiToBigInteger(num, base, context);
                if (bi == null) {
                    return null;
                }
                if (bi.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0 || bi.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
                    return bi;
                } else {
                    return bi.intValue();
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }

        private Object stringToInt(VirtualFrame frame, Object cls, String number, int base, Object origObj,
                        Node inliningTarget, IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        InlinedBranchProfile notSimpleDecimalLiteralProfile, InlinedBranchProfile invalidValueProfile,
                        InlinedBranchProfile bigIntegerProfile, InlinedBranchProfile primitiveIntProfile, InlinedBranchProfile fullIntProfile,
                        PythonObjectFactory factory, PRaiseNode.Lazy raiseNode) {
            if (base == 0 || base == 10) {
                Object value = parseSimpleDecimalLiteral(number, 0, number.length());
                if (value != null) {
                    return createInt(cls, value, inliningTarget, isPrimitiveIntProfile, bigIntegerProfile, primitiveIntProfile, fullIntProfile, factory);
                }
            }
            notSimpleDecimalLiteralProfile.enter(inliningTarget);
            Object value = stringToIntInternal(number, base, getContext());
            if (value == null) {
                invalidValueProfile.enter(inliningTarget);
                if (callReprNode == null) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    callReprNode = insert(LookupAndCallUnaryNode.create(SpecialMethodSlot.Repr));
                }
                Object str = callReprNode.executeObject(frame, origObj);
                if (PGuards.isString(str)) {
                    throw raiseNode.get(inliningTarget).raise(ValueError, ErrorMessages.INVALID_LITERAL_FOR_INT_WITH_BASE, base, str);
                } else {
                    // During the formatting of "ValueError: invalid literal ..." exception,
                    // CPython attempts to raise "TypeError: __repr__ returned non-string",
                    // which gets later overwitten with the original "ValueError",
                    // but without any message (since the message formatting failed)
                    throw raiseNode.get(inliningTarget).raise(ValueError);
                }
            }
            return createInt(cls, value, inliningTarget, isPrimitiveIntProfile, bigIntegerProfile, primitiveIntProfile, fullIntProfile, factory);
        }

        private static Object createInt(Object cls, Object value, Node inliningTarget, IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        InlinedBranchProfile bigIntegerProfile, InlinedBranchProfile primitiveIntProfile, InlinedBranchProfile fullIntProfile,
                        PythonObjectFactory factory) {
            if (value instanceof BigInteger) {
                bigIntegerProfile.enter(inliningTarget);
                return factory.createInt(cls, (BigInteger) value);
            } else if (isPrimitiveInt(inliningTarget, cls, isPrimitiveIntProfile)) {
                primitiveIntProfile.enter(inliningTarget);
                return value;
            } else {
                fullIntProfile.enter(inliningTarget);
                if (value instanceof Integer) {
                    return factory.createInt(cls, (Integer) value);
                } else if (value instanceof Long) {
                    return factory.createInt(cls, (Long) value);
                } else if (value instanceof Boolean) {
                    return factory.createInt(cls, (Boolean) value ? 1 : 0);
                } else if (value instanceof PInt) {
                    return factory.createInt(cls, ((PInt) value).getValue());
                }
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new IllegalStateException("Unexpected type");
        }

        private static void checkBase(int base, Node inliningTarget, InlinedConditionProfile invalidBase, PRaiseNode.Lazy raiseNode) {
            if (invalidBase.profile(inliningTarget, (base < 2 || base > 36) && base != 0)) {
                throw raiseNode.get(inliningTarget).raise(ValueError, ErrorMessages.BASE_OUT_OF_RANGE_FOR_INT);
            }
        }

        private static void checkBase(PInt base, Node inliningTarget, InlinedConditionProfile invalidBase, PRaiseNode.Lazy raiseNode) {
            int ibase;
            try {
                ibase = base.intValueExact();
            } catch (OverflowException e) {
                // this should just trigger the error
                ibase = 1;
            }
            checkBase(ibase, inliningTarget, invalidBase, raiseNode);
        }

        // Adapted from Jython
        private static BigInteger asciiToBigInteger(String str, int possibleBase, PythonContext context) throws NumberFormatException {
            CompilerAsserts.neverPartOfCompilation();
            int base = possibleBase;
            int b = 0;
            int e = str.length();

            while (b < e && Character.isWhitespace(str.charAt(b))) {
                b++;
            }

            while (e > b && Character.isWhitespace(str.charAt(e - 1))) {
                e--;
            }

            boolean acceptUnderscore = false;
            boolean raiseIfNotZero = false;
            char sign = 0;
            if (b < e) {
                sign = str.charAt(b);
                if (sign == '-' || sign == '+') {
                    b++;
                }

                if (base == 16) {
                    if (str.charAt(b) == '0') {
                        if (b < e - 1 && Character.toUpperCase(str.charAt(b + 1)) == 'X') {
                            b += 2;
                            acceptUnderscore = true;
                        }
                    }
                } else if (base == 0) {
                    if (str.charAt(b) == '0') {
                        if (b < e - 1 && Character.toUpperCase(str.charAt(b + 1)) == 'X') {
                            base = 16;
                            b += 2;
                            acceptUnderscore = true;
                        } else if (b < e - 1 && Character.toUpperCase(str.charAt(b + 1)) == 'O') {
                            base = 8;
                            b += 2;
                            acceptUnderscore = true;
                        } else if (b < e - 1 && Character.toUpperCase(str.charAt(b + 1)) == 'B') {
                            base = 2;
                            b += 2;
                            acceptUnderscore = true;
                        } else {
                            raiseIfNotZero = true;
                        }
                    }
                } else if (base == 8) {
                    if (b < e - 1 && Character.toUpperCase(str.charAt(b + 1)) == 'O') {
                        b += 2;
                        acceptUnderscore = true;
                    }
                } else if (base == 2) {
                    if (b < e - 1 && Character.toUpperCase(str.charAt(b + 1)) == 'B') {
                        b += 2;
                        acceptUnderscore = true;
                    }
                }
            }

            if (base == 0) {
                base = 10;
            }

            // reject invalid characters without going to BigInteger
            for (int i = b; i < e; i++) {
                char c = str.charAt(i);
                if (c == '_') {
                    if (!acceptUnderscore || i == e - 1) {
                        throw new NumberFormatException("Illegal underscore in int literal");
                    } else {
                        acceptUnderscore = false;
                    }
                } else {
                    acceptUnderscore = true;
                    if (Character.digit(c, base) == -1) {
                        // invalid char
                        return null;
                    }
                }
            }

            String s = str;
            if (b > 0 || e < str.length()) {
                s = str.substring(b, e);
            }
            s = s.replace("_", "");

            checkMaxDigits(context, s.length(), base);

            BigInteger bi;
            if (sign == '-') {
                bi = new BigInteger("-" + s, base);
            } else {
                bi = new BigInteger(s, base);
            }

            if (raiseIfNotZero && !bi.equals(BigInteger.ZERO)) {
                throw new NumberFormatException("Obsolete octal int literal");
            }
            return bi;
        }

        private static void checkMaxDigits(PythonContext context, int digits, int base) {
            if (digits > SysModuleBuiltins.INT_MAX_STR_DIGITS_THRESHOLD && Integer.bitCount(base) != 1) {
                Integer maxDigits = context.getIntMaxStrDigits();
                if (maxDigits > 0 && digits > maxDigits) {
                    throw PRaiseNode.getUncached().raise(ValueError, ErrorMessages.EXCEEDS_THE_LIMIT_FOR_INTEGER_STRING_CONVERSION_D, maxDigits, digits);
                }
            }
        }

        /**
         * Fast path parser of integer literals. Accepts only a subset of allowed literals - no
         * underscores, no leading zeros, no plus sign, no spaces, only ascii digits and the result
         * must be small enough to fit into long.
         *
         * @param arg the string to parse
         * @return parsed integer, long or null if the literal is not simple enough
         */
        public static Object parseSimpleDecimalLiteral(String arg, int offset, int remaining) {
            if (remaining <= 0) {
                return null;
            }
            int start = arg.charAt(offset) == '-' ? 1 : 0;
            if (remaining <= start || remaining > 18 + start) {
                return null;
            }
            if (arg.charAt(start + offset) == '0') {
                if (remaining > start + 1) {
                    return null;
                }
                return 0;
            }
            long value = 0;
            for (int i = start; i < remaining; i++) {
                char c = arg.charAt(i + offset);
                if (c < '0' || c > '9') {
                    return null;
                }
                value = value * 10 + (c - '0');
            }
            if (start != 0) {
                value = -value;
            }
            if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
                return (int) value;
            }
            return value;
        }

        protected static boolean isPrimitiveInt(Node inliningTarget, Object cls, IsBuiltinClassExactProfile profile) {
            return profile.profileClass(inliningTarget, cls, PythonBuiltinClassType.PInt);
        }

        @Specialization
        static Object parseInt(Object cls, boolean arg, @SuppressWarnings("unused") PNone base,
                        @Bind("this") Node inliningTarget,
                        @Shared("primitiveInt") @Cached IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        @Shared @Cached PythonObjectFactory factory) {
            if (isPrimitiveInt(inliningTarget, cls, isPrimitiveIntProfile)) {
                return arg ? 1 : 0;
            } else {
                return factory.createInt(cls, arg ? 1 : 0);
            }
        }

        @Specialization(guards = "isNoValue(base)")
        static Object createInt(Object cls, int arg, @SuppressWarnings("unused") PNone base,
                        @Bind("this") Node inliningTarget,
                        @Shared("primitiveInt") @Cached IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        // Dummy argument just so that it can be @Shared in the other
                        // specialization, which generated better code for interpreter
                        @SuppressWarnings("unused") @Shared @Cached InlinedConditionProfile isIntProfile,
                        @Shared @Cached PythonObjectFactory factory) {
            if (isPrimitiveInt(inliningTarget, cls, isPrimitiveIntProfile)) {
                return arg;
            }
            return factory.createInt(cls, arg);
        }

        @Specialization(guards = "isNoValue(base)")
        static Object createInt(Object cls, long arg, @SuppressWarnings("unused") PNone base,
                        @Bind("this") Node inliningTarget,
                        @Shared("primitiveInt") @Cached IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        @Shared @Cached InlinedConditionProfile isIntProfile,
                        @Shared @Cached PythonObjectFactory factory) {
            if (isPrimitiveInt(inliningTarget, cls, isPrimitiveIntProfile)) {
                int intValue = (int) arg;
                if (isIntProfile.profile(inliningTarget, intValue == arg)) {
                    return intValue;
                } else {
                    return arg;
                }
            }
            return factory.createInt(cls, arg);
        }

        @Specialization(guards = "isNoValue(base)")
        static Object createInt(Object cls, double arg, @SuppressWarnings("unused") PNone base,
                        @Bind("this") Node inliningTarget,
                        @Exclusive @Cached IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        @Cached PyLongFromDoubleNode pyLongFromDoubleNode,
                        @Exclusive @Cached InlinedBranchProfile bigIntegerProfile,
                        @Exclusive @Cached InlinedBranchProfile primitiveIntProfile,
                        @Exclusive @Cached InlinedBranchProfile fullIntProfile,
                        @Shared @Cached PythonObjectFactory factory) {
            Object result = pyLongFromDoubleNode.execute(inliningTarget, arg);
            return createInt(cls, result, inliningTarget, isPrimitiveIntProfile, bigIntegerProfile, primitiveIntProfile, fullIntProfile, factory);
        }

        // String

        @Specialization
        @Megamorphic
        @InliningCutoff
        @SuppressWarnings("truffle-static-method")
        Object parseTStringError(VirtualFrame frame, Object cls, TruffleString number, Object base,
                        @Bind("this") Node inliningTarget,
                        @Exclusive @Cached InlinedBranchProfile baseIsNoneBranchProfile,
                        @Exclusive @Cached InlinedBranchProfile baseIsIntBranchProfile,
                        @Exclusive @Cached IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        @Exclusive @Cached PyNumberAsSizeNode asSizeNode,
                        @Exclusive @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                        @Exclusive @Cached InlinedConditionProfile invalidBase,
                        @Exclusive @Cached InlinedBranchProfile notSimpleDecimalLiteralProfile,
                        @Exclusive @Cached InlinedBranchProfile invalidValueProfile,
                        @Exclusive @Cached InlinedBranchProfile bigIntegerProfile,
                        @Exclusive @Cached InlinedBranchProfile primitiveIntProfile,
                        @Exclusive @Cached InlinedBranchProfile fullIntProfile,
                        @Shared @Cached PythonObjectFactory factory,
                        @Exclusive @Cached PRaiseNode.Lazy raiseNode) {
            int intBase;
            if (PGuards.isNoValue(base)) {
                baseIsNoneBranchProfile.enter(inliningTarget);
                intBase = 10;
            } else if (base instanceof Integer) {
                baseIsIntBranchProfile.enter(inliningTarget);
                intBase = (int) base;
            } else {
                intBase = asSizeNode.executeLossy(frame, inliningTarget, base);
            }
            checkBase(intBase, inliningTarget, invalidBase, raiseNode);
            return stringToInt(frame, cls, toJavaStringNode.execute(number), intBase, number,
                            inliningTarget, isPrimitiveIntProfile, notSimpleDecimalLiteralProfile, invalidValueProfile,
                            bigIntegerProfile, primitiveIntProfile, fullIntProfile, factory, raiseNode);
        }

        // PIBytesLike
        @Specialization(guards = "isNoValue(base) || isInt(base)")
        @InliningCutoff
        @Megamorphic
        Object parseBytesError(VirtualFrame frame, Object cls, PBytesLike arg, Object base,
                        @Bind("this") Node inliningTarget,
                        @Exclusive @Cached InlinedConditionProfile baseIsNoneBranchProfile,
                        @Exclusive @Cached IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        @Exclusive @Cached InlinedConditionProfile invalidBase,
                        @Exclusive @Cached InlinedBranchProfile notSimpleDecimalLiteralProfile,
                        @Exclusive @Cached InlinedBranchProfile invalidValueProfile,
                        @Exclusive @Cached InlinedBranchProfile bigIntegerProfile,
                        @Exclusive @Cached InlinedBranchProfile primitiveIntProfile,
                        @Exclusive @Cached InlinedBranchProfile fullIntProfile,
                        @Shared @Cached PythonObjectFactory factory,
                        @Exclusive @Cached PRaiseNode.Lazy raiseNode) {
            int intBase;
            if (baseIsNoneBranchProfile.profile(inliningTarget, PGuards.isNoValue(base))) {
                intBase = 10;
            } else {
                intBase = (int) base;
                checkBase(intBase, inliningTarget, invalidBase, raiseNode);
            }
            return stringToInt(frame, cls, toString(arg), intBase, arg, inliningTarget,
                            isPrimitiveIntProfile, notSimpleDecimalLiteralProfile, invalidValueProfile, bigIntegerProfile,
                            primitiveIntProfile, fullIntProfile, factory, raiseNode);
        }

        // PString
        static boolean isNoValueOrIntOrPInt(Object x) {
            return isNoValue(x) || x instanceof Integer || x instanceof PInt;
        }

        @Specialization(guards = "isNoValueOrIntOrPInt(base)")
        @InliningCutoff
        @Megamorphic
        @SuppressWarnings("truffle-static-method")
        Object parsePString(VirtualFrame frame, Object cls, PString arg, Object base,
                        @Bind("this") Node inliningTarget,
                        @Exclusive @Cached IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        @Exclusive @Cached CastToJavaStringNode castToStringNode,
                        @Exclusive @Cached InlinedConditionProfile invalidBase,
                        @Exclusive @Cached InlinedBranchProfile notSimpleDecimalLiteralProfile,
                        @Exclusive @Cached InlinedBranchProfile invalidValueProfile,
                        @Exclusive @Cached InlinedBranchProfile bigIntegerProfile,
                        @Exclusive @Cached InlinedBranchProfile primitiveIntProfile,
                        @Exclusive @Cached InlinedBranchProfile fullIntProfile,
                        @Shared @Cached PythonObjectFactory factory,
                        @Exclusive @Cached PRaiseNode.Lazy raiseNode) {
            int intBase;
            if (PGuards.isNoValue(base)) {
                intBase = 10;
            } else if (base instanceof Integer) {
                intBase = (int) base;
                checkBase(intBase, inliningTarget, invalidBase, raiseNode);
            } else {
                assert base instanceof PInt;
                PInt pintBase = (PInt) base;
                checkBase(pintBase, inliningTarget, invalidBase, raiseNode);
                intBase = (pintBase).intValue();
            }
            Object result = callInt(frame, inliningTarget, arg, raiseNode);
            if (result != PNone.NO_VALUE) {
                return result;
            }
            return stringToInt(frame, cls, castToStringNode.execute(arg), intBase, arg, inliningTarget,
                            isPrimitiveIntProfile, notSimpleDecimalLiteralProfile, invalidValueProfile, bigIntegerProfile,
                            primitiveIntProfile, fullIntProfile, factory, raiseNode);
        }

        // other

        @Specialization(guards = "isNoValue(base)")
        static Object createInt(Object cls, PythonNativeVoidPtr arg, @SuppressWarnings("unused") PNone base,
                        @Bind("this") Node inliningTarget,
                        @Shared("primitiveInt") @Cached IsBuiltinClassExactProfile isPrimitiveIntProfile) {
            if (isPrimitiveInt(inliningTarget, cls, isPrimitiveIntProfile)) {
                return arg;
            } else {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw new IllegalStateException("cannot wrap void ptr in int subclass");
            }
        }

        @Specialization(guards = "isNoValue(none)")
        static Object createInt(Object cls, @SuppressWarnings("unused") PNone none, @SuppressWarnings("unused") PNone base,
                        @Bind("this") Node inliningTarget,
                        @Shared("primitiveInt") @Cached IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        @Shared @Cached PythonObjectFactory factory) {
            if (isPrimitiveInt(inliningTarget, cls, isPrimitiveIntProfile)) {
                return 0;
            }
            return factory.createInt(cls, 0);
        }

        @SuppressWarnings("unused")
        @Specialization(guards = {"!isString(arg)", "!isBytes(arg)", "!isNoValue(base)"})
        static Object fail(Object cls, Object arg, Object base,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.INT_CANT_CONVERT_STRING_WITH_EXPL_BASE);
        }

        @Specialization(guards = {"isNoValue(base)", "!isNoValue(obj)", "!isHandledType(obj)"})
        @SuppressWarnings("truffle-static-method")
        @InliningCutoff
        @Megamorphic
        Object createIntGeneric(VirtualFrame frame, Object cls, Object obj, @SuppressWarnings("unused") PNone base,
                        @Bind("this") Node inliningTarget,
                        @Cached("createFor(this)") IndirectCallData indirectCallData,
                        @Cached PyIndexCheckNode indexCheckNode,
                        @Cached PyLongCheckExactNode longCheckExact,
                        @Exclusive @Cached IsBuiltinClassExactProfile isPrimitiveIntProfile,
                        @CachedLibrary(limit = "3") PythonBufferAcquireLibrary bufferAcquireLib,
                        @CachedLibrary(limit = "3") PythonBufferAccessLibrary bufferLib,
                        @Exclusive @Cached InlinedBranchProfile notSimpleDecimalLiteralProfile,
                        @Exclusive @Cached InlinedBranchProfile invalidValueProfile,
                        @Exclusive @Cached InlinedBranchProfile bigIntegerProfile,
                        @Exclusive @Cached InlinedBranchProfile primitiveIntProfile,
                        @Exclusive @Cached InlinedBranchProfile fullIntProfile,
                        @Shared @Cached PythonObjectFactory factory,
                        @Exclusive @Cached PRaiseNode.Lazy raiseNode) {
            /*
             * This method (together with callInt and callIndex) reflects the logic of PyNumber_Long
             * in CPython. We don't use PythonObjectLibrary here since the original CPython function
             * does not use any of the conversion functions (such as _PyLong_AsInt or
             * PyNumber_Index) either, but it reimplements the logic in a slightly different way
             * (e.g. trying __int__ before __index__ whereas _PyLong_AsInt does it the other way)
             * and also with specific exception messages which are expected by Python unittests.
             * This unfortunately means that this method relies on the internal logic of NO_VALUE
             * return values representing missing magic methods which should be ideally hidden by
             * PythonObjectLibrary.
             */
            Object result = callInt(frame, inliningTarget, obj, raiseNode);
            if (result == PNone.NO_VALUE) {
                result = callIndex(frame, inliningTarget, obj, raiseNode);
                if (result == PNone.NO_VALUE) {
                    Object truncResult = callTrunc(frame, inliningTarget, obj, indexCheckNode, raiseNode);
                    if (truncResult == PNone.NO_VALUE) {
                        Object buffer;
                        try {
                            buffer = bufferAcquireLib.acquireReadonly(obj, frame, indirectCallData);
                        } catch (PException e) {
                            throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.ARG_MUST_BE_STRING_OR_BYTELIKE_OR_NUMBER, "int()", obj);
                        }
                        try {
                            String number = newString(bufferLib.getInternalOrCopiedByteArray(buffer), 0, bufferLib.getBufferLength(buffer));
                            return stringToInt(frame, cls, number, 10, obj, inliningTarget, isPrimitiveIntProfile,
                                            notSimpleDecimalLiteralProfile, invalidValueProfile, bigIntegerProfile, primitiveIntProfile, fullIntProfile, factory, raiseNode);
                        } finally {
                            bufferLib.release(buffer, frame, indirectCallData);
                        }
                    }
                    if (isIntegerType(truncResult)) {
                        result = truncResult;
                    } else {
                        result = callIndex(frame, inliningTarget, truncResult, raiseNode);
                        if (result == PNone.NO_VALUE) {
                            result = callInt(frame, inliningTarget, truncResult, raiseNode);
                            if (result == PNone.NO_VALUE) {
                                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.RETURNED_NON_INTEGRAL, "__trunc__", truncResult);
                            }
                        }
                    }
                }
            }

            // If a subclass of int is returned by __int__ or __index__, a conversion to int is
            // performed and a DeprecationWarning should be triggered (see PyNumber_Long).
            if (!longCheckExact.execute(inliningTarget, result)) {
                getWarnNode().warnFormat(frame, null, PythonBuiltinClassType.DeprecationWarning, 1,
                                ErrorMessages.WARN_P_RETURNED_NON_P,
                                obj, "__int__/__index__", "int", result, "int");
                if (PGuards.isPInt(result)) {
                    result = ((PInt) result).getValue();
                } else if (PGuards.isBoolean(result)) {
                    result = (boolean) result ? 1 : 0;
                }
            }
            return createInt(cls, result, inliningTarget, isPrimitiveIntProfile, bigIntegerProfile, primitiveIntProfile, fullIntProfile, factory);
        }

        protected static boolean isIntegerType(Object obj) {
            return PGuards.isBoolean(obj) || PGuards.isInteger(obj) || PGuards.isPInt(obj);
        }

        protected static boolean isHandledType(Object obj) {
            return PGuards.isInteger(obj) || obj instanceof Double || obj instanceof Boolean || PGuards.isString(obj) || PGuards.isBytes(obj) || obj instanceof PythonNativeVoidPtr;
        }

        private Object callIndex(VirtualFrame frame, Node inliningTarget, Object obj, PRaiseNode.Lazy raiseNode) {
            if (callIndexNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                callIndexNode = insert(LookupAndCallUnaryNode.create(SpecialMethodSlot.Index));
            }
            Object result = callIndexNode.executeObject(frame, obj);
            // the case when the result is NO_VALUE (i.e. the object does not provide __index__)
            // is handled in createIntGeneric
            if (result != PNone.NO_VALUE && !isIntegerType(result)) {
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.RETURNED_NON_INT, J___INDEX__, result);
            }
            return result;
        }

        private Object callTrunc(VirtualFrame frame, Node inliningTarget, Object obj, PyIndexCheckNode indexCheckNode, PRaiseNode.Lazy raiseNode) {
            if (callTruncNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                callTruncNode = insert(LookupAndCallUnaryNode.create(T___TRUNC__));
            }
            Object result = callTruncNode.executeObject(frame, obj);
            if (result != PNone.NO_VALUE) {
                getWarnNode().warnEx(frame, DeprecationWarning, ErrorMessages.WARN_DELEGATION_OF_INT_TO_TRUNC_IS_DEPRECATED, 1);
                if (indexCheckNode.execute(inliningTarget, result)) {
                    return callIndex(frame, inliningTarget, result, raiseNode);
                } else {
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.RETURNED_NON_INTEGRAL, J___TRUNC__, result);
                }
            }
            return result;
        }

        private Object callInt(VirtualFrame frame, Node inliningTarget, Object object, PRaiseNode.Lazy raiseNode) {
            if (callIntNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                callIntNode = insert(LookupAndCallUnaryNode.create(SpecialMethodSlot.Int));
            }
            Object result = callIntNode.executeObject(frame, object);
            if (result != PNone.NO_VALUE && !isIntegerType(result)) {
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.RETURNED_NON_INT, T___INT__, result);
            }
            return result;
        }

        private WarnNode getWarnNode() {
            if (warnNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                warnNode = insert(WarnNode.create());
            }
            return warnNode;
        }

        private String toString(PBytesLike pByteArray) {
            if (toByteArrayNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                toByteArrayNode = insert(BytesNodes.ToBytesNode.create());
            }
            return newString(toByteArrayNode.execute(pByteArray));
        }

        @TruffleBoundary(allowInlining = true)
        private static String newString(byte[] bytes) {
            return new String(bytes);
        }

        @TruffleBoundary(allowInlining = true)
        private static String newString(byte[] bytes, int offset, int length) {
            return new String(bytes, offset, length);
        }
    }

    // bool([x])
    @Builtin(name = J_BOOL, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 2, constructsClass = PythonBuiltinClassType.Boolean, base = PythonBuiltinClassType.PInt, doc = """
                    bool(x) -> bool

                    Returns True when the argument x is true, False otherwise.
                    The builtins True and False are the only two instances of the class bool.
                    The class bool is a subclass of the class int, and cannot be subclassed.""")
    @GenerateNodeFactory
    @ReportPolymorphism
    public abstract static class BoolNode extends PythonBinaryBuiltinNode {
        @Specialization
        public static boolean bool(VirtualFrame frame, @SuppressWarnings("unused") Object cls, Object obj,
                        @Bind("this") Node inliningTarget,
                        @Cached PyObjectIsTrueNode isTrue) {
            return isTrue.execute(frame, inliningTarget, obj);
        }
    }

    // list([iterable])
    @Builtin(name = J_LIST, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PList, doc = """
                    Built-in mutable sequence.

                    If no argument is given, the constructor creates a new empty list.
                    The argument must be an iterable if specified.""")
    @GenerateNodeFactory
    public abstract static class ListNode extends PythonVarargsBuiltinNode {
        @Specialization
        protected PList constructList(Object cls, @SuppressWarnings("unused") Object[] arguments, @SuppressWarnings("unused") PKeyword[] keywords,
                        @Cached PythonObjectFactory factory) {
            return factory.createList(cls);
        }
    }

    // object()
    @Builtin(name = J_OBJECT, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PythonObject, doc = """
                    The base class of the class hierarchy.

                    When called, it accepts no arguments and returns a new featureless
                    instance that has no instance attributes and cannot be given any.
                    """)
    @GenerateNodeFactory
    public abstract static class ObjectNode extends PythonVarargsBuiltinNode {

        @Child private SplitArgsNode splitArgsNode;
        @Child private ReportAbstractClassNode reportAbstractClassNode;

        @GenerateInline(false) // Used lazily
        abstract static class ReportAbstractClassNode extends PNodeWithContext {
            public abstract PException execute(VirtualFrame frame, Object type);

            @Specialization
            static PException report(VirtualFrame frame, Object type,
                            @Bind("this") Node inliningTarget,
                            @Cached PyObjectCallMethodObjArgs callSort,
                            @Cached PyObjectCallMethodObjArgs callJoin,
                            @Cached PyObjectSizeNode sizeNode,
                            @Cached ReadAttributeFromObjectNode readAttributeFromObjectNode,
                            @Cached CastToTruffleStringNode cast,
                            @Cached ListNodes.ConstructListNode constructListNode,
                            @Cached PRaiseNode raiseNode) {
                PList list = constructListNode.execute(frame, readAttributeFromObjectNode.execute(type, T___ABSTRACTMETHODS__));
                int methodCount = sizeNode.execute(frame, inliningTarget, list);
                callSort.execute(frame, inliningTarget, list, T_SORT);
                TruffleString joined = cast.execute(inliningTarget, callJoin.execute(frame, inliningTarget, T_COMMA_SPACE, T_JOIN, list));
                throw raiseNode.raise(TypeError, ErrorMessages.CANT_INSTANTIATE_ABSTRACT_CLASS_WITH_ABSTRACT_METHODS, type, methodCount > 1 ? "s" : "", joined);
            }
        }

        @GenerateInline
        @GenerateCached(false)
        @ImportStatic(SpecialMethodSlot.class)
        abstract static class CheckExcessArgsNode extends Node {
            abstract void execute(Node inliningTarget, Object type, Object[] args, PKeyword[] kwargs);

            @Specialization(guards = {"args.length == 0", "kwargs.length == 0"})
            @SuppressWarnings("unused")
            static void doNothing(Object type, Object[] args, PKeyword[] kwargs) {
            }

            @Fallback
            @SuppressWarnings("unused")
            static void check(Node inliningTarget, Object type, Object[] args, PKeyword[] kwargs,
                            @Cached(parameters = "Init", inline = false) LookupCallableSlotInMRONode lookupInit,
                            @Cached(parameters = "New", inline = false) LookupCallableSlotInMRONode lookupNew,
                            @Cached TypeNodes.CheckCallableIsSpecificBuiltinNode checkSlotIs,
                            @Cached PRaiseNode.Lazy raiseNode) {
                if (!checkSlotIs.execute(inliningTarget, lookupNew.execute(type), BuiltinConstructorsFactory.ObjectNodeFactory.getInstance())) {
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.NEW_TAKES_ONE_ARG);
                }
                if (checkSlotIs.execute(inliningTarget, lookupInit.execute(type), ObjectBuiltinsFactory.InitNodeFactory.getInstance())) {
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.NEW_TAKES_NO_ARGS, type);
                }
            }
        }

        @Override
        public final Object varArgExecute(VirtualFrame frame, @SuppressWarnings("unused") Object self, Object[] arguments, PKeyword[] keywords) throws VarargsBuiltinDirectInvocationNotSupported {
            if (splitArgsNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                splitArgsNode = insert(SplitArgsNode.create());
            }
            return execute(frame, arguments[0], splitArgsNode.executeCached(arguments), keywords);
        }

        @Specialization(guards = {"!self.needsNativeAllocation()"})
        Object doManagedObject(VirtualFrame frame, PythonManagedClass self, Object[] varargs, PKeyword[] kwargs,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CheckExcessArgsNode checkExcessArgsNode,
                        @Shared @Cached PythonObjectFactory factory) {
            checkExcessArgsNode.execute(inliningTarget, self, varargs, kwargs);
            if (self.isAbstractClass()) {
                throw reportAbstractClass(frame, self);
            }
            return factory.createPythonObject(self);
        }

        @Specialization
        static Object doBuiltinTypeType(PythonBuiltinClassType self, Object[] varargs, PKeyword[] kwargs,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CheckExcessArgsNode checkExcessArgsNode,
                        @Shared @Cached PythonObjectFactory factory) {
            checkExcessArgsNode.execute(inliningTarget, self, varargs, kwargs);
            return factory.createPythonObject(self);
        }

        @Specialization(guards = "self.needsNativeAllocation()")
        @SuppressWarnings("truffle-static-method")
        @InliningCutoff
        Object doNativeObjectIndirect(VirtualFrame frame, PythonManagedClass self, Object[] varargs, PKeyword[] kwargs,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CheckExcessArgsNode checkExcessArgsNode,
                        @Shared @Cached CallNativeGenericNewNode callNativeGenericNewNode) {
            checkExcessArgsNode.execute(inliningTarget, self, varargs, kwargs);
            if (self.isAbstractClass()) {
                throw reportAbstractClass(frame, self);
            }
            return callNativeGenericNewNode.execute(inliningTarget, self);
        }

        @Specialization(guards = "isNativeClass(self)")
        @SuppressWarnings("truffle-static-method")
        @InliningCutoff
        Object doNativeObjectDirect(VirtualFrame frame, Object self, Object[] varargs, PKeyword[] kwargs,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached CheckExcessArgsNode checkExcessArgsNode,
                        @Exclusive @Cached TypeNodes.GetTypeFlagsNode getTypeFlagsNode,
                        @Shared @Cached CallNativeGenericNewNode callNativeGenericNewNode) {
            checkExcessArgsNode.execute(inliningTarget, self, varargs, kwargs);
            if ((getTypeFlagsNode.execute(self) & TypeFlags.IS_ABSTRACT) != 0) {
                throw reportAbstractClass(frame, self);
            }
            return callNativeGenericNewNode.execute(inliningTarget, self);
        }

        @GenerateInline
        @GenerateCached(false)
        protected abstract static class CallNativeGenericNewNode extends Node {
            abstract Object execute(Node inliningTarget, Object cls);

            @Specialization
            static Object call(Object cls,
                            @Cached(inline = false) PythonToNativeNode toNativeNode,
                            @Cached(inline = false) NativeToPythonTransferNode toPythonNode,
                            @Cached(inline = false) PCallCapiFunction callCapiFunction) {
                return toPythonNode.execute(callCapiFunction.call(FUN_PY_OBJECT_NEW, toNativeNode.execute(cls)));
            }
        }

        @SuppressWarnings("unused")
        @Fallback
        Object fallback(Object o, Object[] varargs, PKeyword[] kwargs) {
            throw raise(TypeError, ErrorMessages.IS_NOT_TYPE_OBJ, "object.__new__(X): X", o);
        }

        @InliningCutoff
        private PException reportAbstractClass(VirtualFrame frame, Object type) {
            if (reportAbstractClassNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                reportAbstractClassNode = insert(ReportAbstractClassNodeGen.create());
            }
            return reportAbstractClassNode.execute(frame, type);
        }
    }

    // range(stop)
    // range(start, stop[, step])
    @Builtin(name = J_RANGE, minNumOfPositionalArgs = 2, maxNumOfPositionalArgs = 4, constructsClass = PythonBuiltinClassType.PRange, doc = """
                    range(stop) -> range object
                    range(start, stop[, step]) -> range object

                    Return an object that produces a sequence of integers from start (inclusive)
                    to stop (exclusive) by step.  range(i, j) produces i, i+1, i+2, ..., j-1.
                    start defaults to 0, and stop is omitted!  range(4) produces 0, 1, 2, 3.
                    These are exactly the valid indices for a list of 4 elements.
                    When step is given, it specifies the increment (or decrement).""")
    @GenerateNodeFactory
    @ReportPolymorphism
    public abstract static class RangeNode extends PythonQuaternaryBuiltinNode {
        // stop
        @Specialization(guards = "isStop(start, stop, step)")
        static Object doIntStop(Object cls, int stop, @SuppressWarnings("unused") PNone start, @SuppressWarnings("unused") PNone step,
                        @Bind("this") Node inliningTarget,
                        @Shared("exceptionProfile") @Cached InlinedBranchProfile exceptionProfile,
                        @Shared("lenOfRangeNodeExact") @Cached LenOfIntRangeNodeExact lenOfRangeNodeExact,
                        @Shared("createBigRangeNode") @Cached RangeNodes.CreateBigRangeNode createBigRangeNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            return doInt(cls, 0, stop, 1, inliningTarget, exceptionProfile, lenOfRangeNodeExact, createBigRangeNode, factory, raiseNode);
        }

        @Specialization(guards = "isStop(start, stop, step)")
        static Object doPintStop(Object cls, PInt stop, @SuppressWarnings("unused") PNone start, @SuppressWarnings("unused") PNone step,
                        @Bind("this") Node inliningTarget,
                        @Shared("lenOfRangeNode") @Cached RangeNodes.LenOfRangeNode lenOfRangeNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            return doPint(cls, factory.createInt(0), stop, factory.createInt(1), inliningTarget, lenOfRangeNode, factory, raiseNode);
        }

        @Specialization(guards = "isStop(start, stop, step)")
        static Object doGenericStop(VirtualFrame frame, Object cls, Object stop, @SuppressWarnings("unused") PNone start, @SuppressWarnings("unused") PNone step,
                        @Bind("this") Node inliningTarget,
                        @Shared("exceptionProfile") @Cached InlinedBranchProfile exceptionProfile,
                        @Shared("lenOfRangeNodeExact") @Cached LenOfIntRangeNodeExact lenOfRangeNodeExact,
                        @Shared("createBigRangeNode") @Cached RangeNodes.CreateBigRangeNode createBigRangeNode,
                        @Shared("cast") @Cached CastToJavaIntExactNode cast,
                        @Shared("overflowProfile") @Cached IsBuiltinObjectProfile overflowProfile,
                        @Shared("indexNode") @Cached PyNumberIndexNode indexNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            return doGeneric(frame, cls, 0, stop, 1, inliningTarget, exceptionProfile, lenOfRangeNodeExact, createBigRangeNode, cast, overflowProfile, indexNode, factory, raiseNode);
        }

        // start stop
        @Specialization(guards = "isStartStop(start, stop, step)")
        static Object doIntStartStop(Object cls, int start, int stop, @SuppressWarnings("unused") PNone step,
                        @Bind("this") Node inliningTarget,
                        @Shared("exceptionProfile") @Cached InlinedBranchProfile exceptionProfile,
                        @Shared("lenOfRangeNodeExact") @Cached LenOfIntRangeNodeExact lenOfRangeNodeExact,
                        @Shared("createBigRangeNode") @Cached RangeNodes.CreateBigRangeNode createBigRangeNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            return doInt(cls, start, stop, 1, inliningTarget, exceptionProfile, lenOfRangeNodeExact, createBigRangeNode, factory, raiseNode);
        }

        @Specialization(guards = "isStartStop(start, stop, step)")
        static Object doPintStartStop(Object cls, PInt start, PInt stop, @SuppressWarnings("unused") PNone step,
                        @Bind("this") Node inliningTarget,
                        @Shared("lenOfRangeNode") @Cached RangeNodes.LenOfRangeNode lenOfRangeNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            return doPint(cls, start, stop, factory.createInt(1), inliningTarget, lenOfRangeNode, factory, raiseNode);
        }

        @Specialization(guards = "isStartStop(start, stop, step)")
        static Object doGenericStartStop(VirtualFrame frame, Object cls, Object start, Object stop, @SuppressWarnings("unused") PNone step,
                        @Bind("this") Node inliningTarget,
                        @Shared("exceptionProfile") @Cached InlinedBranchProfile exceptionProfile,
                        @Shared("lenOfRangeNodeExact") @Cached LenOfIntRangeNodeExact lenOfRangeNodeExact,
                        @Shared("createBigRangeNode") @Cached RangeNodes.CreateBigRangeNode createBigRangeNode,
                        @Shared("cast") @Cached CastToJavaIntExactNode cast,
                        @Shared("overflowProfile") @Cached IsBuiltinObjectProfile overflowProfile,
                        @Shared("indexNode") @Cached PyNumberIndexNode indexNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            return doGeneric(frame, cls, start, stop, 1, inliningTarget, exceptionProfile, lenOfRangeNodeExact, createBigRangeNode, cast, overflowProfile, indexNode, factory, raiseNode);
        }

        // start stop step
        @Specialization
        static Object doInt(@SuppressWarnings("unused") Object cls, int start, int stop, int step,
                        @Bind("this") Node inliningTarget,
                        @Shared("exceptionProfile") @Cached InlinedBranchProfile exceptionProfile,
                        @Shared("lenOfRangeNodeExact") @Cached LenOfIntRangeNodeExact lenOfRangeNode,
                        @Shared("createBigRangeNode") @Cached RangeNodes.CreateBigRangeNode createBigRangeNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            if (step == 0) {
                throw raiseNode.get(inliningTarget).raise(ValueError, ARG_MUST_NOT_BE_ZERO, "range()", 3);
            }
            try {
                int len = lenOfRangeNode.executeInt(inliningTarget, start, stop, step);
                return factory.createIntRange(start, stop, step, len);
            } catch (OverflowException e) {
                exceptionProfile.enter(inliningTarget);
                return createBigRangeNode.execute(inliningTarget, start, stop, step);
            }
        }

        @Specialization
        static Object doPint(@SuppressWarnings("unused") Object cls, PInt start, PInt stop, PInt step,
                        @Bind("this") Node inliningTarget,
                        @Shared("lenOfRangeNode") @Cached RangeNodes.LenOfRangeNode lenOfRangeNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            if (step.isZero()) {
                throw raiseNode.get(inliningTarget).raise(ValueError, ARG_MUST_NOT_BE_ZERO, "range()", 3);
            }
            BigInteger len = lenOfRangeNode.execute(inliningTarget, start.getValue(), stop.getValue(), step.getValue());
            return factory.createBigRange(start, stop, step, factory.createInt(len));
        }

        @Specialization(guards = "isStartStopStep(start, stop, step)")
        static Object doGeneric(VirtualFrame frame, @SuppressWarnings("unused") Object cls, Object start, Object stop, Object step,
                        @Bind("this") Node inliningTarget,
                        @Shared("exceptionProfile") @Cached InlinedBranchProfile exceptionProfile,
                        @Shared("lenOfRangeNodeExact") @Cached LenOfIntRangeNodeExact lenOfRangeNodeExact,
                        @Shared("createBigRangeNode") @Cached RangeNodes.CreateBigRangeNode createBigRangeNode,
                        @Shared("cast") @Cached CastToJavaIntExactNode cast,
                        @Shared("overflowProfile") @Cached IsBuiltinObjectProfile overflowProfile,
                        @Shared("indexNode") @Cached PyNumberIndexNode indexNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Shared @Cached PRaiseNode.Lazy raiseNode) {
            Object lstart = indexNode.execute(frame, inliningTarget, start);
            Object lstop = indexNode.execute(frame, inliningTarget, stop);
            Object lstep = indexNode.execute(frame, inliningTarget, step);

            try {
                int istart = cast.execute(inliningTarget, lstart);
                int istop = cast.execute(inliningTarget, lstop);
                int istep = cast.execute(inliningTarget, lstep);
                return doInt(cls, istart, istop, istep, inliningTarget, exceptionProfile, lenOfRangeNodeExact, createBigRangeNode, factory, raiseNode);
            } catch (PException e) {
                e.expect(inliningTarget, OverflowError, overflowProfile);
                return createBigRangeNode.execute(inliningTarget, lstart, lstop, lstep);
            }
        }

        protected static boolean isStop(Object start, Object stop, Object step) {
            return isNoValue(start) && !isNoValue(stop) && isNoValue(step);
        }

        protected static boolean isStartStop(Object start, Object stop, Object step) {
            return !isNoValue(start) && !isNoValue(stop) && isNoValue(step);
        }

        protected static boolean isStartStopStep(Object start, Object stop, Object step) {
            return !isNoValue(start) && !isNoValue(stop) && !isNoValue(step);
        }
    }

    // set([iterable])
    @Builtin(name = J_SET, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PSet, doc = """
                    set() -> new empty set object
                    set(iterable) -> new set object

                    Build an unordered collection of unique elements.""")
    @GenerateNodeFactory
    public abstract static class SetNode extends PythonBuiltinNode {

        @Specialization
        public PSet setEmpty(Object cls, @SuppressWarnings("unused") Object arg,
                        @Cached PythonObjectFactory factory) {
            return factory.createSet(cls);
        }

    }

    // str(object='')
    // str(object=b'', encoding='utf-8', errors='strict')
    @Builtin(name = J_STR, minNumOfPositionalArgs = 1, parameterNames = {"cls", "object", "encoding", "errors"}, constructsClass = PythonBuiltinClassType.PString, doc = """
                    str(object='') -> str
                    str(bytes_or_buffer[, encoding[, errors]]) -> str

                    Create a new string object from the given object. If encoding or
                    errors is specified, then the object must expose a data buffer
                    that will be decoded using the given encoding and error handler.
                    Otherwise, returns the result of object.__str__() (if defined)
                    or repr(object).
                    encoding defaults to sys.getdefaultencoding().
                    errors defaults to 'strict'.""")
    @GenerateNodeFactory
    public abstract static class StrNode extends PythonBuiltinNode {

        public final Object executeWith(Object arg) {
            return executeWith(null, PythonBuiltinClassType.PString, arg, PNone.NO_VALUE, PNone.NO_VALUE);
        }

        public final Object executeWith(VirtualFrame frame, Object arg) {
            return executeWith(frame, PythonBuiltinClassType.PString, arg, PNone.NO_VALUE, PNone.NO_VALUE);
        }

        public abstract Object executeWith(VirtualFrame frame, Object cls, Object arg, Object encoding, Object errors);

        @Specialization(guards = {"!needsNativeAllocationNode.execute(inliningTarget, cls)", "isNoValue(arg)"}, limit = "1")
        @SuppressWarnings("unused")
        static Object strNoArgs(Object cls, PNone arg, Object encoding, Object errors,
                        @Bind("this") Node inliningTarget,
                        @SuppressWarnings("unused") @Exclusive @Cached TypeNodes.NeedsNativeAllocationNode needsNativeAllocationNode,
                        @Exclusive @Cached IsBuiltinClassExactProfile isPrimitiveProfile,
                        @Shared @Cached PythonObjectFactory factory) {
            return asPString(cls, T_EMPTY_STRING, inliningTarget, isPrimitiveProfile, factory);
        }

        @Specialization(guards = {"!needsNativeAllocationNode.execute(inliningTarget, cls)", "!isNoValue(obj)", "isNoValue(encoding)", "isNoValue(errors)"}, limit = "1")
        static Object strOneArg(VirtualFrame frame, Object cls, Object obj, @SuppressWarnings("unused") PNone encoding, @SuppressWarnings("unused") PNone errors,
                        @Bind("this") Node inliningTarget,
                        @SuppressWarnings("unused") @Exclusive @Cached TypeNodes.NeedsNativeAllocationNode needsNativeAllocationNode,
                        @Exclusive @Cached IsBuiltinClassExactProfile isPrimitiveProfile,
                        @Exclusive @Cached InlinedConditionProfile isStringProfile,
                        @Cached CastToTruffleStringNode castToTruffleStringNode,
                        @Exclusive @Cached PyObjectStrAsObjectNode strNode,
                        @Shared @Cached PythonObjectFactory factory) {
            Object result = strNode.execute(frame, inliningTarget, obj);

            // try to return a primitive if possible
            result = assertNoJavaString(result);
            if (isStringProfile.profile(inliningTarget, result instanceof TruffleString)) {
                return asPString(cls, (TruffleString) result, inliningTarget, isPrimitiveProfile, factory);
            }

            if (isPrimitiveProfile.profileClass(inliningTarget, cls, PythonBuiltinClassType.PString)) {
                // PyObjectStrAsObjectNode guarantees that the returned object is an instanceof of
                // 'str'
                return result;
            } else {
                try {
                    return asPString(cls, castToTruffleStringNode.execute(inliningTarget, result), inliningTarget, isPrimitiveProfile, factory);
                } catch (CannotCastException e) {
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    throw new IllegalStateException("asPstring result not castable to String");
                }
            }
        }

        @Specialization(guards = {"!needsNativeAllocationNode.execute(inliningTarget, cls)", "!isNoValue(encoding) || !isNoValue(errors)"}, limit = "3")
        static Object doBuffer(VirtualFrame frame, Object cls, Object obj, Object encoding, Object errors,
                        @Bind("this") Node inliningTarget,
                        @Cached("createFor(this)") IndirectCallData indirectCallData,
                        @SuppressWarnings("unused") @Exclusive @Cached TypeNodes.NeedsNativeAllocationNode needsNativeAllocationNode,
                        @Exclusive @Cached IsBuiltinClassExactProfile isPrimitiveProfile,
                        @Exclusive @Cached InlinedConditionProfile isStringProfile,
                        @Exclusive @Cached InlinedConditionProfile isPStringProfile,
                        @CachedLibrary("obj") PythonBufferAcquireLibrary acquireLib,
                        @CachedLibrary(limit = "1") PythonBufferAccessLibrary bufferLib,
                        @Cached("create(T_DECODE)") LookupAndCallTernaryNode callDecodeNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Cached PRaiseNode.Lazy raiseNode) {
            Object buffer;
            try {
                buffer = acquireLib.acquireReadonly(obj, frame, indirectCallData);
            } catch (PException e) {
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.NEED_BYTELIKE_OBJ, obj);
            }
            try {
                // TODO(fa): we should directly call '_codecs.decode'
                // TODO don't copy, CPython creates a memoryview
                PBytes bytesObj = factory.createBytes(bufferLib.getCopiedByteArray(buffer));
                Object en = encoding == PNone.NO_VALUE ? T_UTF8 : encoding;
                Object result = assertNoJavaString(callDecodeNode.execute(frame, bytesObj, en, errors));
                if (isStringProfile.profile(inliningTarget, result instanceof TruffleString)) {
                    return asPString(cls, (TruffleString) result, inliningTarget, isPrimitiveProfile, factory);
                } else if (isPStringProfile.profile(inliningTarget, result instanceof PString)) {
                    return result;
                }
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.P_S_RETURNED_NON_STRING, bytesObj, "decode", result);
            } finally {
                bufferLib.release(buffer, frame, indirectCallData);
            }
        }

        /**
         * logic similar to
         * {@code unicode_subtype_new(PyTypeObject *type, PyObject *args, PyObject *kwds)} from
         * CPython {@code unicodeobject.c} we have to first create a temporary string, then fill it
         * into a natively allocated subtype structure
         */
        @Specialization(guards = {"needsNativeAllocationNode.execute(inliningTarget, cls)", "isSubtypeOfString(frame, isSubtype, cls)", //
                        "isNoValue(encoding)", "isNoValue(errors)"}, limit = "1")
        static Object doNativeSubclass(VirtualFrame frame, Object cls, Object obj, @SuppressWarnings("unused") Object encoding, @SuppressWarnings("unused") Object errors,
                        @SuppressWarnings("unused") @Bind("this") Node inliningTarget,
                        @SuppressWarnings("unused") @Exclusive @Cached TypeNodes.NeedsNativeAllocationNode needsNativeAllocationNode,
                        @Cached @SuppressWarnings("unused") IsSubtypeNode isSubtype,
                        @Exclusive @Cached PyObjectStrAsObjectNode strNode,
                        @Cached CExtNodes.StringSubtypeNew subtypeNew) {
            if (obj == PNone.NO_VALUE) {
                return subtypeNew.call(cls, T_EMPTY_STRING);
            } else {
                return subtypeNew.call(cls, strNode.execute(frame, inliningTarget, obj));
            }
        }

        protected static boolean isSubtypeOfString(VirtualFrame frame, IsSubtypeNode isSubtypeNode, Object cls) {
            return isSubtypeNode.execute(frame, cls, PythonBuiltinClassType.PString);
        }

        private static Object asPString(Object cls, TruffleString str, Node inliningTarget, IsBuiltinClassExactProfile isPrimitiveProfile,
                        PythonObjectFactory factory) {
            if (isPrimitiveProfile.profileClass(inliningTarget, cls, PythonBuiltinClassType.PString)) {
                return str;
            } else {
                return factory.createString(cls, str);
            }
        }

        @NeverDefault
        public static StrNode create() {
            return BuiltinConstructorsFactory.StrNodeFactory.create(null);
        }
    }

    // tuple([iterable])
    @Builtin(name = J_TUPLE, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 2, constructsClass = PythonBuiltinClassType.PTuple, doc = """
                    Built-in immutable sequence.

                    If no argument is given, the constructor returns an empty tuple.
                    If iterable is specified the tuple is initialized from iterable's items.

                    If the argument is a tuple, the return value is the same object.""")
    @GenerateNodeFactory
    public abstract static class TupleNode extends PythonBinaryBuiltinNode {

        @Specialization(guards = "!needsNativeAllocationNode.execute(inliningTarget, cls)")
        static PTuple constructTuple(VirtualFrame frame, Object cls, Object iterable,
                        @SuppressWarnings("unused") @Bind("this") Node inliningTarget,
                        @SuppressWarnings("unused") @Shared @Cached TypeNodes.NeedsNativeAllocationNode needsNativeAllocationNode,
                        @Cached TupleNodes.ConstructTupleNode constructTupleNode) {
            return constructTupleNode.execute(frame, cls, iterable);
        }

        // delegate to tuple_subtype_new(PyTypeObject *type, PyObject *x)
        @Specialization(guards = {"needsNativeAllocationNode.execute(inliningTarget, cls)", "isSubtypeOfTuple(frame, isSubtype, cls)"}, limit = "1")
        @InliningCutoff
        static Object doNative(@SuppressWarnings("unused") VirtualFrame frame, Object cls, Object iterable,
                        @SuppressWarnings("unused") @Bind("this") Node inliningTarget,
                        @SuppressWarnings("unused") @Shared @Cached TypeNodes.NeedsNativeAllocationNode needsNativeAllocationNode,
                        @Cached @SuppressWarnings("unused") IsSubtypeNode isSubtype,
                        @Cached CExtNodes.TupleSubtypeNew subtypeNew) {
            return subtypeNew.call(cls, iterable);
        }

        protected static boolean isSubtypeOfTuple(VirtualFrame frame, IsSubtypeNode isSubtypeNode, Object cls) {
            return isSubtypeNode.execute(frame, cls, PythonBuiltinClassType.PTuple);
        }

        @Fallback
        static PTuple tupleObject(Object cls, @SuppressWarnings("unused") Object arg,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.IS_NOT_TYPE_OBJ, "'cls'", cls);
        }
    }

    // zip(*iterables)
    @Builtin(name = J_ZIP, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PZip, doc = """
                    zip(*iterables, strict=False) --> Yield tuples until an input is exhausted.

                       >>> list(zip('abcdefg', range(3), range(4)))
                       [('a', 0, 0), ('b', 1, 1), ('c', 2, 2)]

                    The zip object yields n-length tuples, where n is the number of iterables
                    passed as positional arguments to zip().  The i-th element in every tuple
                    comes from the i-th iterable argument to zip().  This continues until the
                    shortest argument is exhausted.

                    If strict is true and one of the arguments is exhausted before the others,
                    raise a ValueError.""")
    @GenerateNodeFactory
    public abstract static class ZipNode extends PythonBuiltinNode {
        static boolean isNoneOrEmptyPKeyword(Object value) {
            return PGuards.isPNone(value) || (value instanceof PKeyword[] kw && kw.length == 0);
        }

        @Specialization(guards = "isNoneOrEmptyPKeyword(kw)")
        static PZip zip(VirtualFrame frame, Object cls, Object[] args, @SuppressWarnings("unused") Object kw,
                        @Bind("this") Node inliningTarget,
                        @Exclusive @Cached PyObjectGetIter getIter,
                        @Shared @Cached PythonObjectFactory factory) {
            return zip(frame, inliningTarget, cls, args, false, getIter, factory);
        }

        @Specialization(guards = "kw.length == 1")
        static PZip zip(VirtualFrame frame, Object cls, Object[] args, PKeyword[] kw,
                        @Bind("this") Node inliningTarget,
                        @Cached TruffleString.EqualNode eqNode,
                        @Exclusive @Cached PyObjectGetIter getIter,
                        @Cached PyObjectIsTrueNode isTrueNode,
                        @Cached InlinedConditionProfile profile,
                        @Shared @Cached PythonObjectFactory factory,
                        @Exclusive @Cached PRaiseNode.Lazy raiseNode) {
            if (profile.profile(inliningTarget, eqNode.execute(kw[0].getName(), T_STRICT, TS_ENCODING))) {
                return zip(frame, inliningTarget, cls, args, isTrueNode.execute(frame, inliningTarget, kw[0].getValue()), getIter, factory);
            }
            throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.S_IS_AN_INVALID_ARG_FOR_S, kw[0].getName(), T_ZIP);
        }

        @Specialization(guards = "kw.length != 1")
        static Object zip(@SuppressWarnings("unused") Object cls, @SuppressWarnings("unused") Object[] args, PKeyword[] kw,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.S_TAKES_AT_MOST_ONE_KEYWORD_ARGUMENT_D_GIVEN, T_ZIP, kw.length);
        }

        private static PZip zip(VirtualFrame frame, Node inliningTarget, Object cls, Object[] args, boolean strict, PyObjectGetIter getIter, PythonObjectFactory factory) {
            Object[] iterables = new Object[args.length];
            LoopNode.reportLoopCount(inliningTarget, args.length);
            for (int i = 0; i < args.length; i++) {
                Object item = args[i];
                iterables[i] = getIter.execute(frame, inliningTarget, item);
            }
            return factory.createZip(cls, iterables, strict);
        }
    }

    // function(code, globals[, name[, argdefs[, closure]]])
    @Builtin(name = "function", minNumOfPositionalArgs = 3, parameterNames = {"$cls", "code", "globals", "name", "argdefs",
                    "closure"}, constructsClass = PythonBuiltinClassType.PFunction, isPublic = false)
    @GenerateNodeFactory
    public abstract static class FunctionNode extends PythonBuiltinNode {

        @Specialization
        static PFunction function(@SuppressWarnings("unused") Object cls, PCode code, PDict globals, TruffleString name, @SuppressWarnings("unused") PNone defaultArgs,
                        @SuppressWarnings("unused") PNone closure,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createFunction(name, code, globals, null);
        }

        @Specialization
        static PFunction function(@SuppressWarnings("unused") Object cls, PCode code, PDict globals, @SuppressWarnings("unused") PNone name, @SuppressWarnings("unused") PNone defaultArgs,
                        PTuple closure,
                        @Bind("this") Node inliningTarget,
                        @Shared("getObjectArrayNode") @Cached GetObjectArrayNode getObjectArrayNode,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createFunction(T_LAMBDA_NAME, code, globals, PCell.toCellArray(getObjectArrayNode.execute(inliningTarget, closure)));
        }

        @Specialization
        static PFunction function(@SuppressWarnings("unused") Object cls, PCode code, PDict globals, @SuppressWarnings("unused") PNone name, @SuppressWarnings("unused") PNone defaultArgs,
                        @SuppressWarnings("unused") PNone closure,
                        @SuppressWarnings("unused") @Shared("getObjectArrayNode") @Cached GetObjectArrayNode getObjectArrayNode,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createFunction(T_LAMBDA_NAME, code, globals, null);
        }

        @Specialization
        static PFunction function(@SuppressWarnings("unused") Object cls, PCode code, PDict globals, TruffleString name, @SuppressWarnings("unused") PNone defaultArgs, PTuple closure,
                        @Bind("this") Node inliningTarget,
                        @Shared("getObjectArrayNode") @Cached GetObjectArrayNode getObjectArrayNode,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createFunction(name, code, globals, PCell.toCellArray(getObjectArrayNode.execute(inliningTarget, closure)));
        }

        @Specialization
        static PFunction function(@SuppressWarnings("unused") Object cls, PCode code, PDict globals, @SuppressWarnings("unused") PNone name, PTuple defaultArgs,
                        @SuppressWarnings("unused") PNone closure,
                        @Bind("this") Node inliningTarget,
                        @Shared("getObjectArrayNode") @Cached GetObjectArrayNode getObjectArrayNode,
                        @Shared @Cached PythonObjectFactory factory) {
            // TODO split defaults of positional args from kwDefaults
            return factory.createFunction(code.getName(), code, globals, getObjectArrayNode.execute(inliningTarget, defaultArgs), null, null);
        }

        @Specialization
        static PFunction function(@SuppressWarnings("unused") Object cls, PCode code, PDict globals, TruffleString name, PTuple defaultArgs, @SuppressWarnings("unused") PNone closure,
                        @Bind("this") Node inliningTarget,
                        @Shared("getObjectArrayNode") @Cached GetObjectArrayNode getObjectArrayNode,
                        @Shared @Cached PythonObjectFactory factory) {
            // TODO split defaults of positional args from kwDefaults
            return factory.createFunction(name, code, globals, getObjectArrayNode.execute(inliningTarget, defaultArgs), null, null);
        }

        @Specialization
        static PFunction function(@SuppressWarnings("unused") Object cls, PCode code, PDict globals, TruffleString name, PTuple defaultArgs, PTuple closure,
                        @Bind("this") Node inliningTarget,
                        @Shared("getObjectArrayNode") @Cached GetObjectArrayNode getObjectArrayNode,
                        @Shared @Cached PythonObjectFactory factory) {
            // TODO split defaults of positional args from kwDefaults
            return factory.createFunction(name, code, globals, getObjectArrayNode.execute(inliningTarget, defaultArgs), null, PCell.toCellArray(getObjectArrayNode.execute(inliningTarget, closure)));
        }

        @Fallback
        @SuppressWarnings("unused")
        static PFunction function(@SuppressWarnings("unused") Object cls, Object code, Object globals, Object name, Object defaultArgs, Object closure,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.FUNC_CONSTRUCTION_NOT_SUPPORTED, cls, code, globals, name, defaultArgs, closure);
        }
    }

    // builtin-function(method-def, self, module)
    @Builtin(name = "method_descriptor", minNumOfPositionalArgs = 3, maxNumOfPositionalArgs = 6, constructsClass = PythonBuiltinClassType.PBuiltinFunction, isPublic = false)
    @GenerateNodeFactory
    public abstract static class BuiltinFunctionNode extends PythonBuiltinNode {
        @Specialization
        @SuppressWarnings("unused")
        static PFunction function(Object cls, Object method_def, Object def, Object name, Object module,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, "method_descriptor");
        }
    }

    // type(object, bases, dict)
    @Builtin(name = J_TYPE, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, needsFrame = true, constructsClass = PythonBuiltinClassType.PythonClass)
    @GenerateNodeFactory
    public abstract static class TypeNode extends PythonVarargsBuiltinNode {
        @Child private IsSubtypeNode isSubtypeNode;
        @Child private IsAcceptableBaseNode isAcceptableBaseNode;

        public abstract Object execute(VirtualFrame frame, Object cls, Object name, Object bases, Object dict, PKeyword[] kwds);

        @Override
        public final Object execute(VirtualFrame frame, Object self, Object[] arguments, PKeyword[] keywords) {
            if (arguments.length == 3) {
                return execute(frame, self, arguments[0], arguments[1], arguments[2], keywords);
            } else {
                throw raise(TypeError, ErrorMessages.TAKES_EXACTLY_D_ARGUMENTS_D_GIVEN, "type.__new__", 3, arguments.length);
            }
        }

        @Override
        public Object varArgExecute(VirtualFrame frame, Object self, Object[] arguments, PKeyword[] keywords) {
            if (arguments.length == 4) {
                return execute(frame, arguments[0], arguments[1], arguments[2], arguments[3], keywords);
            } else if (arguments.length == 3) {
                return execute(frame, self, arguments[0], arguments[1], arguments[2], keywords);
            } else {
                throw raise(TypeError, ErrorMessages.TAKES_EXACTLY_D_ARGUMENTS_D_GIVEN, "type.__new__", 3, arguments.length);
            }
        }

        @Specialization(guards = "isString(wName)")
        @SuppressWarnings("truffle-static-method")
        Object typeNew(VirtualFrame frame, Object cls, Object wName, PTuple bases, PDict namespaceOrig, PKeyword[] kwds,
                        @Bind("this") Node inliningTarget,
                        @Cached GetClassNode getClassNode,
                        @Cached("create(New)") LookupCallableSlotInMRONode getNewFuncNode,
                        @Cached TypeBuiltins.BindNew bindNew,
                        @Exclusive @Cached IsTypeNode isTypeNode,
                        @Cached PyObjectLookupAttr lookupMroEntriesNode,
                        @Cached CastToTruffleStringNode castStr,
                        @Cached CallNode callNewFuncNode,
                        @Cached CreateTypeNode createType,
                        @Cached GetObjectArrayNode getObjectArrayNode) {
            // Determine the proper metatype to deal with this
            TruffleString name = castStr.execute(inliningTarget, wName);
            Object metaclass = cls;
            Object winner = calculateMetaclass(frame, inliningTarget, metaclass, bases, getClassNode, isTypeNode, lookupMroEntriesNode, getObjectArrayNode);
            if (winner != metaclass) {
                Object newFunc = getNewFuncNode.execute(winner);
                if (newFunc instanceof PBuiltinMethod && (((PBuiltinMethod) newFunc).getBuiltinFunction().getFunctionRootNode().getCallTarget() == getRootNode().getCallTarget())) {
                    metaclass = winner;
                    // the new metaclass has the same __new__ function as we are in, continue
                } else {
                    // Pass it to the winner
                    return callNewFuncNode.execute(frame, bindNew.execute(frame, inliningTarget, newFunc, winner), new Object[]{winner, name, bases, namespaceOrig}, kwds);
                }
            }

            return createType.execute(frame, namespaceOrig, name, bases, metaclass, kwds);
        }

        @Fallback
        Object generic(@SuppressWarnings("unused") Object cls, @SuppressWarnings("unused") Object name, Object bases, Object namespace, @SuppressWarnings("unused") PKeyword[] kwds) {
            if (!(bases instanceof PTuple)) {
                throw raise(TypeError, ErrorMessages.ARG_D_MUST_BE_S_NOT_P, "type.__new__()", 2, "tuple", bases);
            } else if (!(namespace instanceof PDict)) {
                throw raise(TypeError, ErrorMessages.ARG_D_MUST_BE_S_NOT_P, "type.__new__()", 3, "dict", bases);
            } else {
                throw CompilerDirectives.shouldNotReachHere("type fallback reached incorrectly");
            }
        }

        private Object calculateMetaclass(VirtualFrame frame, Node inliningTarget, Object cls, PTuple bases, GetClassNode getClassNode, IsTypeNode isTypeNode,
                        PyObjectLookupAttr lookupMroEntries, GetObjectArrayNode getObjectArrayNode) {
            Object winner = cls;
            for (Object base : getObjectArrayNode.execute(inliningTarget, bases)) {
                if (!isTypeNode.execute(inliningTarget, base) && lookupMroEntries.execute(frame, inliningTarget, base, T___MRO_ENTRIES__) != PNone.NO_VALUE) {
                    throw raise(TypeError, ErrorMessages.TYPE_DOESNT_SUPPORT_MRO_ENTRY_RESOLUTION);
                }
                if (!ensureIsAcceptableBaseNode().execute(base)) {
                    throw raise(TypeError, ErrorMessages.TYPE_IS_NOT_ACCEPTABLE_BASE_TYPE, base);
                }
                Object typ = getClassNode.execute(inliningTarget, base);
                if (isSubType(frame, winner, typ)) {
                    continue;
                } else if (isSubType(frame, typ, winner)) {
                    winner = typ;
                    continue;
                }
                throw raise(TypeError, ErrorMessages.METACLASS_CONFLICT);
            }
            return winner;
        }

        protected boolean isSubType(VirtualFrame frame, Object subclass, Object superclass) {
            if (isSubtypeNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                isSubtypeNode = insert(IsSubtypeNode.create());
            }
            return isSubtypeNode.execute(frame, subclass, superclass);
        }

        @NeverDefault
        public static TypeNode create() {
            return BuiltinConstructorsFactory.TypeNodeFactory.create();
        }

        @Specialization(guards = {"!isNoValue(bases)", "!isNoValue(dict)"})
        Object typeGeneric(VirtualFrame frame, Object cls, Object name, Object bases, Object dict, PKeyword[] kwds,
                        @Bind("this") Node inliningTarget,
                        @Cached TypeNode nextTypeNode,
                        @Exclusive @Cached IsTypeNode isTypeNode) {
            if (!(name instanceof TruffleString || name instanceof PString)) {
                throw raise(TypeError, ErrorMessages.MUST_BE_STRINGS_NOT_P, "type() argument 1", name);
            } else if (!(bases instanceof PTuple)) {
                throw raise(TypeError, ErrorMessages.MUST_BE_STRINGS_NOT_P, "type() argument 2", bases);
            } else if (!(dict instanceof PDict)) {
                throw raise(TypeError, ErrorMessages.MUST_BE_STRINGS_NOT_P, "type() argument 3", dict);
            } else if (!isTypeNode.execute(inliningTarget, cls)) {
                // TODO: this is actually allowed, deal with it
                throw raise(NotImplementedError, ErrorMessages.CREATING_CLASS_NON_CLS_META_CLS);
            }
            return nextTypeNode.execute(frame, cls, name, bases, dict, kwds);
        }

        private IsAcceptableBaseNode ensureIsAcceptableBaseNode() {
            if (isAcceptableBaseNode == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                isAcceptableBaseNode = insert(IsAcceptableBaseNode.create());
            }
            return isAcceptableBaseNode;
        }
    }

    @Builtin(name = J_MODULE, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PythonModule, isPublic = false, doc = """
                    Create a module object.

                    The name must be a string; the optional doc argument can have any type.""")
    @GenerateNodeFactory
    public abstract static class ModuleNode extends PythonBuiltinNode {
        @Specialization
        @SuppressWarnings("unused")
        static Object doType(PythonBuiltinClass self, Object[] varargs, PKeyword[] kwargs,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createPythonModule(self.getType());
        }

        @Specialization(guards = "!isPythonBuiltinClass(self)")
        @SuppressWarnings("unused")
        static Object doManaged(PythonManagedClass self, Object[] varargs, PKeyword[] kwargs,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createPythonModule(self);
        }

        @Specialization
        @SuppressWarnings("unused")
        static Object doType(PythonBuiltinClassType self, Object[] varargs, PKeyword[] kwargs,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createPythonModule(self);
        }

        @Specialization(guards = "isTypeNode.execute(inliningTarget, self)", limit = "1")
        @SuppressWarnings("unused")
        static Object doNative(PythonAbstractNativeObject self, Object[] varargs, PKeyword[] kwargs,
                        @Bind("this") Node inliningTarget,
                        @Cached IsTypeNode isTypeNode,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createPythonModule(self);
        }
    }

    @Builtin(name = "NotImplementedType", minNumOfPositionalArgs = 1, constructsClass = PythonBuiltinClassType.PNotImplemented, isPublic = false)
    @GenerateNodeFactory
    public abstract static class NotImplementedTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        public static PNotImplemented module(Object cls) {
            return PNotImplemented.NOT_IMPLEMENTED;
        }
    }

    @Builtin(name = "ellipsis", minNumOfPositionalArgs = 1, constructsClass = PythonBuiltinClassType.PEllipsis, isPublic = false)
    @GenerateNodeFactory
    public abstract static class EllipsisTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        public static PEllipsis call(Object cls) {
            return PEllipsis.INSTANCE;
        }
    }

    @Builtin(name = "NoneType", minNumOfPositionalArgs = 1, constructsClass = PythonBuiltinClassType.PNone, isPublic = false)
    @GenerateNodeFactory
    public abstract static class NoneTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        public static PNone module(Object cls) {
            return PNone.NONE;
        }
    }

    @Builtin(name = J_DICT_KEYS, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PDictKeysView, isPublic = false)
    @GenerateNodeFactory
    public abstract static class DictKeysTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object dictKeys(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, J_DICT_KEYS);
        }
    }

    @Builtin(name = J_DICT_KEYITERATOR, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PDictKeyIterator, isPublic = false)
    @GenerateNodeFactory
    public abstract static class DictKeysIteratorTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object dictKeys(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, J_DICT_KEYITERATOR);
        }
    }

    @Builtin(name = J_DICT_VALUES, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PDictValuesView, isPublic = false)
    @GenerateNodeFactory
    public abstract static class DictValuesTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object dictKeys(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, J_DICT_VALUES);
        }
    }

    @Builtin(name = J_DICT_VALUEITERATOR, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PDictValueIterator, isPublic = false)
    @GenerateNodeFactory
    public abstract static class DictValuesIteratorTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object dictKeys(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, J_DICT_VALUEITERATOR);
        }
    }

    @Builtin(name = J_DICT_ITEMS, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PDictItemsView, isPublic = false)
    @GenerateNodeFactory
    public abstract static class DictItemsTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object dictKeys(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, J_DICT_ITEMS);
        }
    }

    @Builtin(name = J_DICT_ITEMITERATOR, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PDictItemIterator, isPublic = false)
    @GenerateNodeFactory
    public abstract static class DictItemsIteratorTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object dictKeys(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, J_DICT_ITEMITERATOR);
        }
    }

    @Builtin(name = "iterator", takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PIterator, isPublic = false)
    @GenerateNodeFactory
    public abstract static class IteratorTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object iterator(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, "iterator");
        }
    }

    @Builtin(name = "arrayiterator", takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PArrayIterator, isPublic = false)
    @GenerateNodeFactory
    public abstract static class ArrayIteratorTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object iterator(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, "arrayiterator");
        }
    }

    @Builtin(name = "callable_iterator", takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PSentinelIterator, isPublic = false)
    @GenerateNodeFactory
    public abstract static class CallableIteratorTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object iterator(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, "callable_iterator");
        }
    }

    @Builtin(name = "foreign_iterator", takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PForeignArrayIterator, isPublic = false)
    @GenerateNodeFactory
    public abstract static class ForeignIteratorTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object iterator(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, "foreign_iterator");
        }
    }

    @Builtin(name = "generator", takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PGenerator, isPublic = false)
    @GenerateNodeFactory
    public abstract static class GeneratorTypeNode extends PythonBuiltinNode {
        @SuppressWarnings("unused")
        @Specialization
        static Object generator(Object args, Object kwargs,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, "generator");
        }
    }

    @Builtin(name = "method", minNumOfPositionalArgs = 3, constructsClass = PythonBuiltinClassType.PMethod, isPublic = false, doc = "Create a bound instance method object.")
    @GenerateNodeFactory
    public abstract static class MethodTypeNode extends PythonTernaryBuiltinNode {
        @Specialization
        static Object method(Object cls, PFunction func, Object self,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createMethod(cls, self, func);
        }

        @Specialization
        static Object methodBuiltin(@SuppressWarnings("unused") Object cls, PBuiltinFunction func, Object self,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createMethod(self, func);
        }

        @Specialization
        static Object methodGeneric(@SuppressWarnings("unused") Object cls, Object func, Object self,
                        @Bind("this") Node inliningTarget,
                        @Cached PyCallableCheckNode callableCheck,
                        @Shared @Cached PythonObjectFactory factory,
                        @Cached PRaiseNode.Lazy raiseNode) {
            if (callableCheck.execute(inliningTarget, func)) {
                return factory.createMethod(self, func);
            } else {
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.FIRST_ARG_MUST_BE_CALLABLE_S, "");
            }
        }
    }

    @Builtin(name = "builtin_function_or_method", minNumOfPositionalArgs = 3, constructsClass = PythonBuiltinClassType.PBuiltinFunctionOrMethod, isPublic = false)
    @GenerateNodeFactory
    public abstract static class BuiltinMethodTypeNode extends PythonBuiltinNode {
        @Specialization
        Object method(Object cls, Object self, PBuiltinFunction func,
                        @Cached PythonObjectFactory factory) {
            return factory.createBuiltinMethod(cls, self, func);
        }
    }

    @Builtin(name = "frame", constructsClass = PythonBuiltinClassType.PFrame, isPublic = false)
    @GenerateNodeFactory
    public abstract static class FrameTypeNode extends PythonBuiltinNode {
        @Specialization
        static Object call(
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(RuntimeError, ErrorMessages.CANNOT_CALL_CTOR_OF, "frame type");
        }
    }

    @Builtin(name = "TracebackType", constructsClass = PythonBuiltinClassType.PTraceback, isPublic = false, minNumOfPositionalArgs = 5, parameterNames = {"$cls", "tb_next", "tb_frame", "tb_lasti",
                    "tb_lineno"})
    @ArgumentClinic(name = "tb_lasti", conversion = ArgumentClinic.ClinicConversion.Index)
    @ArgumentClinic(name = "tb_lineno", conversion = ArgumentClinic.ClinicConversion.Index)
    @GenerateNodeFactory
    public abstract static class TracebackTypeNode extends PythonClinicBuiltinNode {
        @Specialization
        static Object createTraceback(@SuppressWarnings("unused") Object cls, PTraceback next, PFrame pframe, int lasti, int lineno,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createTracebackWithLasti(pframe, lineno, lasti, next);
        }

        @Specialization
        static Object createTraceback(@SuppressWarnings("unused") Object cls, @SuppressWarnings("unused") PNone next, PFrame pframe, int lasti, int lineno,
                        @Shared @Cached PythonObjectFactory factory) {
            return factory.createTracebackWithLasti(pframe, lineno, lasti, null);
        }

        @Specialization(guards = {"!isPTraceback(next)", "!isNone(next)"})
        @SuppressWarnings("unused")
        static Object errorNext(Object cls, Object next, Object frame, Object lasti, Object lineno,
                        @Shared @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.EXPECTED_TRACEBACK_OBJ_OR_NONE, next);
        }

        @Specialization(guards = "!isPFrame(frame)")
        @SuppressWarnings("unused")
        static Object errorFrame(Object cls, Object next, Object frame, Object lasti, Object lineno,
                        @Shared @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.TRACEBACK_TYPE_ARG_MUST_BE_FRAME, frame);
        }

        protected static boolean isPFrame(Object obj) {
            return obj instanceof PFrame;
        }

        @Override
        protected ArgumentClinicProvider getArgumentClinic() {
            return BuiltinConstructorsClinicProviders.TracebackTypeNodeClinicProviderGen.INSTANCE;
        }
    }

    @Builtin(name = "code", constructsClass = PythonBuiltinClassType.PCode, isPublic = false, minNumOfPositionalArgs = 16, numOfPositionalOnlyArgs = 18, parameterNames = {
                    "$cls", "argcount", "posonlyargcount", "kwonlyargcount", "nlocals", "stacksize", "flags", "codestring",
                    "constants", "names", "varnames", "filename", "name", "qualname", "firstlineno",
                    "linetable", "exceptiontable", "freevars", "cellvars"})
    @ArgumentClinic(name = "argcount", conversion = ArgumentClinic.ClinicConversion.Int)
    @ArgumentClinic(name = "posonlyargcount", conversion = ArgumentClinic.ClinicConversion.Int)
    @ArgumentClinic(name = "kwonlyargcount", conversion = ArgumentClinic.ClinicConversion.Int)
    @ArgumentClinic(name = "nlocals", conversion = ArgumentClinic.ClinicConversion.Int)
    @ArgumentClinic(name = "stacksize", conversion = ArgumentClinic.ClinicConversion.Int)
    @ArgumentClinic(name = "flags", conversion = ArgumentClinic.ClinicConversion.Int)
    @ArgumentClinic(name = "filename", conversion = ArgumentClinic.ClinicConversion.TString)
    @ArgumentClinic(name = "name", conversion = ArgumentClinic.ClinicConversion.TString)
    @ArgumentClinic(name = "qualname", conversion = ArgumentClinic.ClinicConversion.TString)
    @ArgumentClinic(name = "firstlineno", conversion = ArgumentClinic.ClinicConversion.Int)
    @GenerateNodeFactory
    public abstract static class CodeConstructorNode extends PythonClinicBuiltinNode {
        @Specialization
        PCode call(VirtualFrame frame, @SuppressWarnings("unused") Object cls, int argcount,
                        int posonlyargcount, int kwonlyargcount,
                        int nlocals, int stacksize, int flags,
                        PBytes codestring, PTuple constants, PTuple names, PTuple varnames,
                        TruffleString filename, TruffleString name, TruffleString qualname,
                        int firstlineno, PBytes linetable, @SuppressWarnings("unused") PBytes exceptiontable,
                        PTuple freevars, PTuple cellvars,
                        @Bind("this") Node inliningTarget,
                        @CachedLibrary(limit = "1") PythonBufferAccessLibrary bufferLib,
                        @Cached CodeNodes.CreateCodeNode createCodeNode,
                        @Cached GetObjectArrayNode getObjectArrayNode,
                        @Cached CastToTruffleStringNode castToTruffleStringNode) {
            byte[] codeBytes = bufferLib.getCopiedByteArray(codestring);
            byte[] linetableBytes = bufferLib.getCopiedByteArray(linetable);

            Object[] constantsArr = getObjectArrayNode.execute(inliningTarget, constants);
            TruffleString[] namesArr = objectArrayToTruffleStringArray(inliningTarget, getObjectArrayNode.execute(inliningTarget, names), castToTruffleStringNode);
            TruffleString[] varnamesArr = objectArrayToTruffleStringArray(inliningTarget, getObjectArrayNode.execute(inliningTarget, varnames), castToTruffleStringNode);
            TruffleString[] freevarsArr = objectArrayToTruffleStringArray(inliningTarget, getObjectArrayNode.execute(inliningTarget, freevars), castToTruffleStringNode);
            TruffleString[] cellcarsArr = objectArrayToTruffleStringArray(inliningTarget, getObjectArrayNode.execute(inliningTarget, cellvars), castToTruffleStringNode);

            return createCodeNode.execute(frame, argcount, posonlyargcount, kwonlyargcount,
                            nlocals, stacksize, flags,
                            codeBytes, constantsArr, namesArr,
                            varnamesArr, freevarsArr, cellcarsArr,
                            filename, name, qualname,
                            firstlineno, linetableBytes);
        }

        @Fallback
        @SuppressWarnings("unused")
        static PCode call(Object cls, Object argcount, Object kwonlyargcount, Object posonlyargcount,
                        Object nlocals, Object stacksize, Object flags,
                        Object codestring, Object constants, Object names, Object varnames,
                        Object filename, Object name, Object qualname,
                        Object firstlineno, Object linetable, Object exceptiontable,
                        Object freevars, Object cellvars,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.INVALID_ARGS, "code");
        }

        @Override
        protected ArgumentClinicProvider getArgumentClinic() {
            return BuiltinConstructorsClinicProviders.CodeConstructorNodeClinicProviderGen.INSTANCE;
        }
    }

    @Builtin(name = "cell", minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 2, constructsClass = PythonBuiltinClassType.PCell, isPublic = false)
    @GenerateNodeFactory
    public abstract static class CellTypeNode extends PythonBinaryBuiltinNode {
        @CompilationFinal private Assumption sharedAssumption;

        private Assumption getAssumption() {
            if (sharedAssumption == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                sharedAssumption = Truffle.getRuntime().createAssumption("cell is effectively final");
            }
            if (CompilerDirectives.inCompiledCode()) {
                return sharedAssumption;
            } else {
                return Truffle.getRuntime().createAssumption("cell is effectively final");
            }
        }

        @Specialization
        Object newCell(@SuppressWarnings("unused") Object cls, Object contents,
                        @Bind("this") Node inliningTarget,
                        @Cached PythonObjectFactory factory,
                        @Cached InlinedConditionProfile nonEmptyProfile) {
            Assumption assumption = getAssumption();
            PCell cell = factory.createCell(assumption);
            if (nonEmptyProfile.profile(inliningTarget, !isNoValue(contents))) {
                cell.setRef(contents, assumption);
            }
            return cell;
        }
    }

    @Builtin(name = "BaseException", constructsClass = PythonBuiltinClassType.PBaseException, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, doc = "Common base class for all exceptions")
    @GenerateNodeFactory
    public abstract static class BaseExceptionNode extends PythonVarargsBuiltinNode {
        @Override
        public final Object varArgExecute(VirtualFrame frame, Object self, Object[] arguments, PKeyword[] keywords) throws VarargsBuiltinDirectInvocationNotSupported {
            if (arguments.length == 0) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                throw VarargsBuiltinDirectInvocationNotSupported.INSTANCE;
            }
            if (arguments.length == 1) {
                return execute(frame, arguments[0], PythonUtils.EMPTY_OBJECT_ARRAY, keywords);
            }
            Object[] argsWithoutSelf = PythonUtils.arrayCopyOfRange(arguments, 1, arguments.length);
            return execute(frame, arguments[0], argsWithoutSelf, keywords);
        }

        @Specialization(guards = "!needsNativeAllocationNode.execute(inliningTarget, cls)", limit = "1")
        static Object doManaged(Object cls, @SuppressWarnings("unused") Object[] args, @SuppressWarnings("unused") PKeyword[] kwargs,
                        @Bind("this") Node inliningTarget,
                        @SuppressWarnings("unused") @Exclusive @Cached TypeNodes.NeedsNativeAllocationNode needsNativeAllocationNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Cached InlinedConditionProfile argsProfile) {
            PTuple argsTuple;
            if (argsProfile.profile(inliningTarget, args.length == 0)) {
                argsTuple = null;
            } else {
                argsTuple = factory.createTuple(args);
            }
            return factory.createBaseException(cls, null, argsTuple);
        }

        @Specialization(guards = "needsNativeAllocationNode.execute(inliningTarget, cls)", limit = "1")
        static Object doNativeSubtype(Object cls, Object[] args, @SuppressWarnings("unused") PKeyword[] kwargs,
                        @SuppressWarnings("unused") @Bind("this") Node inliningTarget,
                        @SuppressWarnings("unused") @Exclusive @Cached TypeNodes.NeedsNativeAllocationNode needsNativeAllocationNode,
                        @Shared @Cached PythonObjectFactory factory,
                        @Cached PCallCapiFunction callCapiFunction,
                        @Cached PythonToNativeNode toNativeNode,
                        @Cached NativeToPythonTransferNode toPythonNode,
                        @Cached ExternalFunctionNodes.DefaultCheckFunctionResultNode checkFunctionResultNode) {
            Object argsTuple = args.length > 0 ? factory.createTuple(args) : factory.createEmptyTuple();
            Object nativeResult = callCapiFunction.call(NativeCAPISymbol.FUN_EXCEPTION_SUBTYPE_NEW, toNativeNode.execute(cls), toNativeNode.execute(argsTuple));
            return toPythonNode.execute(checkFunctionResultNode.execute(PythonContext.get(inliningTarget), NativeCAPISymbol.FUN_EXCEPTION_SUBTYPE_NEW.getTsName(), nativeResult));
        }
    }

    @Builtin(name = "BaseExceptionGroup", constructsClass = PythonBuiltinClassType.PBaseExceptionGroup, minNumOfPositionalArgs = 3, doc = "A combination of multiple unrelated exceptions.")
    @GenerateNodeFactory
    public abstract static class BaseExceptionGroupNode extends PythonTernaryBuiltinNode {

        @Specialization
        static Object doManaged(VirtualFrame frame, Object cls, Object messageObj, Object exceptionsObj,
                        @Bind("this") Node inliningTarget,
                        @Cached CastToTruffleStringNode castToStringNode,
                        @Cached PythonObjectFactory factory,
                        @Cached PySequenceCheckNode sequenceCheckNode,
                        @Cached TupleNodes.ConstructTupleNode toTupleNode,
                        @Cached SequenceStorageNodes.ToArrayNode toArrayNode,
                        @Cached PyObjectGetAttr getAttr,
                        @Cached InlinedLoopConditionProfile loopConditionProfile,
                        @Cached GetClassNode getClassNode,
                        @Cached BuiltinClassProfiles.IsBuiltinClassProfile exceptionProfile,
                        @Cached BuiltinClassProfiles.IsBuiltinClassProfile baseExceptionProfile,
                        @Cached TypeNodes.IsSameTypeNode isSameTypeNode,
                        @Cached PRaiseNode.Lazy raiseNode) {
            TruffleString message;
            try {
                message = castToStringNode.execute(inliningTarget, messageObj);
            } catch (CannotCastException ex) {
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.ARG_D_MUST_BE_S_NOT_P, "BaseExceptionGroup", 1, "str", messageObj);
            }
            if (!sequenceCheckNode.execute(inliningTarget, exceptionsObj)) {
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.SECOND_ARGUMENT_EXCEPTIONS_MUST_BE_A_SEQUENCE);
            }
            PTuple exceptionsTuple = toTupleNode.execute(frame, exceptionsObj);
            Object[] exceptions = toArrayNode.execute(inliningTarget, exceptionsTuple.getSequenceStorage());
            if (exceptions.length == 0) {
                throw raiseNode.get(inliningTarget).raise(ValueError, ErrorMessages.SECOND_ARGUMENT_EXCEPTIONS_MUST_BE_A_NON_EMPTY_SEQUENCE);
            }
            PythonContext context = PythonContext.get(inliningTarget);
            Object exceptionGroupType = getAttr.execute(inliningTarget, context.getBuiltins(), T_EXCEPTION_GROUP);
            boolean nestedBaseExceptions = false;
            loopConditionProfile.profileCounted(inliningTarget, exceptions.length);
            for (int i = 0; loopConditionProfile.inject(inliningTarget, i < exceptions.length); i++) {
                Object exceptionType = getClassNode.execute(inliningTarget, exceptions[i]);
                if (exceptionProfile.profileClass(inliningTarget, exceptionType, PythonBuiltinClassType.Exception)) {
                    continue;
                }
                if (baseExceptionProfile.profileClass(inliningTarget, exceptionType, PythonBuiltinClassType.PBaseException)) {
                    nestedBaseExceptions = true;
                } else {
                    throw raiseNode.get(inliningTarget).raise(ValueError, ErrorMessages.ITEM_D_OF_SECOND_ARGUMENT_EXCEPTIONS_IS_NOT_AN_EXCEPTION, i);
                }
            }
            if (isSameTypeNode.execute(inliningTarget, cls, PythonBuiltinClassType.PBaseExceptionGroup)) {
                if (!nestedBaseExceptions) {
                    /*
                     * All nested exceptions are Exception subclasses, wrap them in an
                     * ExceptionGroup
                     */
                    cls = exceptionGroupType;
                }
            } else if (isSameTypeNode.execute(inliningTarget, cls, exceptionGroupType)) {
                if (nestedBaseExceptions) {
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.CANNOT_NEST_BASE_EXCEPTIONS_IN_AN_EXCEPTION_GROUP);
                }
            } else {
                /* user-defined subclass */
                if (nestedBaseExceptions && exceptionProfile.profileClass(inliningTarget, cls, PythonBuiltinClassType.Exception)) {
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.CANNOT_NEST_BASE_EXCEPTIONS_IN_N, cls);
                }
            }
            return factory.createBaseExceptionGroup(cls, message, exceptions, new Object[]{messageObj, exceptionsObj});
        }
    }

    @Builtin(name = "mappingproxy", constructsClass = PythonBuiltinClassType.PMappingproxy, isPublic = false, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 2)
    @GenerateNodeFactory
    public abstract static class MappingproxyNode extends PythonBinaryBuiltinNode {
        @Specialization(guards = "!isNoValue(obj)")
        static Object doMapping(Object klass, Object obj,
                        @Bind("this") Node inliningTarget,
                        @Cached PyMappingCheckNode mappingCheckNode,
                        @Cached PythonObjectFactory factory,
                        @Cached PRaiseNode.Lazy raiseNode) {
            // descrobject.c mappingproxy_check_mapping()
            if (!(obj instanceof PList || obj instanceof PTuple) && mappingCheckNode.execute(inliningTarget, obj)) {
                return factory.createMappingproxy(klass, obj);
            }
            throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.S_ARG_MUST_BE_S_NOT_P, "mappingproxy()", "mapping", obj);
        }

        @Specialization(guards = "isNoValue(none)")
        @SuppressWarnings("unused")
        static Object doMissing(Object klass, PNone none,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.MISSING_D_REQUIRED_S_ARGUMENT_S_POS, "mappingproxy()", "mapping", 1);
        }
    }

    abstract static class DescriptorNode extends PythonBuiltinNode {
        @TruffleBoundary
        protected final void denyInstantiationAfterInitialization(TruffleString name) {
            if (getContext().isCoreInitialized()) {
                throw PRaiseNode.raiseUncached(this, TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, name);
            }
        }

        protected static Object ensure(Object value) {
            return value == PNone.NO_VALUE ? null : value;
        }
    }

    @Builtin(name = J_GETSET_DESCRIPTOR, constructsClass = PythonBuiltinClassType.GetSetDescriptor, isPublic = false, minNumOfPositionalArgs = 1, //
                    parameterNames = {"cls", "fget", "fset", "name", "owner"})
    @GenerateNodeFactory
    public abstract static class GetSetDescriptorNode extends DescriptorNode {
        @Specialization(guards = "isPythonClass(owner)")
        @TruffleBoundary
        Object call(@SuppressWarnings("unused") Object clazz, Object get, Object set, TruffleString name, Object owner) {
            denyInstantiationAfterInitialization(T_GETSET_DESCRIPTOR);
            return PythonObjectFactory.getUncached().createGetSetDescriptor(ensure(get), ensure(set), name, owner);
        }
    }

    @Builtin(name = J_MEMBER_DESCRIPTOR, constructsClass = PythonBuiltinClassType.MemberDescriptor, isPublic = false, minNumOfPositionalArgs = 1, //
                    parameterNames = {"cls", "fget", "fset", "name", "owner"})
    @GenerateNodeFactory
    public abstract static class MemberDescriptorNode extends DescriptorNode {
        @Specialization(guards = "isPythonClass(owner)")
        @TruffleBoundary
        Object doGeneric(@SuppressWarnings("unused") Object clazz, Object get, Object set, TruffleString name, Object owner) {
            denyInstantiationAfterInitialization(T_MEMBER_DESCRIPTOR);
            return PythonObjectFactory.getUncached().createGetSetDescriptor(ensure(get), ensure(set), name, owner);
        }
    }

    @Builtin(name = J_WRAPPER_DESCRIPTOR, constructsClass = PythonBuiltinClassType.WrapperDescriptor, isPublic = false, minNumOfPositionalArgs = 1, //
                    parameterNames = {"cls", "fget", "fset", "name", "owner"})
    @GenerateNodeFactory
    public abstract static class WrapperDescriptorNode extends DescriptorNode {
        @Specialization(guards = "isPythonClass(owner)")
        @TruffleBoundary
        Object doGeneric(@SuppressWarnings("unused") Object clazz, Object get, Object set, TruffleString name, Object owner) {
            denyInstantiationAfterInitialization(T_WRAPPER_DESCRIPTOR);
            return PythonObjectFactory.getUncached().createGetSetDescriptor(ensure(get), ensure(set), name, owner);
        }
    }

    // slice(stop)
    // slice(start, stop[, step])
    @Builtin(name = "slice", minNumOfPositionalArgs = 2, maxNumOfPositionalArgs = 4, constructsClass = PythonBuiltinClassType.PSlice, doc = """
                    slice(stop)
                    slice(start, stop[, step])

                    Create a slice object.  This is used for extended slicing (e.g. a[0:10:2]).""")
    @GenerateNodeFactory
    abstract static class SliceNode extends PythonQuaternaryBuiltinNode {
        @Specialization(guards = {"isNoValue(second)"})
        @SuppressWarnings("unused")
        static Object singleArg(Object cls, Object first, Object second, Object third,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached PySliceNew sliceNode) {
            return sliceNode.execute(inliningTarget, PNone.NONE, first, PNone.NONE);
        }

        @Specialization(guards = {"!isNoValue(stop)", "isNoValue(step)"})
        @SuppressWarnings("unused")
        static Object twoArgs(Object cls, Object start, Object stop, Object step,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached PySliceNew sliceNode) {
            return sliceNode.execute(inliningTarget, start, stop, PNone.NONE);
        }

        @Fallback
        static Object threeArgs(@SuppressWarnings("unused") Object cls, Object start, Object stop, Object step,
                        @Bind("this") Node inliningTarget,
                        @Shared @Cached PySliceNew sliceNode) {
            return sliceNode.execute(inliningTarget, start, stop, step);
        }
    }

    // memoryview(obj)
    @Builtin(name = J_MEMORYVIEW, minNumOfPositionalArgs = 2, parameterNames = {"$cls", "object"}, constructsClass = PythonBuiltinClassType.PMemoryView, //
                    doc = "Create a new memoryview object which references the given object.")
    @GenerateNodeFactory
    public abstract static class MemoryViewNode extends PythonBuiltinNode {

        public abstract PMemoryView execute(VirtualFrame frame, Object cls, Object object);

        public final PMemoryView execute(VirtualFrame frame, Object object) {
            return execute(frame, PythonBuiltinClassType.PMemoryView, object);
        }

        @Specialization
        PMemoryView fromObject(VirtualFrame frame, @SuppressWarnings("unused") Object cls, Object object,
                        @Cached PyMemoryViewFromObject memoryViewFromObject) {
            return memoryViewFromObject.execute(frame, object);
        }

        @NeverDefault
        public static MemoryViewNode create() {
            return BuiltinConstructorsFactory.MemoryViewNodeFactory.create(null);
        }
    }

    // super()
    @Builtin(name = J_SUPER, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 3, constructsClass = PythonBuiltinClassType.Super, doc = """
                    super() -> same as super(__class__, <first argument>)
                    super(type) -> unbound super object
                    super(type, obj) -> bound super object; requires isinstance(obj, type)
                    super(type, type2) -> bound super object; requires issubclass(type2, type)
                    Typical use to call a cooperative superclass method:
                    class C(B):
                        def meth(self, arg):
                            super().meth(arg)
                    This works for class methods too:
                    class C(B):
                        @classmethod
                        def cmeth(cls, arg):
                            super().cmeth(arg)""")
    @GenerateNodeFactory
    public abstract static class SuperInitNode extends PythonTernaryBuiltinNode {
        @Specialization
        static Object doObjectIndirect(Object self, @SuppressWarnings("unused") Object type, @SuppressWarnings("unused") Object object,
                        @Cached PythonObjectFactory factory) {
            return factory.createSuperObject(self);
        }
    }

    @Builtin(name = J_CLASSMETHOD, minNumOfPositionalArgs = 2, constructsClass = PythonBuiltinClassType.PClassmethod, doc = """
                    classmethod(function) -> method

                    Convert a function to be a class method.

                    A class method receives the class as implicit first argument,
                    just like an instance method receives the instance.
                    To declare a class method, use this idiom:

                      class C:
                          @classmethod
                          def f(cls, arg1, arg2, argN):
                              ...

                    It can be called either on the class (e.g. C.f()) or on an instance
                    (e.g. C().f()).  The instance is ignored except for its class.
                    If a class method is called for a derived class, the derived class
                    object is passed as the implied first argument.

                    Class methods are different than C++ or Java static methods.
                    If you want those, see the staticmethod builtin.""")
    @GenerateNodeFactory
    public abstract static class ClassmethodNode extends PythonBinaryBuiltinNode {
        @Specialization
        static Object doObjectIndirect(Object self, @SuppressWarnings("unused") Object callable,
                        @Cached PythonObjectFactory factory) {
            return factory.createClassmethod(self);
        }
    }

    @Builtin(name = J_INSTANCEMETHOD, minNumOfPositionalArgs = 2, constructsClass = PythonBuiltinClassType.PInstancemethod, isPublic = false, doc = "instancemethod(function)\n\nBind a function to a class.")
    @GenerateNodeFactory
    public abstract static class InstancemethodNode extends PythonBinaryBuiltinNode {
        @Specialization
        static Object doObjectIndirect(Object self, @SuppressWarnings("unused") Object callable,
                        @Cached PythonObjectFactory factory) {
            return factory.createInstancemethod(self);
        }
    }

    @Builtin(name = J_STATICMETHOD, minNumOfPositionalArgs = 2, constructsClass = PythonBuiltinClassType.PStaticmethod, doc = """
                    staticmethod(function) -> method

                    Convert a function to be a static method.

                    A static method does not receive an implicit first argument.
                    To declare a static method, use this idiom:

                         class C:
                             @staticmethod
                             def f(arg1, arg2, argN):
                                 ...

                    It can be called either on the class (e.g. C.f()) or on an instance
                    (e.g. C().f()). Both the class and the instance are ignored, and
                    neither is passed implicitly as the first argument to the method.

                    Static methods in Python are similar to those found in Java or C++.
                    For a more advanced concept, see the classmethod builtin.""")
    @GenerateNodeFactory
    public abstract static class StaticmethodNode extends PythonBinaryBuiltinNode {
        @Specialization
        static Object doObjectIndirect(Object self, @SuppressWarnings("unused") Object callable,
                        @Cached PythonObjectFactory factory) {
            return factory.createStaticmethod(self);
        }
    }

    @Builtin(name = J_MAP, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PMap, doc = """
                    map(func, *iterables) --> map object

                    Make an iterator that computes the function using arguments from
                    each of the iterables.  Stops when the shortest iterable is exhausted.""")
    @GenerateNodeFactory
    public abstract static class MapNode extends PythonVarargsBuiltinNode {
        @Specialization
        static PMap doit(VirtualFrame frame, Object cls, Object[] args, PKeyword[] keywords,
                        @Bind("this") Node inliningTarget,
                        @Cached(inline = false /* uncommon path */) TypeNodes.HasObjectInitNode hasObjectInitNode,
                        @Cached InlinedLoopConditionProfile loopProfile,
                        @Cached PyObjectGetIter getIter,
                        @Cached PythonObjectFactory factory,
                        @Cached PRaiseNode.Lazy raiseNode) {
            if (keywords.length > 0 && hasObjectInitNode.executeCached(cls)) {
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.S_TAKES_NO_KEYWORD_ARGS, "map()");
            }
            if (args.length < 2) {
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.MAP_MUST_HAVE_AT_LEAST_TWO_ARGUMENTS);
            }
            PMap map = factory.createMap(cls);
            map.setFunction(args[0]);
            Object[] iterators = new Object[args.length - 1];
            loopProfile.profileCounted(inliningTarget, iterators.length);
            for (int i = 0; loopProfile.inject(inliningTarget, i < iterators.length); i++) {
                iterators[i] = getIter.execute(frame, inliningTarget, args[i + 1]);
            }
            map.setIterators(iterators);
            return map;
        }
    }

    @Builtin(name = J_PROPERTY, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, constructsClass = PythonBuiltinClassType.PProperty, doc = """
                    Property attribute.

                      fget
                        function to be used for getting an attribute value
                      fset
                        function to be used for setting an attribute value
                      fdel
                        function to be used for del'ing an attribute
                      doc
                        docstring

                    Typical use is to define a managed attribute x:

                    class C(object):
                        def getx(self): return self._x
                        def setx(self, value): self._x = value
                        def delx(self): del self._x
                        x = property(getx, setx, delx, "I'm the 'x' property.")

                    Decorators make defining new properties or modifying existing ones easy:

                    class C(object):
                        @property
                        def x(self):
                            "I am the 'x' property."
                            return self._x
                        @x.setter
                        def x(self, value):
                            self._x = value
                        @x.deleter
                        def x(self):
                            del self._x""")
    @GenerateNodeFactory
    public abstract static class PropertyNode extends PythonVarargsBuiltinNode {
        @Specialization
        static PProperty doit(Object self, @SuppressWarnings("unused") Object[] args, @SuppressWarnings("unused") PKeyword[] keywords,
                        @Cached PythonObjectFactory factory) {
            return factory.createProperty(self);
        }
    }

    @Builtin(name = "SimpleNamespace", minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true, isPublic = false, constructsClass = PythonBuiltinClassType.PSimpleNamespace, doc = "A simple attribute-based namespace.\n" +
                    "\n" +
                    "SimpleNamespace(**kwargs)")
    @GenerateNodeFactory
    public abstract static class SimpleNamespaceNode extends PythonVarargsBuiltinNode {
        @Specialization
        static PSimpleNamespace doit(Object self, @SuppressWarnings("unused") Object[] args, @SuppressWarnings("unused") PKeyword[] keywords,
                        @Cached PythonObjectFactory factory) {
            return factory.createSimpleNamespace(self);
        }
    }

    @Builtin(name = "GenericAlias", minNumOfPositionalArgs = 3, constructsClass = PythonBuiltinClassType.PGenericAlias)
    @GenerateNodeFactory
    abstract static class GenericAliasNode extends PythonTernaryBuiltinNode {
        @Specialization
        static PGenericAlias doit(Object cls, Object origin, Object arguments,
                        @Cached PythonObjectFactory factory) {
            return factory.createGenericAlias(cls, origin, arguments, false);
        }
    }
}
