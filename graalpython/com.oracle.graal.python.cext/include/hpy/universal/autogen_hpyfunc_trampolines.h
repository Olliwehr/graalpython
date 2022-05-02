/* MIT License
 *
 * Copyright (c) 2020, 2022, Oracle and/or its affiliates.
 * Copyright (c) 2019 pyhandle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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

   This file is automatically generated by hpy.tools.autogen.hpyfunc.autogen_hpyfunc_trampoline_h
   See also hpy.tools.autogen and hpy/tools/public_api.h

   Run this to regenerate:
       make autogen

*/

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject * result;
} _HPyFunc_args_UNARYFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_UNARYFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0) \
    { \
        _HPyFunc_args_UNARYFUNC a = { arg0 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_UNARYFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject *arg1;
    cpy_PyObject * result;
} _HPyFunc_args_BINARYFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_BINARYFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0, cpy_PyObject *arg1) \
    { \
        _HPyFunc_args_BINARYFUNC a = { arg0, arg1 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_BINARYFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject *arg1;
    cpy_PyObject *arg2;
    cpy_PyObject * result;
} _HPyFunc_args_TERNARYFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_TERNARYFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0, cpy_PyObject *arg1, cpy_PyObject *arg2) \
    { \
        _HPyFunc_args_TERNARYFUNC a = { arg0, arg1, arg2 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_TERNARYFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    int result;
} _HPyFunc_args_INQUIRY;

#define _HPyFunc_TRAMPOLINE_HPyFunc_INQUIRY(SYM, IMPL) \
    static int SYM(cpy_PyObject *arg0) \
    { \
        _HPyFunc_args_INQUIRY a = { arg0 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_INQUIRY, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    HPy_ssize_t result;
} _HPyFunc_args_LENFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_LENFUNC(SYM, IMPL) \
    static HPy_ssize_t SYM(cpy_PyObject *arg0) \
    { \
        _HPyFunc_args_LENFUNC a = { arg0 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_LENFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    HPy_ssize_t arg1;
    cpy_PyObject * result;
} _HPyFunc_args_SSIZEARGFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_SSIZEARGFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0, HPy_ssize_t arg1) \
    { \
        _HPyFunc_args_SSIZEARGFUNC a = { arg0, arg1 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_SSIZEARGFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    HPy_ssize_t arg1;
    HPy_ssize_t arg2;
    cpy_PyObject * result;
} _HPyFunc_args_SSIZESSIZEARGFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_SSIZESSIZEARGFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0, HPy_ssize_t arg1, HPy_ssize_t arg2) \
    { \
        _HPyFunc_args_SSIZESSIZEARGFUNC a = { arg0, arg1, arg2 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_SSIZESSIZEARGFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    HPy_ssize_t arg1;
    cpy_PyObject *arg2;
    int result;
} _HPyFunc_args_SSIZEOBJARGPROC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_SSIZEOBJARGPROC(SYM, IMPL) \
    static int SYM(cpy_PyObject *arg0, HPy_ssize_t arg1, cpy_PyObject *arg2) \
    { \
        _HPyFunc_args_SSIZEOBJARGPROC a = { arg0, arg1, arg2 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_SSIZEOBJARGPROC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    HPy_ssize_t arg1;
    HPy_ssize_t arg2;
    cpy_PyObject *arg3;
    int result;
} _HPyFunc_args_SSIZESSIZEOBJARGPROC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_SSIZESSIZEOBJARGPROC(SYM, IMPL) \
    static int SYM(cpy_PyObject *arg0, HPy_ssize_t arg1, HPy_ssize_t arg2, cpy_PyObject *arg3) \
    { \
        _HPyFunc_args_SSIZESSIZEOBJARGPROC a = { arg0, arg1, arg2, arg3 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_SSIZESSIZEOBJARGPROC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject *arg1;
    cpy_PyObject *arg2;
    int result;
} _HPyFunc_args_OBJOBJARGPROC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_OBJOBJARGPROC(SYM, IMPL) \
    static int SYM(cpy_PyObject *arg0, cpy_PyObject *arg1, cpy_PyObject *arg2) \
    { \
        _HPyFunc_args_OBJOBJARGPROC a = { arg0, arg1, arg2 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_OBJOBJARGPROC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    void *arg0;
} _HPyFunc_args_FREEFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_FREEFUNC(SYM, IMPL) \
    static void SYM(void *arg0) \
    { \
        _HPyFunc_args_FREEFUNC a = { arg0 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_FREEFUNC, (HPyCFunction)IMPL, &a); \
        return; \
    }

typedef struct {
    cpy_PyObject *arg0;
    char *arg1;
    cpy_PyObject * result;
} _HPyFunc_args_GETATTRFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_GETATTRFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0, char *arg1) \
    { \
        _HPyFunc_args_GETATTRFUNC a = { arg0, arg1 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_GETATTRFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject *arg1;
    cpy_PyObject * result;
} _HPyFunc_args_GETATTROFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_GETATTROFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0, cpy_PyObject *arg1) \
    { \
        _HPyFunc_args_GETATTROFUNC a = { arg0, arg1 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_GETATTROFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    char *arg1;
    cpy_PyObject *arg2;
    int result;
} _HPyFunc_args_SETATTRFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_SETATTRFUNC(SYM, IMPL) \
    static int SYM(cpy_PyObject *arg0, char *arg1, cpy_PyObject *arg2) \
    { \
        _HPyFunc_args_SETATTRFUNC a = { arg0, arg1, arg2 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_SETATTRFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject *arg1;
    cpy_PyObject *arg2;
    int result;
} _HPyFunc_args_SETATTROFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_SETATTROFUNC(SYM, IMPL) \
    static int SYM(cpy_PyObject *arg0, cpy_PyObject *arg1, cpy_PyObject *arg2) \
    { \
        _HPyFunc_args_SETATTROFUNC a = { arg0, arg1, arg2 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_SETATTROFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject * result;
} _HPyFunc_args_REPRFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_REPRFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0) \
    { \
        _HPyFunc_args_REPRFUNC a = { arg0 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_REPRFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    HPy_hash_t result;
} _HPyFunc_args_HASHFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_HASHFUNC(SYM, IMPL) \
    static HPy_hash_t SYM(cpy_PyObject *arg0) \
    { \
        _HPyFunc_args_HASHFUNC a = { arg0 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_HASHFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject * result;
} _HPyFunc_args_GETITERFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_GETITERFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0) \
    { \
        _HPyFunc_args_GETITERFUNC a = { arg0 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_GETITERFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject * result;
} _HPyFunc_args_ITERNEXTFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_ITERNEXTFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0) \
    { \
        _HPyFunc_args_ITERNEXTFUNC a = { arg0 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_ITERNEXTFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject *arg1;
    cpy_PyObject *arg2;
    cpy_PyObject * result;
} _HPyFunc_args_DESCRGETFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_DESCRGETFUNC(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0, cpy_PyObject *arg1, cpy_PyObject *arg2) \
    { \
        _HPyFunc_args_DESCRGETFUNC a = { arg0, arg1, arg2 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_DESCRGETFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject *arg1;
    cpy_PyObject *arg2;
    int result;
} _HPyFunc_args_DESCRSETFUNC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_DESCRSETFUNC(SYM, IMPL) \
    static int SYM(cpy_PyObject *arg0, cpy_PyObject *arg1, cpy_PyObject *arg2) \
    { \
        _HPyFunc_args_DESCRSETFUNC a = { arg0, arg1, arg2 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_DESCRSETFUNC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    void *arg1;
    cpy_PyObject * result;
} _HPyFunc_args_GETTER;

#define _HPyFunc_TRAMPOLINE_HPyFunc_GETTER(SYM, IMPL) \
    static cpy_PyObject *SYM(cpy_PyObject *arg0, void *arg1) \
    { \
        _HPyFunc_args_GETTER a = { arg0, arg1 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_GETTER, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject *arg1;
    void *arg2;
    int result;
} _HPyFunc_args_SETTER;

#define _HPyFunc_TRAMPOLINE_HPyFunc_SETTER(SYM, IMPL) \
    static int SYM(cpy_PyObject *arg0, cpy_PyObject *arg1, void *arg2) \
    { \
        _HPyFunc_args_SETTER a = { arg0, arg1, arg2 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_SETTER, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
    cpy_PyObject *arg1;
    int result;
} _HPyFunc_args_OBJOBJPROC;

#define _HPyFunc_TRAMPOLINE_HPyFunc_OBJOBJPROC(SYM, IMPL) \
    static int SYM(cpy_PyObject *arg0, cpy_PyObject *arg1) \
    { \
        _HPyFunc_args_OBJOBJPROC a = { arg0, arg1 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_OBJOBJPROC, (HPyCFunction)IMPL, &a); \
        return a.result; \
    }

typedef struct {
    cpy_PyObject *arg0;
} _HPyFunc_args_DESTRUCTOR;

#define _HPyFunc_TRAMPOLINE_HPyFunc_DESTRUCTOR(SYM, IMPL) \
    static void SYM(cpy_PyObject *arg0) \
    { \
        _HPyFunc_args_DESTRUCTOR a = { arg0 }; \
        _HPy_CallRealFunctionFromTrampoline( \
           _ctx_for_trampolines, HPyFunc_DESTRUCTOR, (HPyCFunction)IMPL, &a); \
        return; \
    }

