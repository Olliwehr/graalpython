/*
 * Copyright (c) 2021, 2024, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.python.builtins.objects.tuple;

import static com.oracle.graal.python.nodes.SpecialAttributeNames.T___DOC__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.J___NEW__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.J___REDUCE__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.J___REPR__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.T___NEW__;
import static com.oracle.graal.python.nodes.StringLiterals.T_COMMA_SPACE;
import static com.oracle.graal.python.nodes.StringLiterals.T_EQ;
import static com.oracle.graal.python.nodes.StringLiterals.T_LPAREN;
import static com.oracle.graal.python.nodes.StringLiterals.T_RPAREN;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.TypeError;
import static com.oracle.graal.python.util.PythonUtils.EMPTY_TRUFFLESTRING_ARRAY;
import static com.oracle.graal.python.util.PythonUtils.TS_ENCODING;
import static com.oracle.graal.python.util.PythonUtils.toTruffleStringArrayUncached;
import static com.oracle.graal.python.util.PythonUtils.toTruffleStringUncached;
import static com.oracle.graal.python.util.PythonUtils.tsArray;
import static com.oracle.graal.python.util.PythonUtils.tsLiteral;

import java.util.Arrays;
import java.util.Objects;

import org.graalvm.nativeimage.ImageInfo;

import com.oracle.graal.python.PythonLanguage;
import com.oracle.graal.python.builtins.Builtin;
import com.oracle.graal.python.builtins.Python3Core;
import com.oracle.graal.python.builtins.PythonBuiltinClassType;
import com.oracle.graal.python.builtins.objects.PNone;
import com.oracle.graal.python.builtins.objects.common.EconomicMapStorage;
import com.oracle.graal.python.builtins.objects.common.HashingStorage;
import com.oracle.graal.python.builtins.objects.common.HashingStorageNodes.HashingStorageGetItem;
import com.oracle.graal.python.builtins.objects.common.HashingStorageNodes.HashingStorageSetItem;
import com.oracle.graal.python.builtins.objects.common.SequenceStorageNodes;
import com.oracle.graal.python.builtins.objects.common.SequenceStorageNodes.GetItemNode;
import com.oracle.graal.python.builtins.objects.common.SequenceStorageNodes.ToArrayNode;
import com.oracle.graal.python.builtins.objects.dict.PDict;
import com.oracle.graal.python.builtins.objects.function.PArguments;
import com.oracle.graal.python.builtins.objects.function.PBuiltinFunction;
import com.oracle.graal.python.builtins.objects.function.PKeyword;
import com.oracle.graal.python.builtins.objects.function.Signature;
import com.oracle.graal.python.builtins.objects.getsetdescriptor.GetSetDescriptor;
import com.oracle.graal.python.builtins.objects.object.ObjectNodes.GetFullyQualifiedClassNameNode;
import com.oracle.graal.python.builtins.objects.tuple.StructSequenceFactory.DisabledNewNodeGen;
import com.oracle.graal.python.builtins.objects.tuple.StructSequenceFactory.NewNodeGen;
import com.oracle.graal.python.builtins.objects.tuple.StructSequenceFactory.ReduceNodeGen;
import com.oracle.graal.python.builtins.objects.tuple.StructSequenceFactory.ReprNodeGen;
import com.oracle.graal.python.builtins.objects.type.PythonBuiltinClass;
import com.oracle.graal.python.builtins.objects.type.TypeFlags;
import com.oracle.graal.python.builtins.objects.type.TypeNodes;
import com.oracle.graal.python.builtins.objects.type.TypeNodes.GetNameNode;
import com.oracle.graal.python.lib.PyObjectReprAsTruffleStringNode;
import com.oracle.graal.python.nodes.ErrorMessages;
import com.oracle.graal.python.nodes.PRaiseNode;
import com.oracle.graal.python.nodes.PRootNode;
import com.oracle.graal.python.nodes.attributes.ReadAttributeFromObjectNode;
import com.oracle.graal.python.nodes.attributes.WriteAttributeToObjectNode;
import com.oracle.graal.python.nodes.builtins.ListNodes.FastConstructListNode;
import com.oracle.graal.python.nodes.classes.IsSubtypeNode;
import com.oracle.graal.python.nodes.function.BuiltinFunctionRootNode;
import com.oracle.graal.python.nodes.function.PythonBuiltinBaseNode;
import com.oracle.graal.python.nodes.function.builtins.PythonTernaryBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonUnaryBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonVarargsBuiltinNode;
import com.oracle.graal.python.nodes.object.BuiltinClassProfiles.IsBuiltinObjectProfile;
import com.oracle.graal.python.nodes.object.GetClassNode;
import com.oracle.graal.python.runtime.PythonContext;
import com.oracle.graal.python.runtime.exception.PException;
import com.oracle.graal.python.runtime.object.PythonObjectFactory;
import com.oracle.graal.python.runtime.object.PythonObjectSlowPathFactory;
import com.oracle.graal.python.runtime.sequence.PSequence;
import com.oracle.graal.python.runtime.sequence.storage.ObjectSequenceStorage;
import com.oracle.graal.python.runtime.sequence.storage.SequenceStorage;
import com.oracle.graal.python.util.Function;
import com.oracle.graal.python.util.PythonUtils;
import com.oracle.graal.python.util.PythonUtils.PrototypeNodeFactory;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Cached.Exclusive;
import com.oracle.truffle.api.dsl.Cached.Shared;
import com.oracle.truffle.api.dsl.NeverDefault;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.profiles.InlinedBranchProfile;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;

/**
 * Allows definitions of tuple-like structs with named fields (like structseq.c in CPython).
 *
 * @see com.oracle.graal.python.builtins.modules.PosixModuleBuiltins for an example
 */
