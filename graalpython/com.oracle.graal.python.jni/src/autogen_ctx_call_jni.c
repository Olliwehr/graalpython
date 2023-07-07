/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
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


/*
   DO NOT EDIT THIS FILE!

   This file is automatically generated by hpy.tools.autogen.graalpy.autogen_ctx_call_jni
   See also hpy.tools.autogen and hpy/tools/public_api.h

   Run this to regenerate:
       make autogen

*/

#include "hpy_jni.h"
#include "com_oracle_graal_python_builtins_objects_cext_hpy_jni_GraalHPyJNITrampolines.h"

#define TRAMPOLINE(name) Java_com_oracle_graal_python_builtins_objects_cext_hpy_jni_GraalHPyJNITrampolines_ ## name

/*******************************************************************
 *                    UNIVERSAL MODE TRAMPOLINES                   *
 ******************************************************************/

JNIEXPORT jlong JNICALL TRAMPOLINE(executeModuleInit)(JNIEnv *env, jclass clazz, jlong target)
{
    return (jlong) (((HPyModuleDef *(*)(void)) target)());
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeNoargs)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong self)
{
    HPyFunc_noargs f = (HPyFunc_noargs)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(self)));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeO)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong self, jlong arg)
{
    HPyFunc_o f = (HPyFunc_o)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(self), _jlong2h(arg)));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeVarargs)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong self, jlong args, jlong nargs)
{
    HPyFunc_varargs f = (HPyFunc_varargs)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(self), (const HPy *) args, (size_t) nargs));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeKeywords)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong self, jlong args, jlong nargs, jlong kwnames)
{
    HPyFunc_keywords f = (HPyFunc_keywords)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(self), (const HPy *) args, (size_t) nargs, _jlong2h(kwnames)));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeUnaryfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyFunc_unaryfunc f = (HPyFunc_unaryfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0)));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeBinaryfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyFunc_binaryfunc f = (HPyFunc_binaryfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0), _jlong2h(arg1)));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeTernaryfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyFunc_ternaryfunc f = (HPyFunc_ternaryfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0), _jlong2h(arg1), _jlong2h(arg2)));
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeInquiry)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyFunc_inquiry f = (HPyFunc_inquiry)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(arg0));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeLenfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyFunc_lenfunc f = (HPyFunc_lenfunc)target;
    return (jlong) f((HPyContext *)ctx, _jlong2h(arg0));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeSsizeargfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyFunc_ssizeargfunc f = (HPyFunc_ssizeargfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0), (HPy_ssize_t) arg1));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeSsizessizeargfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyFunc_ssizessizeargfunc f = (HPyFunc_ssizessizeargfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0), (HPy_ssize_t) arg1, (HPy_ssize_t) arg2));
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeSsizeobjargproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyFunc_ssizeobjargproc f = (HPyFunc_ssizeobjargproc)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(arg0), (HPy_ssize_t) arg1, _jlong2h(arg2));
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeSsizessizeobjargproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2, jlong arg3)
{
    HPyFunc_ssizessizeobjargproc f = (HPyFunc_ssizessizeobjargproc)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(arg0), (HPy_ssize_t) arg1, (HPy_ssize_t) arg2, _jlong2h(arg3));
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeObjobjargproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyFunc_objobjargproc f = (HPyFunc_objobjargproc)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(arg0), _jlong2h(arg1), _jlong2h(arg2));
}

