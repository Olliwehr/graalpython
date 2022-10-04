package com.oracle.graal.python.builtins.objects.types;

import static com.oracle.graal.python.nodes.SpecialAttributeNames.J___ARGS__;
import static com.oracle.graal.python.nodes.SpecialAttributeNames.T___MODULE__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.J___GETATTRIBUTE__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.J___HASH__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.J___OR__;
import static com.oracle.graal.python.nodes.SpecialMethodNames.J___REPR__;
import static com.oracle.graal.python.util.PythonUtils.TS_ENCODING;
import static com.oracle.graal.python.util.PythonUtils.tsLiteral;

import java.util.List;

import com.oracle.graal.python.builtins.Builtin;
import com.oracle.graal.python.builtins.CoreFunctions;
import com.oracle.graal.python.builtins.PythonBuiltinClassType;
import com.oracle.graal.python.builtins.PythonBuiltins;
import com.oracle.graal.python.builtins.objects.common.HashingCollectionNodes;
import com.oracle.graal.python.builtins.objects.object.ObjectBuiltins;
import com.oracle.graal.python.builtins.objects.set.PFrozenSet;
import com.oracle.graal.python.builtins.objects.type.TypeNodes;
import com.oracle.graal.python.lib.PyObjectGetAttr;
import com.oracle.graal.python.lib.PyObjectHashNode;
import com.oracle.graal.python.nodes.StringLiterals;
import com.oracle.graal.python.nodes.function.PythonBuiltinBaseNode;
import com.oracle.graal.python.nodes.function.builtins.PythonBinaryBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonUnaryBuiltinNode;
import com.oracle.graal.python.nodes.object.GetClassNode;
import com.oracle.graal.python.nodes.util.CannotCastException;
import com.oracle.graal.python.nodes.util.CastToTruffleStringNode;
import com.oracle.graal.python.runtime.sequence.storage.SequenceStorage;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.strings.TruffleString;
import com.oracle.truffle.api.strings.TruffleStringBuilder;

@CoreFunctions(extendClasses = PythonBuiltinClassType.PUnionType)
public class UnionTypeBuiltins extends PythonBuiltins {
    @Override
    protected List<? extends NodeFactory<? extends PythonBuiltinBaseNode>> getNodeFactories() {
        return UnionTypeBuiltinsFactory.getFactories();
    }

    @Builtin(name = J___ARGS__, minNumOfPositionalArgs = 1, isGetter = true)
    @GenerateNodeFactory
    static abstract class ArgsNode extends PythonUnaryBuiltinNode {
        @Specialization
        Object args(PUnionType self) {
            return self.getArgs();
        }
    }

    @Builtin(name = J___OR__, minNumOfPositionalArgs = 2)
    @GenerateNodeFactory
    abstract static class OrNode extends PythonBinaryBuiltinNode {
        @Specialization
        Object union(Object self, Object other,
                        @Cached GenericTypeNodes.UnionTypeOrNode orNode) {
            return orNode.execute(self, other);
        }
    }

    @Builtin(name = J___REPR__, minNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    static abstract class ReprNode extends PythonUnaryBuiltinNode {
        private static final TruffleString SEPARATOR = tsLiteral(" | ");

        @Specialization
        @TruffleBoundary
        Object repr(PUnionType self) {
            TruffleStringBuilder sb = TruffleStringBuilder.create(TS_ENCODING);
            SequenceStorage argsStorage = self.getArgs().getSequenceStorage();
            for (int i = 0; i < argsStorage.length(); i++) {
                if (i > 0) {
                    sb.appendStringUncached(SEPARATOR);
                }
                reprItem(sb, argsStorage.getItemNormalized(i));
            }
            return sb.toStringUncached();
        }

        // Equivalent of union_repr_item in CPython
        private static void reprItem(TruffleStringBuilder sb, Object obj) {
            TypeNodes.IsSameTypeNode sameType = TypeNodes.IsSameTypeNode.getUncached();
            if (sameType.execute(obj, PythonBuiltinClassType.PNone)) {
                sb.appendStringUncached(StringLiterals.T_NONE);
                return;
            }
            GenericTypeNodes.reprItem(sb, obj);
        }
    }

    @Builtin(name = J___HASH__, minNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    static abstract class HashNode extends PythonUnaryBuiltinNode {
        @Specialization
        long hash(VirtualFrame frame, PUnionType self,
                        @Cached PyObjectHashNode hashNode,
                        @Cached HashingCollectionNodes.GetClonedHashingStorageNode getHashingStorageNode) {
            PFrozenSet argSet = factory().createFrozenSet(getHashingStorageNode.doNoValue(frame, self.getArgs()));
            return hashNode.execute(frame, argSet);
        }
    }

    @Builtin(name = J___GETATTRIBUTE__, minNumOfPositionalArgs = 2)
    @GenerateNodeFactory
    abstract static class GetAttributeNode extends PythonBinaryBuiltinNode {

        @Specialization
        Object getattribute(VirtualFrame frame, PUnionType self, Object nameObj,
                        @Cached CastToTruffleStringNode cast,
                        @Cached TruffleString.EqualNode equalNode,
                        @Cached GetClassNode getClassNode,
                        @Cached PyObjectGetAttr getAttr,
                        @Cached ObjectBuiltins.GetAttributeNode genericGetAttribute) {
            TruffleString name;
            try {
                name = cast.execute(nameObj);
            } catch (CannotCastException e) {
                return genericGetAttribute.execute(frame, self, nameObj);
            }
            if (equalNode.execute(name, T___MODULE__, TS_ENCODING)) {
                return getAttr.execute(frame, getClassNode.execute(self), name);
            }
            return genericGetAttribute.execute(frame, self, nameObj);
        }
    }
}