public class StructSequence {

    /** The <it>visible</it> length (excludes unnamed fields) of the structseq type. */
    public static final TruffleString T_N_SEQUENCE_FIELDS = tsLiteral("n_sequence_fields");

    /** The <it>real</it> length (includes unnamed fields) of the structseq type. */
    public static final TruffleString T_N_FIELDS = tsLiteral("n_fields");

    /** The number of unnamed fields. */
    public static final TruffleString T_N_UNNAMED_FIELDS = tsLiteral("n_unnamed_fields");

    /**
     * The equivalent of {@code PyStructSequence_Desc} except of the {@code name}. We don't need the
     * type's name in the descriptor and this will improve code sharing.
     */
    public static sealed class Descriptor permits BuiltinTypeDescriptor {
        public final TruffleString docString;
        public final int inSequence;
        public final TruffleString[] fieldNames;
        public final TruffleString[] fieldDocStrings;
        public final boolean allowInstances;

        public Descriptor(TruffleString docString, int inSequence, TruffleString[] fieldNames, TruffleString[] fieldDocStrings, boolean allowInstances) {
            assert fieldDocStrings == null || fieldNames.length == fieldDocStrings.length;
            this.docString = docString;
            this.inSequence = inSequence;
            this.fieldNames = fieldNames;
            this.fieldDocStrings = fieldDocStrings;
            this.allowInstances = allowInstances;
        }

        public Descriptor(TruffleString docString, int inSequence, TruffleString[] fieldNames, TruffleString[] fieldDocStrings) {
            this(docString, inSequence, fieldNames, fieldDocStrings, true);
        }