JNIEXPORT void JNICALL TRAMPOLINE(executeFreefunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyFunc_freefunc f = (HPyFunc_freefunc)target;
    f((HPyContext *)ctx, (void *) arg0);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeGetattrfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyFunc_getattrfunc f = (HPyFunc_getattrfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0), (char *) arg1));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeGetattrofunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyFunc_getattrofunc f = (HPyFunc_getattrofunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0), _jlong2h(arg1)));
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeSetattrfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyFunc_setattrfunc f = (HPyFunc_setattrfunc)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(arg0), (char *) arg1, _jlong2h(arg2));
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeSetattrofunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyFunc_setattrofunc f = (HPyFunc_setattrofunc)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(arg0), _jlong2h(arg1), _jlong2h(arg2));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeReprfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyFunc_reprfunc f = (HPyFunc_reprfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0)));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeHashfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyFunc_hashfunc f = (HPyFunc_hashfunc)target;
    return (jlong) f((HPyContext *)ctx, _jlong2h(arg0));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeRichcmpfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyFunc_richcmpfunc f = (HPyFunc_richcmpfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0), _jlong2h(arg1), (HPy_RichCmpOp) arg2));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeGetiterfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyFunc_getiterfunc f = (HPyFunc_getiterfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0)));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeIternextfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyFunc_iternextfunc f = (HPyFunc_iternextfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0)));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDescrgetfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyFunc_descrgetfunc f = (HPyFunc_descrgetfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0), _jlong2h(arg1), _jlong2h(arg2)));
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDescrsetfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyFunc_descrsetfunc f = (HPyFunc_descrsetfunc)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(arg0), _jlong2h(arg1), _jlong2h(arg2));
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeInitproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong self, jlong args, jlong nargs, jlong kw)
{
    HPyFunc_initproc f = (HPyFunc_initproc)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(self), (const HPy *) args, (HPy_ssize_t) nargs, _jlong2h(kw));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeNewfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong type, jlong args, jlong nargs, jlong kw)
{
    HPyFunc_newfunc f = (HPyFunc_newfunc)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(type), (const HPy *) args, (HPy_ssize_t) nargs, _jlong2h(kw)));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeGetter)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyFunc_getter f = (HPyFunc_getter)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0), (void *) arg1));
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeSetter)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyFunc_setter f = (HPyFunc_setter)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(arg0), _jlong2h(arg1), (void *) arg2);
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeObjobjproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyFunc_objobjproc f = (HPyFunc_objobjproc)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(arg0), _jlong2h(arg1));
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeGetbufferproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jint arg2)
{
    HPyFunc_getbufferproc f = (HPyFunc_getbufferproc)target;
    return (jint) f((HPyContext *)ctx, _jlong2h(arg0), (HPy_buffer *) arg1, (int) arg2);
}

JNIEXPORT void JNICALL TRAMPOLINE(executeReleasebufferproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyFunc_releasebufferproc f = (HPyFunc_releasebufferproc)target;
    f((HPyContext *)ctx, _jlong2h(arg0), (HPy_buffer *) arg1);
}

JNIEXPORT void JNICALL TRAMPOLINE(executeDestructor)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyFunc_destructor f = (HPyFunc_destructor)target;
    f((HPyContext *)ctx, _jlong2h(arg0));
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeMod_create)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyFunc_mod_create f = (HPyFunc_mod_create)target;
    return _h2jlong(f((HPyContext *)ctx, _jlong2h(arg0)));
}


