/*
 * Copyright (c) 2018, Oracle and/or its affiliates.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or data
 * (collectively the "Software"), free of charge and under any and all copyright
 * rights in the Software, and any and all patent rights owned or freely
 * licensable by each licensor hereunder covering either (i) the unmodified
 * Software as contributed to or provided by such licensor, or (ii) the Larger
 * Works (as defined below), to deal in both
 *
 * (a) the Software, and
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 *     one is included with the Software (each a "Larger Work" to which the
 *     Software is contributed by such licensors),
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
#include "capi.h"

void _PyErr_BadInternalCall(const char *filename, int lineno) {
    polyglot_invoke(PY_TRUFFLE_CEXT, "_PyErr_BadInternalCall", polyglot_from_string(filename, "utf-8"), lineno, Py_NoValue);
}

#undef PyErr_BadInternalCall
void PyErr_BadInternalCall(void) {
    assert(0 && "bad argument to internal function");
    truffle_invoke(PY_TRUFFLE_CEXT, "PyTruffle_Err_Format", to_java(PyExc_SystemError), truffle_read_string("bad argument to internal function"));
}
#define PyErr_BadInternalCall() _PyErr_BadInternalCall(__FILE__, __LINE__)


PyObject* PyErr_Occurred() {
	PyObject* result = truffle_invoke(PY_TRUFFLE_CEXT, "PyErr_Occurred");
	if (result == Py_NoValue) {
		return NULL;
	}
    return to_sulong(result);
}

void PyErr_SetString(PyObject *exception, const char *string) {
    PyObject *value = PyUnicode_FromString(string);
    PyErr_SetObject(exception, value);
}

void PyErr_SetObject(PyObject *exception, PyObject *value) {
    truffle_invoke(PY_TRUFFLE_CEXT, "PyErr_CreateAndSetException", to_java(exception), to_java(value));
}

void PyErr_Clear(void) {
    PyErr_Restore(NULL, NULL, NULL);
}

void PyErr_Restore(PyObject *type, PyObject *value, PyObject *traceback) {
	truffle_invoke(PY_TRUFFLE_CEXT, "PyErr_Restore", to_java(type), to_java(value), to_java(traceback));
}

PyObject* PyErr_NewException(const char *name, PyObject *base, PyObject *dict) {
    if (base == NULL) {
        base = PyExc_Exception;
    }
    if (dict == NULL) {
        dict = PyDict_New();
    }
    return to_sulong(truffle_invoke(PY_TRUFFLE_CEXT, "PyErr_NewException", truffle_read_string(name), to_java(base), to_java(dict)));
}

int PyErr_GivenExceptionMatches(PyObject *err, PyObject *exc) {
    if (err == NULL || exc == NULL) {
        /* maybe caused by "import exceptions" that failed early on */
        return 0;
    }
    return truffle_invoke_i(PY_TRUFFLE_CEXT, "PyErr_GivenExceptionMatches", to_java(err), to_java(exc));
}

void PyErr_SetNone(PyObject *exception) {
    truffle_invoke(PY_TRUFFLE_CEXT, "PyErr_CreateAndSetException", to_java(exception), Py_None);
}

static void _PyErr_GetOrFetchExcInfo(int consume, PyObject **p_type, PyObject **p_value, PyObject **p_traceback) {
    PyObject* result = truffle_invoke(PY_TRUFFLE_CEXT, "PyErr_Fetch", (consume ? Py_True : Py_False));
    if(result == ERROR_MARKER) {
    	*p_type = NULL;
    	*p_value = NULL;
    	*p_traceback = NULL;
    } else {
    	*p_type = PyTuple_GetItem(result, 0);
    	*p_value = PyTuple_GetItem(result, 1);
    	*p_traceback = PyTuple_GetItem(result, 2);
    }
}

void PyErr_Fetch(PyObject **p_type, PyObject **p_value, PyObject **p_traceback) {
	_PyErr_GetOrFetchExcInfo(1, p_type, p_value, p_traceback);
}

void PyErr_GetExcInfo(PyObject **p_type, PyObject **p_value, PyObject **p_traceback) {
	_PyErr_GetOrFetchExcInfo(0, p_type, p_value, p_traceback);
    Py_XINCREF(*p_type);
    Py_XINCREF(*p_value);
    Py_XINCREF(*p_traceback);
}

// taken from CPython "Python/errors.c"
void PyErr_Print(void) {
    PyErr_PrintEx(1);
}

void PyErr_PrintEx(int set_sys_last_vars) {
    truffle_invoke(PY_TRUFFLE_CEXT, "PyErr_PrintEx", set_sys_last_vars);
}

// taken from CPython "Python/errors.c"
int PyErr_BadArgument(void) {
    PyErr_SetString(PyExc_TypeError, "bad argument type for built-in operation");
    return 0;
}

// partially taken from CPython "Python/errors.c"
PyObject * PyErr_NoMemory(void) {
    PyErr_SetNone(PyExc_MemoryError);
    return NULL;
}

// taken from CPython "Python/errors.c"
int PyErr_ExceptionMatches(PyObject *exc) {
    return PyErr_GivenExceptionMatches(PyErr_Occurred(), exc);
}

PyObject* PyTruffle_Err_Format(PyObject* exception, const char* fmt, int s, void* v0, void* v1, void* v2, void* v3, void* v4, void* v5, void* v6, void* v7, void* v8, void* v9) {
    PyObject *formatted_msg = PyTruffle_Unicode_FromFormat(fmt, s, v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
    truffle_invoke(PY_TRUFFLE_CEXT, "PyErr_CreateAndSetException", to_java(exception), to_java(formatted_msg));
    return NULL;
}

void PyErr_WriteUnraisable(PyObject *obj) {
    truffle_invoke(PY_TRUFFLE_CEXT, "PyErr_WriteUnraisable", to_java(obj));
}

void PyErr_Display(PyObject *exception, PyObject *value, PyObject *tb) {
    truffle_invoke(PY_TRUFFLE_CEXT, "PyErr_Display", to_java(exception), to_java(value), to_java(tb));
}