        // This shifts the names in a confusing way, but that is what CPython does:
        // >>> s = os.stat('.')
        // >>> s.st_atime
        // 1607033732.3041613
        // >>> s
        // os.stat_result(..., st_atime=1607033732, ...)
        //
        // note that st_atime accessed by name returns float (index 10), but in repr the same label
        // is assigned to the int value at index 7
        TruffleString[] getFieldsForRepr() {
            TruffleString[] fieldNamesForRepr = new TruffleString[inSequence];
            int k = 0;
            for (int idx = 0; idx < fieldNames.length && k < inSequence; ++idx) {
                if (fieldNames[idx] != null) {
                    fieldNamesForRepr[k++] = fieldNames[idx];
                }
            }
            // each field < inSequence must have a name
            assert k == inSequence;
            return fieldNamesForRepr;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Descriptor)) {
                return false;
            }
            Descriptor that = (Descriptor) o;
            return inSequence == that.inSequence && allowInstances == that.allowInstances && Objects.equals(docString, that.docString) && Arrays.equals(fieldNames, that.fieldNames) &&
                            Arrays.equals(fieldDocStrings, that.fieldDocStrings);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(docString, inSequence, allowInstances);
            result = 31 * result + Arrays.hashCode(fieldNames);
            result = 31 * result + Arrays.hashCode(fieldDocStrings);
            return result;
        }
    }

    /**
     * Very similar to {@code PyStructSequence_Desc} but already specific to a built-in type. Used
     * for built-in structseq objects.
     */
    public static final class BuiltinTypeDescriptor extends Descriptor {
        public final PythonBuiltinClassType type;
        private final boolean initializedInBuildTime = ImageInfo.inImageBuildtimeCode();

        public BuiltinTypeDescriptor(PythonBuiltinClassType type, String docString, int inSequence, String[] fieldNames, String[] fieldDocStrings, boolean allowInstances) {
            super(docString == null ? null : toTruffleStringUncached(docString), inSequence, toTruffleStringArrayUncached(fieldNames), toTruffleStringArrayUncached(fieldDocStrings), allowInstances);
            assert type.getBase() == PythonBuiltinClassType.PTuple;
            assert !type.isAcceptableBase();
            this.type = type;
        }

        public BuiltinTypeDescriptor(PythonBuiltinClassType type, String docString, int inSequence, String[] fieldNames, String[] fieldDocStrings) {
            this(type, docString, inSequence, fieldNames, fieldDocStrings, true);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof BuiltinTypeDescriptor that)) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            return type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), type);
        }

        public boolean wasInitializedAtBuildTime() {
            return initializedInBuildTime;
        }
    }

    public record DescriptorCallTargets(RootCallTarget reprBuiltin, RootCallTarget reduceBuiltin, RootCallTarget newBuiltin) {
    }

    @TruffleBoundary
    public static void initType(Python3Core core, BuiltinTypeDescriptor desc) {
        initType(core.factory(), core.getLanguage(), core.lookupType(desc.type), desc);
    }

    @TruffleBoundary
    public static void initType(PythonLanguage language, Object klass, Descriptor desc) {
        initType(PythonContext.get(null).factory(), language, klass, desc);
    }

    @TruffleBoundary
    public static void initType(PythonObjectSlowPathFactory factory, PythonLanguage language, Object klass, Descriptor desc) {
        assert IsSubtypeNode.getUncached().execute(klass, PythonBuiltinClassType.PTuple);

        long flags = TypeNodes.GetTypeFlagsNode.executeUncached(klass);
        if ((flags & TypeFlags.IMMUTABLETYPE) != 0) {
            // Temporarily open the type for mutation
            TypeNodes.SetTypeFlagsNode.executeUncached(klass, flags & ~TypeFlags.IMMUTABLETYPE);
        }

        // create descriptors for accessing named fields by their names
        int unnamedFields = 0;
        for (int idx = 0; idx < desc.fieldNames.length; ++idx) {
            if (desc.fieldNames[idx] != null) {
                TruffleString doc = desc.fieldDocStrings == null ? null : desc.fieldDocStrings[idx];
                createMember(factory, language, klass, desc.fieldNames[idx], doc, idx);
            } else {
                unnamedFields++;
            }
        }

        DescriptorCallTargets callTargets = language.getOrCreateStructSequenceCallTargets(desc, d -> new DescriptorCallTargets(
                        createBuiltinCallTarget(language, desc, ReprNode.class, ReprNodeGen::create, true),
                        createBuiltinCallTarget(language, desc, ReduceNode.class, ReduceNodeGen::create, true),
                        desc.allowInstances ? //
                                        createBuiltinCallTarget(language, desc, NewNode.class, NewNodeGen::create, false) : //
                                        createBuiltinCallTarget(language, desc, DisabledNewNode.class, ignore -> DisabledNewNodeGen.create(), false)));

        createMethod(factory, klass, ReprNode.class, callTargets.reprBuiltin);
        createMethod(factory, klass, ReduceNode.class, callTargets.reduceBuiltin);

        WriteAttributeToObjectNode writeAttrNode = WriteAttributeToObjectNode.getUncached(true);
        /*
         * Only set __doc__ if given. It may be 'null' e.g. in case of initializing a native class
         * where 'tp_doc' is set in native code already.
         */
        if (desc.docString != null) {
            writeAttrNode.execute(klass, T___DOC__, desc.docString);
        }
        writeAttrNode.execute(klass, T_N_SEQUENCE_FIELDS, desc.inSequence);
        writeAttrNode.execute(klass, T_N_FIELDS, desc.fieldNames.length);
        writeAttrNode.execute(klass, T_N_UNNAMED_FIELDS, unnamedFields);

        if (ReadAttributeFromObjectNode.getUncachedForceType().execute(klass, T___NEW__) == PNone.NO_VALUE) {
            Builtin builtin = NewNode.class.getAnnotation(Builtin.class);
            PythonUtils.createConstructor(factory, klass, builtin, callTargets.newBuiltin);
        }
        if ((flags & TypeFlags.IMMUTABLETYPE) != 0) {
            // Restore flags
            TypeNodes.SetTypeFlagsNode.executeUncached(klass, flags);
        }
    }

    private static <T extends PythonBuiltinBaseNode> RootCallTarget createBuiltinCallTarget(PythonLanguage l, Descriptor descriptor, Class<T> nodeClass, Function<Descriptor, T> nodeFactory,
                    boolean declaresExplicitSelf) {
        Builtin builtin = nodeClass.getAnnotation(Builtin.class);
        return new BuiltinFunctionRootNode(l, builtin, new PrototypeNodeFactory<T>(nodeFactory.apply(descriptor)), declaresExplicitSelf).getCallTarget();
    }

    private static void createMember(PythonObjectSlowPathFactory factory, PythonLanguage language, Object klass, TruffleString name, TruffleString doc, int idx) {
        RootCallTarget callTarget = language.createStructSeqIndexedMemberAccessCachedCallTarget((l) -> new GetStructMemberNode(l, idx), idx);
        PBuiltinFunction getter = factory.createBuiltinFunction(name, klass, 0, 0, callTarget);
        GetSetDescriptor callable = factory.createGetSetDescriptor(getter, null, name, klass, false);
        if (doc != null) {
            callable.setAttribute(T___DOC__, doc);
        }
        WriteAttributeToObjectNode.getUncached(true).execute(klass, name, callable);
    }

    private static void createMethod(PythonObjectSlowPathFactory factory, Object klass, Class<? extends PythonBuiltinBaseNode> nodeClass, RootCallTarget callTarget) {
        Builtin builtin = nodeClass.getAnnotation(Builtin.class);
        PythonUtils.createMethod(factory, klass, builtin, callTarget, PythonBuiltinClassType.PTuple, 0);
    }

    @Builtin(name = J___NEW__, minNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true)
    public abstract static class DisabledNewNode extends PythonVarargsBuiltinNode {

        @Override
        @SuppressWarnings("unused")
        public final Object varArgExecute(VirtualFrame frame, Object self, Object[] arguments, PKeyword[] keywords) throws VarargsBuiltinDirectInvocationNotSupported {
            if (arguments.length > 0) {
                return error(arguments[0], PythonUtils.EMPTY_OBJECT_ARRAY, PKeyword.EMPTY_KEYWORDS);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw VarargsBuiltinDirectInvocationNotSupported.INSTANCE;
        }

        @Specialization
        @SuppressWarnings("unused")
        Object error(Object cls, Object[] arguments, PKeyword[] keywords) {
            throw raise(TypeError, ErrorMessages.CANNOT_CREATE_INSTANCES, StructSequence.getTpName(cls));
        }
    }

    @Builtin(name = J___NEW__, minNumOfPositionalArgs = 2, parameterNames = {"$cls", "sequence", "dict"})
    public abstract static class NewNode extends PythonTernaryBuiltinNode {

        @CompilationFinal(dimensions = 1) private final TruffleString[] fieldNames;
        private final int inSequence;

        NewNode(Descriptor desc) {
            this.fieldNames = desc.fieldNames;
            this.inSequence = desc.inSequence;
        }

        @NeverDefault
        public static NewNode create(Descriptor desc) {
            return NewNodeGen.create(desc);
        }

        @Specialization(guards = "isNoValue(dict)")
        @SuppressWarnings("truffle-static-method")
        PTuple withoutDict(VirtualFrame frame, Object cls, Object sequence, @SuppressWarnings("unused") PNone dict,
                        @Bind("this") Node inliningTarget,
                        @Exclusive @Cached FastConstructListNode fastConstructListNode,
                        @Exclusive @Cached ToArrayNode toArrayNode,
                        @Exclusive @Cached IsBuiltinObjectProfile notASequenceProfile,
                        @Exclusive @Cached InlinedBranchProfile wrongLenProfile,
                        @Exclusive @Cached InlinedBranchProfile needsReallocProfile,
                        @Shared @Cached PythonObjectFactory factory,
                        @Exclusive @Cached PRaiseNode.Lazy raiseNode) {
            Object[] src = sequenceToArray(frame, inliningTarget, sequence, fastConstructListNode, toArrayNode, notASequenceProfile, raiseNode);
            Object[] dst = processSequence(inliningTarget, cls, src, wrongLenProfile, needsReallocProfile, raiseNode);
            for (int i = src.length; i < dst.length; ++i) {
                dst[i] = PNone.NONE;
            }
            return factory.createTuple(cls, new ObjectSequenceStorage(dst, inSequence));
        }

        @Specialization
        @SuppressWarnings("truffle-static-method")
        PTuple withDict(VirtualFrame frame, Object cls, Object sequence, PDict dict,
                        @Bind("this") Node inliningTarget,
                        @Exclusive @Cached FastConstructListNode fastConstructListNode,
                        @Exclusive @Cached ToArrayNode toArrayNode,
                        @Exclusive @Cached IsBuiltinObjectProfile notASequenceProfile,
                        @Exclusive @Cached InlinedBranchProfile wrongLenProfile,
                        @Exclusive @Cached InlinedBranchProfile needsReallocProfile,
                        @Cached HashingStorageGetItem getItem,
                        @Shared @Cached PythonObjectFactory factory,
                        @Exclusive @Cached PRaiseNode.Lazy raiseNode) {
            Object[] src = sequenceToArray(frame, inliningTarget, sequence, fastConstructListNode, toArrayNode, notASequenceProfile, raiseNode);
            Object[] dst = processSequence(inliningTarget, cls, src, wrongLenProfile, needsReallocProfile, raiseNode);
            HashingStorage hs = dict.getDictStorage();
            for (int i = src.length; i < dst.length; ++i) {
                Object o = getItem.execute(inliningTarget, hs, fieldNames[i]);
                dst[i] = o == null ? PNone.NONE : o;
            }
            return factory.createTuple(cls, new ObjectSequenceStorage(dst, inSequence));
        }

        @Specialization(guards = {"!isNoValue(dict)", "!isDict(dict)"})
        @SuppressWarnings("unused")
        static PTuple doDictError(VirtualFrame frame, Object cls, Object sequence, Object dict,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(TypeError, ErrorMessages.TAKES_A_DICT_AS_SECOND_ARG_IF_ANY, StructSequence.getTpName(cls));
        }

        private static Object[] sequenceToArray(VirtualFrame frame, Node inliningTarget, Object sequence, FastConstructListNode fastConstructListNode,
                        ToArrayNode toArrayNode, IsBuiltinObjectProfile notASequenceProfile, PRaiseNode.Lazy raiseNode) {
            PSequence seq;
            try {
                seq = fastConstructListNode.execute(frame, inliningTarget, sequence);
            } catch (PException e) {
                e.expect(inliningTarget, TypeError, notASequenceProfile);
                throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.CONSTRUCTOR_REQUIRES_A_SEQUENCE);
            }
            return toArrayNode.execute(inliningTarget, seq.getSequenceStorage());
        }

        private Object[] processSequence(Node inliningTarget, Object cls, Object[] src, InlinedBranchProfile wrongLenProfile, InlinedBranchProfile needsReallocProfile, PRaiseNode.Lazy raiseNode) {
            int len = src.length;
            int minLen = inSequence;
            int maxLen = fieldNames.length;

            if (len < minLen || len > maxLen) {
                wrongLenProfile.enter(inliningTarget);
                if (minLen == maxLen) {
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.TAKES_A_D_SEQUENCE, StructSequence.getTpName(cls), minLen, len);
                }
                if (len < minLen) {
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.TAKES_AN_AT_LEAST_D_SEQUENCE, StructSequence.getTpName(cls), minLen, len);
                } else {    // len > maxLen
                    throw raiseNode.get(inliningTarget).raise(TypeError, ErrorMessages.TAKES_AN_AT_MOST_D_SEQUENCE, StructSequence.getTpName(cls), maxLen, len);
                }
            }

            if (len != maxLen) {
                needsReallocProfile.enter(inliningTarget);
                Object[] dst = new Object[maxLen];
                PythonUtils.arraycopy(src, 0, dst, 0, len);
                return dst;
            }
            return src;
        }
    }

    @Builtin(name = J___REDUCE__, minNumOfPositionalArgs = 1)
    abstract static class ReduceNode extends PythonUnaryBuiltinNode {

        @CompilationFinal(dimensions = 1) private final TruffleString[] fieldNames;
        private final int inSequence;

        ReduceNode(Descriptor desc) {
            this.fieldNames = desc.fieldNames;
            this.inSequence = desc.inSequence;
        }

        @Specialization
        PTuple reduce(PTuple self,
                        @Bind("this") Node inliningTarget,
                        @Cached HashingStorageSetItem setHashingStorageItem,
                        @Cached GetClassNode getClass,
                        @Cached PythonObjectFactory factory) {
            assert self.getSequenceStorage() instanceof ObjectSequenceStorage;
            Object[] data = CompilerDirectives.castExact(self.getSequenceStorage(), ObjectSequenceStorage.class).getInternalObjectArray();
            assert data.length == fieldNames.length;
            PTuple seq;
            PDict dict;
            if (fieldNames.length == inSequence) {
                seq = factory.createTuple(data);
                dict = factory.createDict();
            } else {
                HashingStorage storage = EconomicMapStorage.create(fieldNames.length - inSequence);
                for (int i = inSequence; i < fieldNames.length; ++i) {
                    storage = setHashingStorageItem.execute(inliningTarget, storage, fieldNames[i], data[i]);
                }
                seq = factory.createTuple(Arrays.copyOf(data, inSequence));
                dict = factory.createDict(storage);
            }
            PTuple seqDictPair = factory.createTuple(new Object[]{seq, dict});
            return factory.createTuple(new Object[]{getClass.execute(inliningTarget, self), seqDictPair});
        }
    }

    @Builtin(name = J___REPR__, minNumOfPositionalArgs = 1)
    abstract static class ReprNode extends PythonUnaryBuiltinNode {

        @CompilationFinal(dimensions = 1) private final TruffleString[] fieldNames;

        ReprNode(Descriptor desc) {
            this.fieldNames = desc.getFieldsForRepr();
        }

        @Specialization
        public TruffleString repr(VirtualFrame frame, PTuple self,
                        @Bind("this") Node inliningTarget,
                        @Cached GetFullyQualifiedClassNameNode getFullyQualifiedClassNameNode,
                        @Cached("createNotNormalized()") GetItemNode getItemNode,
                        @Cached PyObjectReprAsTruffleStringNode reprNode,
                        @Cached TruffleStringBuilder.AppendStringNode appendStringNode,
                        @Cached TruffleStringBuilder.ToStringNode toStringNode) {
            TruffleStringBuilder sb = TruffleStringBuilder.create(TS_ENCODING);
            appendStringNode.execute(sb, getFullyQualifiedClassNameNode.execute(frame, inliningTarget, self));
            appendStringNode.execute(sb, T_LPAREN);
            SequenceStorage tupleStore = self.getSequenceStorage();
            if (fieldNames.length > 0) {
                appendStringNode.execute(sb, fieldNames[0]);
                appendStringNode.execute(sb, T_EQ);
                appendStringNode.execute(sb, reprNode.execute(frame, inliningTarget, getItemNode.execute(tupleStore, 0)));
                for (int i = 1; i < fieldNames.length; i++) {
                    appendStringNode.execute(sb, T_COMMA_SPACE);
                    appendStringNode.execute(sb, fieldNames[i]);
                    appendStringNode.execute(sb, T_EQ);
                    appendStringNode.execute(sb, reprNode.execute(frame, inliningTarget, getItemNode.execute(tupleStore, i)));
                }
            }
            appendStringNode.execute(sb, T_RPAREN);
            return toStringNode.execute(sb);
        }
    }

    private static class GetStructMemberNode extends PRootNode {
        public static final Signature SIGNATURE = new Signature(-1, false, -1, false, tsArray("$self"), EMPTY_TRUFFLESTRING_ARRAY);
        private final int fieldIdx;

        GetStructMemberNode(PythonLanguage language, int fieldIdx) {
            super(language);
            this.fieldIdx = fieldIdx;
        }

        @Override
        public Object execute(VirtualFrame frame) {
            PTuple self = (PTuple) PArguments.getArgument(frame, 0);
            return SequenceStorageNodes.GetItemScalarNode.executeUncached(self.getSequenceStorage(), fieldIdx);
        }

        @Override
        public Signature getSignature() {
            return SIGNATURE;
        }

        @Override
        public boolean isInternal() {
            return true;
        }

        @Override
        public boolean isPythonInternal() {
            return true;
        }
    }

    static TruffleString getTpName(Object cls) {
        if (cls instanceof PythonBuiltinClassType) {
            return ((PythonBuiltinClassType) cls).getPrintName();
        } else if (cls instanceof PythonBuiltinClass) {
            return ((PythonBuiltinClass) cls).getType().getPrintName();
        }
        return GetNameNode.executeUncached(cls);
    }
}