/*******************************************************************
 *                      DEBUG MODE TRAMPOLINES                     *
 ******************************************************************/

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugNoargs)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong self)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_noargs f = (HPyFunc_noargs)target;
    DHPy dh_self = _jlong2dh(dctx, self);
    DHPy dh_result = f(dctx, dh_self);
    DHPy_close_and_check(dctx, dh_self);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugO)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong self, jlong arg)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_o f = (HPyFunc_o)target;
    DHPy dh_self = _jlong2dh(dctx, self);
    DHPy dh_arg = _jlong2dh(dctx, arg);
    DHPy dh_result = f(dctx, dh_self, dh_arg);
    DHPy_close_and_check(dctx, dh_self);
    DHPy_close_and_check(dctx, dh_arg);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugVarargs)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong self, jlong args, jlong nargs)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_varargs f = (HPyFunc_varargs)target;
    DHPy dh_self = _jlong2dh(dctx, self);
    _ARR_JLONG2DH(dctx, dh_args, args, nargs)
    DHPy dh_result = f(dctx, dh_self, dh_args, (size_t)nargs);
    _ARR_DH_CLOSE(dctx, dh_args, nargs)
    DHPy_close_and_check(dctx, dh_self);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugKeywords)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong self, jlong args, jlong nargs, jlong kwnames)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_keywords f = (HPyFunc_keywords)target;
    DHPy dh_self = _jlong2dh(dctx, self);
    DHPy dh_kwnames = _jlong2dh(dctx, kwnames);
    _ARR_JLONG2DH(dctx, dh_args, args, nargs)
    DHPy dh_result = f(dctx, dh_self, dh_args, (size_t)nargs, dh_kwnames);
    _ARR_DH_CLOSE(dctx, dh_args, nargs)
    DHPy_close_and_check(dctx, dh_self);
    DHPy_close_and_check(dctx, dh_kwnames);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugUnaryfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_unaryfunc f = (HPyFunc_unaryfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_result = f(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg0);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugBinaryfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_binaryfunc f = (HPyFunc_binaryfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg1 = _jlong2dh(dctx, arg1);
    DHPy dh_result = f(dctx, dh_arg0, dh_arg1);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg1);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugTernaryfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_ternaryfunc f = (HPyFunc_ternaryfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg1 = _jlong2dh(dctx, arg1);
    DHPy dh_arg2 = _jlong2dh(dctx, arg2);
    DHPy dh_result = f(dctx, dh_arg0, dh_arg1, dh_arg2);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg1);
    DHPy_close_and_check(dctx, dh_arg2);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDebugInquiry)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_inquiry f = (HPyFunc_inquiry)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    jint result = (jint) f(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg0);
    return result;
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugLenfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_lenfunc f = (HPyFunc_lenfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    jlong result = (jlong) f(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg0);
    return result;
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugSsizeargfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_ssizeargfunc f = (HPyFunc_ssizeargfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_result = f(dctx, dh_arg0, (HPy_ssize_t)arg1);
    DHPy_close_and_check(dctx, dh_arg0);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugSsizessizeargfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_ssizessizeargfunc f = (HPyFunc_ssizessizeargfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_result = f(dctx, dh_arg0, (HPy_ssize_t)arg1, (HPy_ssize_t)arg2);
    DHPy_close_and_check(dctx, dh_arg0);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDebugSsizeobjargproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_ssizeobjargproc f = (HPyFunc_ssizeobjargproc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg2 = _jlong2dh(dctx, arg2);
    jint result = (jint) f(dctx, dh_arg0, (HPy_ssize_t)arg1, dh_arg2);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg2);
    return result;
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDebugSsizessizeobjargproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2, jlong arg3)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_ssizessizeobjargproc f = (HPyFunc_ssizessizeobjargproc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg3 = _jlong2dh(dctx, arg3);
    jint result = (jint) f(dctx, dh_arg0, (HPy_ssize_t)arg1, (HPy_ssize_t)arg2, dh_arg3);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg3);
    return result;
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDebugObjobjargproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_objobjargproc f = (HPyFunc_objobjargproc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg1 = _jlong2dh(dctx, arg1);
    DHPy dh_arg2 = _jlong2dh(dctx, arg2);
    jint result = (jint) f(dctx, dh_arg0, dh_arg1, dh_arg2);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg1);
    DHPy_close_and_check(dctx, dh_arg2);
    return result;
}

JNIEXPORT void JNICALL TRAMPOLINE(executeDebugFreefunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_freefunc f = (HPyFunc_freefunc)target;
    f(dctx, (void *)arg0);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugGetattrfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_getattrfunc f = (HPyFunc_getattrfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_result = f(dctx, dh_arg0, (char *)arg1);
    DHPy_close_and_check(dctx, dh_arg0);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugGetattrofunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_getattrofunc f = (HPyFunc_getattrofunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg1 = _jlong2dh(dctx, arg1);
    DHPy dh_result = f(dctx, dh_arg0, dh_arg1);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg1);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDebugSetattrfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_setattrfunc f = (HPyFunc_setattrfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg2 = _jlong2dh(dctx, arg2);
    jint result = (jint) f(dctx, dh_arg0, (char *)arg1, dh_arg2);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg2);
    return result;
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDebugSetattrofunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_setattrofunc f = (HPyFunc_setattrofunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg1 = _jlong2dh(dctx, arg1);
    DHPy dh_arg2 = _jlong2dh(dctx, arg2);
    jint result = (jint) f(dctx, dh_arg0, dh_arg1, dh_arg2);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg1);
    DHPy_close_and_check(dctx, dh_arg2);
    return result;
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugReprfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_reprfunc f = (HPyFunc_reprfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_result = f(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg0);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugHashfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_hashfunc f = (HPyFunc_hashfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    jlong result = (jlong) f(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg0);
    return result;
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugRichcmpfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_richcmpfunc f = (HPyFunc_richcmpfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg1 = _jlong2dh(dctx, arg1);
    DHPy dh_result = f(dctx, dh_arg0, dh_arg1, (HPy_RichCmpOp)arg2);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg1);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugGetiterfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_getiterfunc f = (HPyFunc_getiterfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_result = f(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg0);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugIternextfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_iternextfunc f = (HPyFunc_iternextfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_result = f(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg0);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugDescrgetfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_descrgetfunc f = (HPyFunc_descrgetfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg1 = _jlong2dh(dctx, arg1);
    DHPy dh_arg2 = _jlong2dh(dctx, arg2);
    DHPy dh_result = f(dctx, dh_arg0, dh_arg1, dh_arg2);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg1);
    DHPy_close_and_check(dctx, dh_arg2);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDebugDescrsetfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_descrsetfunc f = (HPyFunc_descrsetfunc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg1 = _jlong2dh(dctx, arg1);
    DHPy dh_arg2 = _jlong2dh(dctx, arg2);
    jint result = (jint) f(dctx, dh_arg0, dh_arg1, dh_arg2);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg1);
    DHPy_close_and_check(dctx, dh_arg2);
    return result;
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDebugInitproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong self, jlong args, jlong nargs, jlong kw)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_initproc f = (HPyFunc_initproc)target;
    DHPy dh_self = _jlong2dh(dctx, self);
    DHPy dh_kw = _jlong2dh(dctx, kw);
    _ARR_JLONG2DH(dctx, dh_args, args, nargs)
    jint result = (jint) f(dctx, dh_self, dh_args, (HPy_ssize_t)nargs, dh_kw);
    _ARR_DH_CLOSE(dctx, dh_args, nargs)
    DHPy_close_and_check(dctx, dh_self);
    DHPy_close_and_check(dctx, dh_kw);
    return result;
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugNewfunc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong type, jlong args, jlong nargs, jlong kw)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_newfunc f = (HPyFunc_newfunc)target;
    DHPy dh_type = _jlong2dh(dctx, type);
    DHPy dh_kw = _jlong2dh(dctx, kw);
    _ARR_JLONG2DH(dctx, dh_args, args, nargs)
    DHPy dh_result = f(dctx, dh_type, dh_args, (HPy_ssize_t)nargs, dh_kw);
    _ARR_DH_CLOSE(dctx, dh_args, nargs)
    DHPy_close_and_check(dctx, dh_type);
    DHPy_close_and_check(dctx, dh_kw);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugGetter)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_getter f = (HPyFunc_getter)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_result = f(dctx, dh_arg0, (void *)arg1);
    DHPy_close_and_check(dctx, dh_arg0);
    return from_dh(dctx, dh_result);
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDebugSetter)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1, jlong arg2)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_setter f = (HPyFunc_setter)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg1 = _jlong2dh(dctx, arg1);
    jint result = (jint) f(dctx, dh_arg0, dh_arg1, (void *)arg2);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg1);
    return result;
}

JNIEXPORT jint JNICALL TRAMPOLINE(executeDebugObjobjproc)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0, jlong arg1)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_objobjproc f = (HPyFunc_objobjproc)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_arg1 = _jlong2dh(dctx, arg1);
    jint result = (jint) f(dctx, dh_arg0, dh_arg1);
    DHPy_close_and_check(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg1);
    return result;
}

JNIEXPORT void JNICALL TRAMPOLINE(executeDebugDestructor)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_destructor f = (HPyFunc_destructor)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    f(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg0);
}

JNIEXPORT jlong JNICALL TRAMPOLINE(executeDebugMod_create)(JNIEnv *env, jclass clazz, jlong target, jlong ctx, jlong arg0)
{
    HPyContext *dctx = (HPyContext *) ctx;
    HPyFunc_mod_create f = (HPyFunc_mod_create)target;
    DHPy dh_arg0 = _jlong2dh(dctx, arg0);
    DHPy dh_result = f(dctx, dh_arg0);
    DHPy_close_and_check(dctx, dh_arg0);
    return from_dh(dctx, dh_result);
}

#undef TRAMPOLINE
