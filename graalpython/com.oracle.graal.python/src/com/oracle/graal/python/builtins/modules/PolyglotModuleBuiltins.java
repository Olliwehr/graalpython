/*
 * Copyright (c) 2017, 2023, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.python.builtins.modules;

import static com.oracle.graal.python.nodes.BuiltinNames.J_REGISTER_HOST_INTEROP_BEHAVIOR;
import static com.oracle.graal.python.nodes.BuiltinNames.J___GRAALPYTHON_HOST_INTEROP_BEHAVIOR__;
import static com.oracle.graal.python.nodes.ErrorMessages.ARG_MUST_BE_NUMBER;
import static com.oracle.graal.python.nodes.ErrorMessages.ARG_S_MUST_BE_S_NOT_P;
import static com.oracle.graal.python.nodes.ErrorMessages.S_ARG_MUST_BE_S_NOT_P;
import static com.oracle.graal.python.nodes.ErrorMessages.S_CANNOT_HAVE_S;
import static com.oracle.graal.python.nodes.ErrorMessages.S_TAKES_NO_KEYWORD_ARGS;
import static com.oracle.graal.python.nodes.HostInteropMethodNames.J_FITS_IN_BIG_INTEGER;
import static com.oracle.graal.python.nodes.HostInteropMethodNames.J_FITS_IN_BYTE;
import static com.oracle.graal.python.nodes.HostInteropMethodNames.J_FITS_IN_DOUBLE;
import static com.oracle.graal.python.nodes.HostInteropMethodNames.J_FITS_IN_FLOAT;
import static com.oracle.graal.python.nodes.HostInteropMethodNames.J_FITS_IN_INT;
import static com.oracle.graal.python.nodes.HostInteropMethodNames.J_FITS_IN_LONG;
import static com.oracle.graal.python.nodes.HostInteropMethodNames.J_FITS_IN_SHORT;
import static com.oracle.graal.python.nodes.StringLiterals.T_READABLE;
import static com.oracle.graal.python.nodes.StringLiterals.T_WRITABLE;
import static com.oracle.graal.python.nodes.truffle.TruffleStringMigrationHelpers.isJavaString;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.NotImplementedError;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.OSError;
import static com.oracle.graal.python.runtime.exception.PythonErrorType.ValueError;
import static com.oracle.graal.python.util.PythonUtils.TS_ENCODING;
import static com.oracle.graal.python.util.PythonUtils.tsLiteral;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.oracle.graal.python.PythonLanguage;
import com.oracle.graal.python.builtins.Builtin;
import com.oracle.graal.python.builtins.CoreFunctions;
import com.oracle.graal.python.builtins.Python3Core;
import com.oracle.graal.python.builtins.PythonBuiltinClassType;
import com.oracle.graal.python.builtins.PythonBuiltins;
import com.oracle.graal.python.builtins.objects.PNone;
import com.oracle.graal.python.builtins.objects.PythonAbstractObject;
import com.oracle.graal.python.builtins.objects.common.SequenceNodes;
import com.oracle.graal.python.builtins.objects.function.PBuiltinFunction;
import com.oracle.graal.python.builtins.objects.function.PFunction;
import com.oracle.graal.python.builtins.objects.function.PKeyword;
import com.oracle.graal.python.builtins.objects.ints.PInt;
import com.oracle.graal.python.builtins.objects.method.PBuiltinMethod;
import com.oracle.graal.python.builtins.objects.method.PMethod;
import com.oracle.graal.python.builtins.objects.module.PythonModule;
import com.oracle.graal.python.builtins.objects.polyglot.PHostInteropBehavior;
import com.oracle.graal.python.builtins.objects.type.TypeNodes;
import com.oracle.graal.python.nodes.ErrorMessages;
import com.oracle.graal.python.nodes.PRaiseNode;
import com.oracle.graal.python.nodes.SpecialAttributeNames;
import com.oracle.graal.python.nodes.attributes.GetAttributeNode;
import com.oracle.graal.python.nodes.function.PythonBuiltinBaseNode;
import com.oracle.graal.python.nodes.function.PythonBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonUnaryBuiltinNode;
import com.oracle.graal.python.nodes.function.builtins.PythonVarargsBuiltinNode;
import com.oracle.graal.python.nodes.interop.HostInteropBehaviorMethod;
import com.oracle.graal.python.nodes.truffle.PythonArithmeticTypes;
import com.oracle.graal.python.nodes.util.CannotCastException;
import com.oracle.graal.python.nodes.util.CastToJavaStringNode;
import com.oracle.graal.python.runtime.PythonContext;
import com.oracle.graal.python.runtime.exception.PythonErrorType;
import com.oracle.graal.python.runtime.object.PythonObjectFactory;
import com.oracle.graal.python.runtime.sequence.PSequence;
import com.oracle.graal.python.runtime.sequence.storage.SequenceStorage;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.TruffleFile;
import com.oracle.truffle.api.TruffleLanguage.Env;
import com.oracle.truffle.api.TruffleLogger;
import com.oracle.truffle.api.dsl.Bind;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Cached.Shared;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.InvalidArrayIndexException;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.LanguageInfo;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.object.HiddenKey;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.Source.LiteralBuilder;
import com.oracle.truffle.api.source.Source.SourceBuilder;
import com.oracle.truffle.api.strings.TruffleString;

@CoreFunctions(defineModule = "polyglot")
public final class PolyglotModuleBuiltins extends PythonBuiltins {

    private static final TruffleString T_READ_SIDE_EFFECTS = tsLiteral("read-side-effects");
    private static final TruffleString T_WRITE_SIDE_EFFECTS = tsLiteral("write-side-effects");
    private static final TruffleString T_EXISTS = tsLiteral("exists");
    private static final TruffleString T_INSERTABLE = tsLiteral("insertable");
    private static final TruffleString T_REMOVABLE = tsLiteral("removable");
    private static final TruffleString T_MODIFIABLE = tsLiteral("modifiable");
    private static final TruffleString T_INVOKABLE = tsLiteral("invokable");
    private static final TruffleString T_INTERNAL = tsLiteral("internal");

    @Override
    protected List<com.oracle.truffle.api.dsl.NodeFactory<? extends PythonBuiltinBaseNode>> getNodeFactories() {
        return PolyglotModuleBuiltinsFactory.getFactories();
    }

    @Override
    public void initialize(Python3Core core) {
        super.initialize(core);

        PythonContext context = core.getContext();
        Env env = context.getEnv();
        TruffleString coreHome = context.getCoreHome();
        try {
            TruffleFile coreDir = env.getInternalTruffleFile(coreHome.toJavaStringUncached());
            TruffleFile docDir = coreDir.resolveSibling("docs");
            if (docDir.exists() || docDir.getParent() != null && (docDir = coreDir.getParent().resolveSibling("docs")).exists()) {
                addBuiltinConstant(SpecialAttributeNames.T___DOC__, new String(docDir.resolve("user").resolve("Interoperability.md").readAllBytes()));
            }
        } catch (SecurityException | IOException e) {
        }
    }

    @Builtin(name = "import_value", minNumOfPositionalArgs = 1, parameterNames = {"name"})
    @GenerateNodeFactory
    public abstract static class ImportNode extends PythonBuiltinNode {
        @Specialization
        @TruffleBoundary
        Object importSymbol(TruffleString name) {
            Env env = getContext().getEnv();
            if (!env.isPolyglotBindingsAccessAllowed()) {
                throw PRaiseNode.raiseUncached(this, PythonErrorType.NotImplementedError, ErrorMessages.POLYGLOT_ACCESS_NOT_ALLOWED);
            }
            Object object = env.importSymbol(name.toJavaStringUncached());
            if (object == null) {
                return PNone.NONE;
            }
            return object;
        }
    }

    @Builtin(name = "eval", minNumOfPositionalArgs = 0, parameterNames = {"path", "string", "language"})
    @GenerateNodeFactory
    abstract static class EvalInteropNode extends PythonBuiltinNode {
        @TruffleBoundary
        @Specialization
        Object evalString(@SuppressWarnings("unused") PNone path, TruffleString tvalue, TruffleString tlangOrMimeType) {
            Env env = getContext().getEnv();
            if (!env.isPolyglotEvalAllowed()) {
                throw PRaiseNode.raiseUncached(this, PythonErrorType.NotImplementedError, ErrorMessages.POLYGLOT_ACCESS_NOT_ALLOWED);
            }
            try {
                String value = tvalue.toJavaStringUncached();
                String langOrMimeType = tlangOrMimeType.toJavaStringUncached();
                boolean mimeType = isMimeType(langOrMimeType);
                String lang = mimeType ? findLanguageByMimeType(env, langOrMimeType) : langOrMimeType;
                raiseIfInternal(env, lang);
                LiteralBuilder newBuilder = Source.newBuilder(lang, value, value);
                if (mimeType) {
                    newBuilder = newBuilder.mimeType(langOrMimeType);
                }
                return env.parsePublic(newBuilder.build()).call();
            } catch (RuntimeException e) {
                throw PRaiseNode.raiseUncached(this, NotImplementedError, e);
            }
        }

        private void raiseIfInternal(Env env, String lang) {
            LanguageInfo languageInfo = env.getPublicLanguages().get(lang);
            if (languageInfo != null && languageInfo.isInternal()) {
                throw PRaiseNode.raiseUncached(this, NotImplementedError, ErrorMessages.ACCESS_TO_INTERNAL_LANG_NOT_PERMITTED, lang);
            }
        }

        @TruffleBoundary
        @Specialization
        Object evalFile(TruffleString tpath, @SuppressWarnings("unused") PNone string, TruffleString tlangOrMimeType) {
            Env env = getContext().getEnv();
            if (!env.isPolyglotEvalAllowed()) {
                throw PRaiseNode.raiseUncached(this, PythonErrorType.NotImplementedError, ErrorMessages.POLYGLOT_ACCESS_NOT_ALLOWED);
            }
            try {
                String path = tpath.toJavaStringUncached();
                String langOrMimeType = tlangOrMimeType.toJavaStringUncached();
                boolean mimeType = isMimeType(langOrMimeType);
                String lang = mimeType ? findLanguageByMimeType(env, langOrMimeType) : langOrMimeType;
                raiseIfInternal(env, lang);
                SourceBuilder newBuilder = Source.newBuilder(lang, env.getPublicTruffleFile(path));
                if (mimeType) {
                    newBuilder = newBuilder.mimeType(langOrMimeType);
                }
                return getContext().getEnv().parsePublic(newBuilder.name(path).build()).call();
            } catch (IOException e) {
                throw PRaiseNode.raiseUncached(this, OSError, ErrorMessages.S, e);
            } catch (RuntimeException e) {
                throw PRaiseNode.raiseUncached(this, NotImplementedError, e);
            }
        }

        @TruffleBoundary
        @Specialization
        Object evalFile(TruffleString tpath, @SuppressWarnings("unused") PNone string, @SuppressWarnings("unused") PNone lang) {
            Env env = getContext().getEnv();
            if (!env.isPolyglotEvalAllowed()) {
                throw PRaiseNode.raiseUncached(this, PythonErrorType.NotImplementedError, ErrorMessages.POLYGLOT_ACCESS_NOT_ALLOWED);
            }
            try {
                String path = tpath.toJavaStringUncached();
                return getContext().getEnv().parsePublic(Source.newBuilder(PythonLanguage.ID, env.getPublicTruffleFile(path)).name(path).build()).call();
            } catch (IOException e) {
                throw PRaiseNode.raiseUncached(this, OSError, ErrorMessages.S, e);
            } catch (RuntimeException e) {
                throw PRaiseNode.raiseUncached(this, NotImplementedError, e);
            }
        }

        @SuppressWarnings("unused")
        @Specialization
        static Object evalStringWithoutLang(PNone path, TruffleString string, PNone lang,
                        @Shared @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(ValueError, ErrorMessages.POLYGLOT_EVAL_WITH_STRING_MUST_PASS_LANG);
        }

        @SuppressWarnings("unused")
        @Fallback
        static Object evalWithoutContent(Object path, Object string, Object lang,
                        @Shared @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(ValueError, ErrorMessages.POLYGLOT_EVAL_MUST_PASS_STRINGS);
        }

        @TruffleBoundary(transferToInterpreterOnException = false)
        private static String findLanguageByMimeType(Env env, String mimeType) {
            Map<String, LanguageInfo> languages = env.getPublicLanguages();
            for (String language : languages.keySet()) {
                for (String registeredMimeType : languages.get(language).getMimeTypes()) {
                    if (mimeType.equals(registeredMimeType)) {
                        return language;
                    }
                }
            }
            return null;
        }

        protected boolean isMimeType(String lang) {
            return lang.contains("/");
        }
    }

    @Builtin(name = "export_value", minNumOfPositionalArgs = 1, parameterNames = {"name", "value"})
    @GenerateNodeFactory
    public abstract static class ExportSymbolNode extends PythonBuiltinNode {
        private static final TruffleLogger LOGGER = PythonLanguage.getLogger(ExportSymbolNode.class);

        @Specialization(guards = "!isString(value)")
        @TruffleBoundary
        Object exportSymbolKeyValue(TruffleString name, Object value) {
            Env env = getContext().getEnv();
            if (!env.isPolyglotBindingsAccessAllowed()) {
                throw PRaiseNode.raiseUncached(this, PythonErrorType.NotImplementedError, ErrorMessages.POLYGLOT_ACCESS_NOT_ALLOWED);
            }
            env.exportSymbol(name.toJavaStringUncached(), value);
            return value;
        }

        @Specialization(guards = "!isString(value)")
        @TruffleBoundary
        Object exportSymbolValueKey(Object value, TruffleString name) {
            LOGGER.warning("[deprecation] polyglot.export_value(value, name) is deprecated " +
                            "and will be removed. Please swap the arguments.");
            return exportSymbolKeyValue(name, value);
        }

        @Specialization(guards = "isString(arg1)")
        @TruffleBoundary
        Object exportSymbolAmbiguous(Object arg1, TruffleString arg2) {
            LOGGER.warning("[deprecation] polyglot.export_value(str, str) is ambiguous. In the future, this will " +
                            "default to using the first argument as the name and the second as value, but now it " +
                            "uses the first argument as value and the second as the name.");
            return exportSymbolValueKey(arg1, arg2);
        }

        @Specialization
        @TruffleBoundary
        Object exportSymbol(PFunction fun, @SuppressWarnings("unused") PNone name) {
            Env env = getContext().getEnv();
            if (!env.isPolyglotBindingsAccessAllowed()) {
                throw PRaiseNode.raiseUncached(this, PythonErrorType.NotImplementedError, ErrorMessages.POLYGLOT_ACCESS_NOT_ALLOWED);
            }
            env.exportSymbol(fun.getName().toJavaStringUncached(), fun);
            return fun;
        }

        @Specialization
        @TruffleBoundary
        Object exportSymbol(PBuiltinFunction fun, @SuppressWarnings("unused") PNone name) {
            Env env = getContext().getEnv();
            if (!env.isPolyglotBindingsAccessAllowed()) {
                throw PRaiseNode.raiseUncached(this, PythonErrorType.NotImplementedError, ErrorMessages.POLYGLOT_ACCESS_NOT_ALLOWED);
            }
            env.exportSymbol(fun.getName().toJavaStringUncached(), fun);
            return fun;
        }

        @Specialization(guards = "isModuleMethod(fun)")
        static Object exportSymbol(VirtualFrame frame, Object fun, @SuppressWarnings("unused") PNone name,
                        @Bind("this") Node inliningTarget,
                        @Cached("create(T___NAME__)") GetAttributeNode.GetFixedAttributeNode getNameAttributeNode,
                        @Cached CastToJavaStringNode castToStringNode,
                        @Cached PRaiseNode.Lazy raiseNode) {
            Object attrNameValue = getNameAttributeNode.executeObject(frame, fun);
            String methodName;
            try {
                methodName = castToStringNode.execute(attrNameValue);
            } catch (CannotCastException e) {
                throw raiseNode.get(inliningTarget).raise(PythonBuiltinClassType.TypeError, ErrorMessages.METHOD_NAME_MUST_BE, attrNameValue);
            }
            export(inliningTarget, methodName, fun);
            return fun;
        }

        @Fallback
        static Object exportSymbol(Object value, Object name,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(PythonBuiltinClassType.TypeError, ErrorMessages.EXPECTED_ARG_TYPES_S_S_BUT_NOT_P_P, "function", "object, str", value, name);
        }

        protected static boolean isModuleMethod(Object o) {
            return (o instanceof PMethod m && m.getSelf() instanceof PythonModule) || (o instanceof PBuiltinMethod bm && bm.getSelf() instanceof PythonModule);
        }

        @TruffleBoundary
        private static void export(Node raisingNode, String name, Object obj) {
            Env env = PythonContext.get(raisingNode).getEnv();
            if (!env.isPolyglotBindingsAccessAllowed()) {
                throw PRaiseNode.raiseUncached(raisingNode, PythonErrorType.NotImplementedError, ErrorMessages.POLYGLOT_ACCESS_NOT_ALLOWED);
            }
            env.exportSymbol(name, obj);
        }
    }

    @CompilationFinal static InteropLibrary UNCACHED_INTEROP;

    static InteropLibrary getInterop() {
        if (UNCACHED_INTEROP == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            UNCACHED_INTEROP = InteropLibrary.getFactory().getUncached();
        }
        return UNCACHED_INTEROP;
    }

    abstract static class FitsInNumberNode extends PythonUnaryBuiltinNode {
        static boolean isSupportedNumber(Object number) {
            return number instanceof Number || number instanceof PInt;
        }

        static boolean isWhole(double number) {
            return !(number % 1.0 > 0);
        }

        @Specialization(guards = {"!isSupportedNumber(number)"})
        static boolean unsupported(PythonAbstractObject number,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(PythonBuiltinClassType.TypeError, ARG_MUST_BE_NUMBER, "given", number);
        }
    }

    @Builtin(name = J_FITS_IN_BYTE, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class FitsInByteNode extends FitsInNumberNode {
        static boolean fits(long number) {
            return number >= 0 && number < 256;
        }

        @Specialization
        static boolean check(int number) {
            return fits(number);
        }

        @Specialization
        static boolean check(long number) {
            return fits(number);
        }

        @Specialization
        static boolean check(double number) {
            if (isWhole(number)) {
                return fits((long) number);
            }
            return false;
        }

        @Specialization
        static boolean check(PInt number,
                        @CachedLibrary(limit = "1") InteropLibrary ilib) {
            return ilib.fitsInByte(number);
        }
    }

    @Builtin(name = J_FITS_IN_SHORT, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class FitsInShortNode extends FitsInNumberNode {
        static boolean fits(long number) {
            return number >= Short.MIN_VALUE && number < Short.MAX_VALUE;
        }

        @Specialization
        static boolean check(int number) {
            return fits(number);
        }

        @Specialization
        static boolean check(long number) {
            return fits(number);
        }

        @Specialization
        static boolean check(double number) {
            if (isWhole(number)) {
                return fits((long) number);
            }
            return false;
        }

        @Specialization
        static boolean check(PInt number,
                        @CachedLibrary(limit = "1") InteropLibrary ilib) {
            return ilib.fitsInShort(number);
        }
    }

    @Builtin(name = J_FITS_IN_INT, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class FitsInIntNode extends FitsInNumberNode {
        static boolean fits(long number) {
            return number >= Integer.MIN_VALUE && number < Integer.MAX_VALUE;
        }

        @Specialization
        static boolean check(@SuppressWarnings("unused") int number) {
            return true;
        }

        @Specialization
        static boolean check(long number) {
            return fits(number);
        }

        @Specialization
        static boolean check(double number) {
            if (isWhole(number)) {
                return fits((long) number);
            }
            return false;
        }

        @Specialization
        static boolean check(PInt number,
                        @CachedLibrary(limit = "1") InteropLibrary ilib) {
            return ilib.fitsInInt(number);
        }
    }

    @Builtin(name = J_FITS_IN_LONG, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class FitsInLongNode extends FitsInNumberNode {

        @Specialization
        static boolean check(@SuppressWarnings("unused") int number) {
            return true;
        }

        @Specialization
        static boolean check(@SuppressWarnings("unused") long number) {
            return true;
        }

        @Specialization
        static boolean check(double number) {
            return isWhole(number);
        }

        @Specialization
        static boolean check(PInt number,
                        @CachedLibrary(limit = "1") InteropLibrary ilib) {
            return ilib.fitsInLong(number);
        }
    }

    @Builtin(name = J_FITS_IN_BIG_INTEGER, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class FitsInBigIntegerNode extends FitsInNumberNode {

        @Specialization
        static boolean check(@SuppressWarnings("unused") int number) {
            return true;
        }

        @Specialization
        static boolean check(@SuppressWarnings("unused") long number) {
            return true;
        }

        @Specialization
        static boolean check(double number) {
            return isWhole(number);
        }

        @Specialization
        static boolean check(@SuppressWarnings("unused") PInt number) {
            return true;
        }
    }

    @Builtin(name = J_FITS_IN_FLOAT, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class FitsInFloatNode extends FitsInNumberNode {
        static int PRECISION = 24;
        static long MIN = -(long) Math.pow(2, PRECISION);
        static long MAX = (long) Math.pow(2, PRECISION) - 1;

        static boolean fits(long number) {
            return number >= MIN && number <= MAX;
        }

        @Specialization
        static boolean check(int number) {
            return fits(number);
        }

        @Specialization
        static boolean check(long number) {
            return fits(number);
        }

        @Specialization
        static boolean check(double number) {
            return !Double.isFinite(number) || (float) number == number;
        }

        @Specialization
        static boolean check(@SuppressWarnings("unused") PInt number,
                        @CachedLibrary(limit = "1") InteropLibrary ilib) {
            return ilib.fitsInFloat(number);
        }
    }

    @Builtin(name = J_FITS_IN_DOUBLE, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class FitsInDoubleNode extends FitsInNumberNode {
        static int PRECISION = 53;
        static long MIN = -(long) Math.pow(2, PRECISION);
        static long MAX = (long) Math.pow(2, PRECISION) - 1;

        static boolean fits(long number) {
            return number >= MIN && number <= MAX;
        }

        @Specialization
        static boolean check(@SuppressWarnings("unused") int number) {
            return true;
        }

        @Specialization
        static boolean check(long number) {
            return fits(number);
        }

        @Specialization
        static boolean check(@SuppressWarnings("true") double number) {
            return true;
        }

        @Specialization
        static boolean check(@SuppressWarnings("unused") PInt number,
                        @CachedLibrary(limit = "1") InteropLibrary ilib) {
            return ilib.fitsInDouble(number);
        }
    }

    @Builtin(name = J_REGISTER_HOST_INTEROP_BEHAVIOR, minNumOfPositionalArgs = 1, maxNumOfPositionalArgs = 1, takesVarArgs = true, takesVarKeywordArgs = true)
    @GenerateNodeFactory
    public abstract static class RegisterInteropBehaviorNode extends PythonVarargsBuiltinNode {
        public static final HiddenKey HOST_INTEROP_BEHAVIOR = new HiddenKey(J___GRAALPYTHON_HOST_INTEROP_BEHAVIOR__);

        @Specialization
        Object register(PythonAbstractObject receiver, @SuppressWarnings("unused") Object[] arguments, PKeyword[] keywords,
                        @Bind("this") Node inliningTarget,
                        @Cached TypeNodes.IsTypeNode isTypeNode,
                        @Cached CastToJavaStringNode castToJavaStringNode,
                        @Cached PythonObjectFactory factory,
                        @CachedLibrary(limit = "1") DynamicObjectLibrary dylib) {
            if (isTypeNode.execute(inliningTarget, receiver)) {
                final PFunction[] functions = new PFunction[HostInteropBehaviorMethod.values().length];
                final boolean[] constants = new boolean[HostInteropBehaviorMethod.values().length];

                for (PKeyword kw : keywords) {
                    String name = castToJavaStringNode.execute(kw.getName());
                    Object value = kw.getValue();
                    HostInteropBehaviorMethod method = HostInteropBehaviorMethod.valueOf(name);

                    if (method.constantBoolean && value instanceof Boolean boolValue) {
                        constants[method.ordinal()] = boolValue;
                    } else if (!method.constantBoolean && value instanceof PFunction function) {
                        functions[method.ordinal()] = function;
                        // validate the function
                        if (function.getKwDefaults().length != 0) {
                            throw raise(ValueError, S_TAKES_NO_KEYWORD_ARGS, "function");
                        } else if (function.getCode().getCellVars().length != 0) {
                            throw raise(ValueError, S_CANNOT_HAVE_S, "function", "cell vars");
                        } else if (function.getCode().getFreeVars().length != 0) {
                            throw raise(ValueError, S_CANNOT_HAVE_S, "function", "free vars");
                        }
                    } else {
                        throw raise(ValueError, ARG_S_MUST_BE_S_NOT_P, method.name, method.constantBoolean ? "a boolean" : "a pure function", value);
                    }
                }

                PHostInteropBehavior behavior = factory.createHostInteropBehavior(receiver, functions, constants);
                dylib.put(receiver, HOST_INTEROP_BEHAVIOR, behavior);
                return PNone.NONE;
            }
            throw raise(ValueError, S_ARG_MUST_BE_S_NOT_P, "first", "a type", receiver);
        }
    }

    @Builtin(name = "__read__", minNumOfPositionalArgs = 2)
    @GenerateNodeFactory
    public abstract static class ReadNode extends PythonBuiltinNode {
        @Specialization
        @TruffleBoundary
        Object read(Object receiver, Object key) {
            try {
                if (key instanceof TruffleString) {
                    return getInterop().readMember(receiver, ((TruffleString) key).toJavaStringUncached());
                } else if (isJavaString(key)) {
                    return getInterop().readMember(receiver, (String) key);
                } else if (key instanceof Number) {
                    return getInterop().readArrayElement(receiver, ((Number) key).longValue());
                } else {
                    throw PRaiseNode.raiseUncached(this, PythonErrorType.AttributeError, ErrorMessages.UNKNOWN_ATTR, key);
                }
            } catch (UnknownIdentifierException | UnsupportedMessageException | InvalidArrayIndexException e) {
                throw PRaiseNode.raiseUncached(this, PythonErrorType.AttributeError, e);
            }
        }
    }

    @Builtin(name = "__write__", minNumOfPositionalArgs = 3)
    @GenerateNodeFactory
    public abstract static class WriteNode extends PythonBuiltinNode {
        @Specialization
        @TruffleBoundary
        Object write(Object receiver, Object key, Object value) {
            try {
                if (key instanceof TruffleString) {
                    getInterop().writeMember(receiver, ((TruffleString) key).toJavaStringUncached(), value);
                } else if (isJavaString(key)) {
                    getInterop().writeMember(receiver, (String) key, value);
                } else if (key instanceof Number) {
                    getInterop().writeArrayElement(receiver, ((Number) key).longValue(), value);
                } else {
                    throw PRaiseNode.raiseUncached(this, PythonErrorType.AttributeError, ErrorMessages.UNKNOWN_ATTR, key);
                }
            } catch (UnknownIdentifierException | UnsupportedMessageException | UnsupportedTypeException | InvalidArrayIndexException e) {
                throw PRaiseNode.raiseUncached(this, PythonErrorType.AttributeError, e);
            }
            return PNone.NONE;
        }
    }

    @Builtin(name = "__remove__", minNumOfPositionalArgs = 2)
    @GenerateNodeFactory
    public abstract static class removeNode extends PythonBuiltinNode {
        @Specialization
        @TruffleBoundary
        Object remove(Object receiver, Object key) {
            try {
                if (key instanceof TruffleString) {
                    getInterop().removeMember(receiver, ((TruffleString) key).toJavaStringUncached());
                } else if (isJavaString(key)) {
                    getInterop().removeMember(receiver, (String) key);
                } else if (key instanceof Number) {
                    getInterop().removeArrayElement(receiver, ((Number) key).longValue());
                } else {
                    throw PRaiseNode.raiseUncached(this, PythonErrorType.AttributeError, ErrorMessages.UNKNOWN_ATTR, key);
                }
            } catch (UnknownIdentifierException | UnsupportedMessageException | InvalidArrayIndexException e) {
                throw PRaiseNode.raiseUncached(this, PythonErrorType.AttributeError, e);
            }
            return PNone.NONE;
        }
    }

    @Builtin(name = "__execute__", minNumOfPositionalArgs = 1, takesVarArgs = true)
    @GenerateNodeFactory
    public abstract static class executeNode extends PythonBuiltinNode {
        @Specialization
        static Object exec(Object receiver, Object[] arguments,
                        @Bind("this") Node inliningTarget,
                        @Cached PRaiseNode.Lazy raiseNode) {
            try {
                return getInterop().execute(receiver, arguments);
            } catch (UnsupportedMessageException | UnsupportedTypeException | ArityException e) {
                throw raiseNode.get(inliningTarget).raise(PythonErrorType.AttributeError, e);
            }
        }
    }

    @Builtin(name = "__new__", minNumOfPositionalArgs = 1, takesVarArgs = true)
    @GenerateNodeFactory
    public abstract static class newNode extends PythonBuiltinNode {
        @Specialization
        static Object instantiate(Object receiver, Object[] arguments,
                        @Bind("this") Node inliningTarget,
                        @Cached PRaiseNode.Lazy raiseNode) {
            try {
                return getInterop().instantiate(receiver, arguments);
            } catch (UnsupportedMessageException | UnsupportedTypeException | ArityException e) {
                throw raiseNode.get(inliningTarget).raise(PythonErrorType.AttributeError, e);
            }
        }
    }

    @Builtin(name = "__invoke__", minNumOfPositionalArgs = 2, takesVarArgs = true)
    @GenerateNodeFactory
    public abstract static class invokeNode extends PythonBuiltinNode {
        @Specialization
        static Object invoke(Object receiver, TruffleString key, Object[] arguments,
                        @Bind("this") Node inliningTarget,
                        @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                        @Cached PRaiseNode.Lazy raiseNode) {
            try {
                return getInterop().invokeMember(receiver, toJavaStringNode.execute(key), arguments);
            } catch (UnsupportedMessageException | UnsupportedTypeException | ArityException | UnknownIdentifierException e) {
                throw raiseNode.get(inliningTarget).raise(PythonErrorType.AttributeError, e);
            }
        }
    }

    @Builtin(name = "__is_null__", minNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class IsNullNode extends PythonBuiltinNode {
        @Specialization
        static boolean isNull(Object receiver) {
            return getInterop().isNull(receiver);
        }
    }

    @Builtin(name = "__has_size__", minNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class HasSizeNode extends PythonBuiltinNode {
        @Specialization
        static boolean hasSize(Object receiver) {
            return getInterop().hasArrayElements(receiver);
        }
    }

    @Builtin(name = "__get_size__", minNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class GetSizeNode extends PythonBuiltinNode {
        @Specialization
        static Object getSize(Object receiver,
                        @Bind("this") Node inliningTarget,
                        @Cached PRaiseNode.Lazy raiseNode) {
            try {
                return getInterop().getArraySize(receiver);
            } catch (UnsupportedMessageException e) {
                throw raiseNode.get(inliningTarget).raise(PythonErrorType.TypeError, e);
            }
        }
    }

    @Builtin(name = "__is_boxed__", minNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class IsBoxedNode extends PythonBuiltinNode {
        @Specialization
        static boolean isBoxed(Object receiver) {
            return getInterop().isString(receiver) || getInterop().fitsInDouble(receiver) || getInterop().fitsInLong(receiver);
        }
    }

    @Builtin(name = "__has_keys__", minNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class HasKeysNode extends PythonBuiltinNode {
        @Specialization
        static boolean hasKeys(Object receiver) {
            return getInterop().hasMembers(receiver);
        }
    }

    @Builtin(name = "__key_info__", minNumOfPositionalArgs = 3)
    @GenerateNodeFactory
    public abstract static class KeyInfoNode extends PythonBuiltinNode {
        @Specialization
        static boolean keyInfo(Object receiver, TruffleString tmember, TruffleString info,
                        @Cached TruffleString.ToJavaStringNode toJavaStringNode,
                        @Cached TruffleString.EqualNode equalNode) {
            String member = toJavaStringNode.execute(tmember);
            if (equalNode.execute(info, T_READ_SIDE_EFFECTS, TS_ENCODING)) {
                return getInterop().hasMemberReadSideEffects(receiver, member);
            } else if (equalNode.execute(info, T_WRITE_SIDE_EFFECTS, TS_ENCODING)) {
                return getInterop().hasMemberWriteSideEffects(receiver, member);
            } else if (equalNode.execute(info, T_EXISTS, TS_ENCODING)) {
                return getInterop().isMemberExisting(receiver, member);
            } else if (equalNode.execute(info, T_READABLE, TS_ENCODING)) {
                return getInterop().isMemberReadable(receiver, member);
            } else if (equalNode.execute(info, T_WRITABLE, TS_ENCODING)) {
                return getInterop().isMemberWritable(receiver, member);
            } else if (equalNode.execute(info, T_INSERTABLE, TS_ENCODING)) {
                return getInterop().isMemberInsertable(receiver, member);
            } else if (equalNode.execute(info, T_REMOVABLE, TS_ENCODING)) {
                return getInterop().isMemberRemovable(receiver, member);
            } else if (equalNode.execute(info, T_MODIFIABLE, TS_ENCODING)) {
                return getInterop().isMemberModifiable(receiver, member);
            } else if (equalNode.execute(info, T_INVOKABLE, TS_ENCODING)) {
                return getInterop().isMemberInvocable(receiver, member);
            } else if (equalNode.execute(info, T_INTERNAL, TS_ENCODING)) {
                return getInterop().isMemberInternal(receiver, member);
            } else {
                return false;
            }
        }
    }

    @Builtin(name = "__keys__", minNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    public abstract static class KeysNode extends PythonBuiltinNode {
        @Specialization
        static Object remove(Object receiver,
                        @Bind("this") Node inliningTarget,
                        @Cached PRaiseNode.Lazy raiseNode) {
            try {
                return getInterop().getMembers(receiver);
            } catch (UnsupportedMessageException e) {
                throw raiseNode.get(inliningTarget).raise(PythonErrorType.TypeError, e);
            }
        }
    }

    @Builtin(name = "__element_info__", minNumOfPositionalArgs = 3)
    @GenerateNodeFactory
    @TypeSystemReference(PythonArithmeticTypes.class)
    public abstract static class ArrayElementInfoNode extends PythonBuiltinNode {
        @Specialization
        static boolean keyInfo(Object receiver, long member, TruffleString info,
                        @Cached TruffleString.EqualNode equalNode) {
            if (equalNode.execute(info, T_EXISTS, TS_ENCODING)) {
                return getInterop().isArrayElementExisting(receiver, member);
            } else if (equalNode.execute(info, T_READABLE, TS_ENCODING)) {
                return getInterop().isArrayElementReadable(receiver, member);
            } else if (equalNode.execute(info, T_WRITABLE, TS_ENCODING)) {
                return getInterop().isArrayElementWritable(receiver, member);
            } else if (equalNode.execute(info, T_INSERTABLE, TS_ENCODING)) {
                return getInterop().isArrayElementInsertable(receiver, member);
            } else if (equalNode.execute(info, T_REMOVABLE, TS_ENCODING)) {
                return getInterop().isArrayElementRemovable(receiver, member);
            } else if (equalNode.execute(info, T_MODIFIABLE, TS_ENCODING)) {
                return getInterop().isArrayElementModifiable(receiver, member);
            } else {
                return false;
            }
        }
    }

    @Builtin(name = "storage", minNumOfPositionalArgs = 1)
    @GenerateNodeFactory
    @TypeSystemReference(PythonArithmeticTypes.class)
    public abstract static class StorageNode extends PythonUnaryBuiltinNode {
        @Specialization
        static Object doSequence(PSequence seq,
                        @Bind("this") Node inliningTarget,
                        @Cached SequenceNodes.GetSequenceStorageNode getSequenceStorageNode) {
            SequenceStorage storage = getSequenceStorageNode.execute(inliningTarget, seq);
            return PythonContext.get(inliningTarget).getEnv().asGuestValue(storage.getInternalArrayObject());
        }

        @Fallback
        static Object doError(Object object,
                        @Cached PRaiseNode raiseNode) {
            throw raiseNode.raise(PythonBuiltinClassType.TypeError, ErrorMessages.UNSUPPORTED_OPERAND_P, object);
        }
    }

}
