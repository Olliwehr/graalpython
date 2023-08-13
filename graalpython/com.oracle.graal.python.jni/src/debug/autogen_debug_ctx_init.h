/* MIT License
 *
 * Copyright (c) 2022, 2023, Oracle and/or its affiliates.
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

   This file is automatically generated by hpy.tools.autogen.debug.autogen_debug_ctx_init_h
   See also hpy.tools.autogen and hpy/tools/public_api.h

   Run this to regenerate:
       make autogen

*/

DHPy debug_ctx_Dup(HPyContext *dctx, DHPy h);
void debug_ctx_Close(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Long_FromInt32_t(HPyContext *dctx, int32_t value);
DHPy debug_ctx_Long_FromUInt32_t(HPyContext *dctx, uint32_t value);
DHPy debug_ctx_Long_FromInt64_t(HPyContext *dctx, int64_t v);
DHPy debug_ctx_Long_FromUInt64_t(HPyContext *dctx, uint64_t v);
DHPy debug_ctx_Long_FromSize_t(HPyContext *dctx, size_t value);
DHPy debug_ctx_Long_FromSsize_t(HPyContext *dctx, HPy_ssize_t value);
int32_t debug_ctx_Long_AsInt32_t(HPyContext *dctx, DHPy h);
uint32_t debug_ctx_Long_AsUInt32_t(HPyContext *dctx, DHPy h);
uint32_t debug_ctx_Long_AsUInt32_tMask(HPyContext *dctx, DHPy h);
int64_t debug_ctx_Long_AsInt64_t(HPyContext *dctx, DHPy h);
uint64_t debug_ctx_Long_AsUInt64_t(HPyContext *dctx, DHPy h);
uint64_t debug_ctx_Long_AsUInt64_tMask(HPyContext *dctx, DHPy h);
size_t debug_ctx_Long_AsSize_t(HPyContext *dctx, DHPy h);
HPy_ssize_t debug_ctx_Long_AsSsize_t(HPyContext *dctx, DHPy h);
void *debug_ctx_Long_AsVoidPtr(HPyContext *dctx, DHPy h);
double debug_ctx_Long_AsDouble(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Float_FromDouble(HPyContext *dctx, double v);
double debug_ctx_Float_AsDouble(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Bool_FromBool(HPyContext *dctx, bool v);
HPy_ssize_t debug_ctx_Length(HPyContext *dctx, DHPy h);
int debug_ctx_Number_Check(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Add(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_Subtract(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_Multiply(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_MatrixMultiply(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_FloorDivide(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_TrueDivide(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_Remainder(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_Divmod(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_Power(HPyContext *dctx, DHPy h1, DHPy h2, DHPy h3);
DHPy debug_ctx_Negative(HPyContext *dctx, DHPy h1);
DHPy debug_ctx_Positive(HPyContext *dctx, DHPy h1);
DHPy debug_ctx_Absolute(HPyContext *dctx, DHPy h1);
DHPy debug_ctx_Invert(HPyContext *dctx, DHPy h1);
DHPy debug_ctx_Lshift(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_Rshift(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_And(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_Xor(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_Or(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_Index(HPyContext *dctx, DHPy h1);
DHPy debug_ctx_Long(HPyContext *dctx, DHPy h1);
DHPy debug_ctx_Float(HPyContext *dctx, DHPy h1);
DHPy debug_ctx_InPlaceAdd(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlaceSubtract(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlaceMultiply(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlaceMatrixMultiply(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlaceFloorDivide(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlaceTrueDivide(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlaceRemainder(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlacePower(HPyContext *dctx, DHPy h1, DHPy h2, DHPy h3);
DHPy debug_ctx_InPlaceLshift(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlaceRshift(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlaceAnd(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlaceXor(HPyContext *dctx, DHPy h1, DHPy h2);
DHPy debug_ctx_InPlaceOr(HPyContext *dctx, DHPy h1, DHPy h2);
int debug_ctx_Callable_Check(HPyContext *dctx, DHPy h);
DHPy debug_ctx_CallTupleDict(HPyContext *dctx, DHPy callable, DHPy args, DHPy kw);
DHPy debug_ctx_Call(HPyContext *dctx, DHPy callable, const DHPy *args, size_t nargs, DHPy kwnames);
DHPy debug_ctx_CallMethod(HPyContext *dctx, DHPy name, const DHPy *args, size_t nargs, DHPy kwnames);
void debug_ctx_FatalError(HPyContext *dctx, const char *message);
void debug_ctx_Err_SetString(HPyContext *dctx, DHPy h_type, const char *utf8_message);
void debug_ctx_Err_SetObject(HPyContext *dctx, DHPy h_type, DHPy h_value);
DHPy debug_ctx_Err_SetFromErrnoWithFilename(HPyContext *dctx, DHPy h_type, const char *filename_fsencoded);
void debug_ctx_Err_SetFromErrnoWithFilenameObjects(HPyContext *dctx, DHPy h_type, DHPy filename1, DHPy filename2);
int debug_ctx_Err_Occurred(HPyContext *dctx);
int debug_ctx_Err_ExceptionMatches(HPyContext *dctx, DHPy exc);
void debug_ctx_Err_NoMemory(HPyContext *dctx);
void debug_ctx_Err_Clear(HPyContext *dctx);
DHPy debug_ctx_Err_NewException(HPyContext *dctx, const char *utf8_name, DHPy base, DHPy dict);
DHPy debug_ctx_Err_NewExceptionWithDoc(HPyContext *dctx, const char *utf8_name, const char *utf8_doc, DHPy base, DHPy dict);
int debug_ctx_Err_WarnEx(HPyContext *dctx, DHPy category, const char *utf8_message, HPy_ssize_t stack_level);
void debug_ctx_Err_WriteUnraisable(HPyContext *dctx, DHPy obj);
int debug_ctx_IsTrue(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Type_FromSpec(HPyContext *dctx, HPyType_Spec *spec, HPyType_SpecParam *params);
DHPy debug_ctx_Type_GenericNew(HPyContext *dctx, DHPy type, const DHPy *args, HPy_ssize_t nargs, DHPy kw);
DHPy debug_ctx_GetAttr(HPyContext *dctx, DHPy obj, DHPy name);
DHPy debug_ctx_GetAttr_s(HPyContext *dctx, DHPy obj, const char *utf8_name);
int debug_ctx_HasAttr(HPyContext *dctx, DHPy obj, DHPy name);
int debug_ctx_HasAttr_s(HPyContext *dctx, DHPy obj, const char *utf8_name);
int debug_ctx_SetAttr(HPyContext *dctx, DHPy obj, DHPy name, DHPy value);
int debug_ctx_SetAttr_s(HPyContext *dctx, DHPy obj, const char *utf8_name, DHPy value);
DHPy debug_ctx_GetItem(HPyContext *dctx, DHPy obj, DHPy key);
DHPy debug_ctx_GetItem_i(HPyContext *dctx, DHPy obj, HPy_ssize_t idx);
DHPy debug_ctx_GetItem_s(HPyContext *dctx, DHPy obj, const char *utf8_key);
int debug_ctx_Contains(HPyContext *dctx, DHPy container, DHPy key);
int debug_ctx_SetItem(HPyContext *dctx, DHPy obj, DHPy key, DHPy value);
int debug_ctx_SetItem_i(HPyContext *dctx, DHPy obj, HPy_ssize_t idx, DHPy value);
int debug_ctx_SetItem_s(HPyContext *dctx, DHPy obj, const char *utf8_key, DHPy value);
int debug_ctx_DelItem(HPyContext *dctx, DHPy obj, DHPy key);
int debug_ctx_DelItem_i(HPyContext *dctx, DHPy obj, HPy_ssize_t idx);
int debug_ctx_DelItem_s(HPyContext *dctx, DHPy obj, const char *utf8_key);
DHPy debug_ctx_Type(HPyContext *dctx, DHPy obj);
int debug_ctx_TypeCheck(HPyContext *dctx, DHPy obj, DHPy type);
const char *debug_ctx_Type_GetName(HPyContext *dctx, DHPy type);
int debug_ctx_Type_IsSubtype(HPyContext *dctx, DHPy sub, DHPy type);
int debug_ctx_Is(HPyContext *dctx, DHPy obj, DHPy other);
void *debug_ctx_AsStruct_Object(HPyContext *dctx, DHPy h);
void *debug_ctx_AsStruct_Legacy(HPyContext *dctx, DHPy h);
void *debug_ctx_AsStruct_Type(HPyContext *dctx, DHPy h);
void *debug_ctx_AsStruct_Long(HPyContext *dctx, DHPy h);
void *debug_ctx_AsStruct_Float(HPyContext *dctx, DHPy h);
void *debug_ctx_AsStruct_Unicode(HPyContext *dctx, DHPy h);
void *debug_ctx_AsStruct_Tuple(HPyContext *dctx, DHPy h);
void *debug_ctx_AsStruct_List(HPyContext *dctx, DHPy h);
HPyType_BuiltinShape debug_ctx_Type_GetBuiltinShape(HPyContext *dctx, DHPy h_type);
DHPy debug_ctx_New(HPyContext *dctx, DHPy h_type, void **data);
DHPy debug_ctx_Repr(HPyContext *dctx, DHPy obj);
DHPy debug_ctx_Str(HPyContext *dctx, DHPy obj);
DHPy debug_ctx_ASCII(HPyContext *dctx, DHPy obj);
DHPy debug_ctx_Bytes(HPyContext *dctx, DHPy obj);
DHPy debug_ctx_RichCompare(HPyContext *dctx, DHPy v, DHPy w, int op);
int debug_ctx_RichCompareBool(HPyContext *dctx, DHPy v, DHPy w, int op);
HPy_hash_t debug_ctx_Hash(HPyContext *dctx, DHPy obj);
int debug_ctx_Bytes_Check(HPyContext *dctx, DHPy h);
HPy_ssize_t debug_ctx_Bytes_Size(HPyContext *dctx, DHPy h);
HPy_ssize_t debug_ctx_Bytes_GET_SIZE(HPyContext *dctx, DHPy h);
const char *debug_ctx_Bytes_AsString(HPyContext *dctx, DHPy h);
const char *debug_ctx_Bytes_AS_STRING(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Bytes_FromString(HPyContext *dctx, const char *bytes);
DHPy debug_ctx_Bytes_FromStringAndSize(HPyContext *dctx, const char *bytes, HPy_ssize_t len);
DHPy debug_ctx_Unicode_FromString(HPyContext *dctx, const char *utf8);
int debug_ctx_Unicode_Check(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Unicode_AsASCIIString(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Unicode_AsLatin1String(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Unicode_AsUTF8String(HPyContext *dctx, DHPy h);
const char *debug_ctx_Unicode_AsUTF8AndSize(HPyContext *dctx, DHPy h, HPy_ssize_t *size);
DHPy debug_ctx_Unicode_FromWideChar(HPyContext *dctx, const wchar_t *w, HPy_ssize_t size);
DHPy debug_ctx_Unicode_DecodeFSDefault(HPyContext *dctx, const char *v);
DHPy debug_ctx_Unicode_DecodeFSDefaultAndSize(HPyContext *dctx, const char *v, HPy_ssize_t size);
DHPy debug_ctx_Unicode_EncodeFSDefault(HPyContext *dctx, DHPy h);
HPy_UCS4 debug_ctx_Unicode_ReadChar(HPyContext *dctx, DHPy h, HPy_ssize_t index);
DHPy debug_ctx_Unicode_DecodeASCII(HPyContext *dctx, const char *ascii, HPy_ssize_t size, const char *errors);
DHPy debug_ctx_Unicode_DecodeLatin1(HPyContext *dctx, const char *latin1, HPy_ssize_t size, const char *errors);
DHPy debug_ctx_Unicode_FromEncodedObject(HPyContext *dctx, DHPy obj, const char *encoding, const char *errors);
DHPy debug_ctx_Unicode_Substring(HPyContext *dctx, DHPy str, HPy_ssize_t start, HPy_ssize_t end);
int debug_ctx_List_Check(HPyContext *dctx, DHPy h);
DHPy debug_ctx_List_New(HPyContext *dctx, HPy_ssize_t len);
int debug_ctx_List_Append(HPyContext *dctx, DHPy h_list, DHPy h_item);
int debug_ctx_Dict_Check(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Dict_New(HPyContext *dctx);
DHPy debug_ctx_Dict_Keys(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Dict_Copy(HPyContext *dctx, DHPy h);
int debug_ctx_Tuple_Check(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Tuple_FromArray(HPyContext *dctx, DHPy items[], HPy_ssize_t n);
int debug_ctx_Slice_Unpack(HPyContext *dctx, DHPy slice, HPy_ssize_t *start, HPy_ssize_t *stop, HPy_ssize_t *step);
DHPy debug_ctx_Import_ImportModule(HPyContext *dctx, const char *utf8_name);
DHPy debug_ctx_Capsule_New(HPyContext *dctx, void *pointer, const char *utf8_name, HPyCapsule_Destructor *destructor);
void *debug_ctx_Capsule_Get(HPyContext *dctx, DHPy capsule, _HPyCapsule_key key, const char *utf8_name);
int debug_ctx_Capsule_IsValid(HPyContext *dctx, DHPy capsule, const char *utf8_name);
int debug_ctx_Capsule_Set(HPyContext *dctx, DHPy capsule, _HPyCapsule_key key, void *value);
DHPy debug_ctx_FromPyObject(HPyContext *dctx, cpy_PyObject *obj);
cpy_PyObject *debug_ctx_AsPyObject(HPyContext *dctx, DHPy h);
void debug_ctx_CallRealFunctionFromTrampoline(HPyContext *dctx, HPyFunc_Signature sig, HPyCFunction func, void *args);
HPyListBuilder debug_ctx_ListBuilder_New(HPyContext *dctx, HPy_ssize_t size);
void debug_ctx_ListBuilder_Set(HPyContext *dctx, HPyListBuilder builder, HPy_ssize_t index, DHPy h_item);
DHPy debug_ctx_ListBuilder_Build(HPyContext *dctx, HPyListBuilder builder);
void debug_ctx_ListBuilder_Cancel(HPyContext *dctx, HPyListBuilder builder);
HPyTupleBuilder debug_ctx_TupleBuilder_New(HPyContext *dctx, HPy_ssize_t size);
void debug_ctx_TupleBuilder_Set(HPyContext *dctx, HPyTupleBuilder builder, HPy_ssize_t index, DHPy h_item);
DHPy debug_ctx_TupleBuilder_Build(HPyContext *dctx, HPyTupleBuilder builder);
void debug_ctx_TupleBuilder_Cancel(HPyContext *dctx, HPyTupleBuilder builder);
HPyTracker debug_ctx_Tracker_New(HPyContext *dctx, HPy_ssize_t size);
int debug_ctx_Tracker_Add(HPyContext *dctx, HPyTracker ht, DHPy h);
void debug_ctx_Tracker_ForgetAll(HPyContext *dctx, HPyTracker ht);
void debug_ctx_Tracker_Close(HPyContext *dctx, HPyTracker ht);
void debug_ctx_Field_Store(HPyContext *dctx, DHPy target_object, HPyField *target_field, DHPy h);
DHPy debug_ctx_Field_Load(HPyContext *dctx, DHPy source_object, HPyField source_field);
void debug_ctx_ReenterPythonExecution(HPyContext *dctx, HPyThreadState state);
HPyThreadState debug_ctx_LeavePythonExecution(HPyContext *dctx);
void debug_ctx_Global_Store(HPyContext *dctx, HPyGlobal *global, DHPy h);
DHPy debug_ctx_Global_Load(HPyContext *dctx, HPyGlobal global);
void debug_ctx_Dump(HPyContext *dctx, DHPy h);
DHPy debug_ctx_Compile_s(HPyContext *dctx, const char *utf8_source, const char *utf8_filename, HPy_SourceKind kind);
DHPy debug_ctx_EvalCode(HPyContext *dctx, DHPy code, DHPy globals, DHPy locals);
DHPy debug_ctx_ContextVar_New(HPyContext *dctx, const char *name, DHPy default_value);
int32_t debug_ctx_ContextVar_Get(HPyContext *dctx, DHPy context_var, DHPy default_value, DHPy *result);
DHPy debug_ctx_ContextVar_Set(HPyContext *dctx, DHPy context_var, DHPy value);
int debug_ctx_SetCallFunction(HPyContext *dctx, DHPy h, HPyCallFunction *func);

static inline void debug_ctx_init_fields(HPyContext *dctx, HPyContext *uctx)
{
    dctx->h_None = DHPy_open_immortal(dctx, uctx->h_None);
    dctx->h_True = DHPy_open_immortal(dctx, uctx->h_True);
    dctx->h_False = DHPy_open_immortal(dctx, uctx->h_False);
    dctx->h_NotImplemented = DHPy_open_immortal(dctx, uctx->h_NotImplemented);
    dctx->h_Ellipsis = DHPy_open_immortal(dctx, uctx->h_Ellipsis);
    dctx->h_BaseException = DHPy_open_immortal(dctx, uctx->h_BaseException);
    dctx->h_Exception = DHPy_open_immortal(dctx, uctx->h_Exception);
    dctx->h_StopAsyncIteration = DHPy_open_immortal(dctx, uctx->h_StopAsyncIteration);
    dctx->h_StopIteration = DHPy_open_immortal(dctx, uctx->h_StopIteration);
    dctx->h_GeneratorExit = DHPy_open_immortal(dctx, uctx->h_GeneratorExit);
    dctx->h_ArithmeticError = DHPy_open_immortal(dctx, uctx->h_ArithmeticError);
    dctx->h_LookupError = DHPy_open_immortal(dctx, uctx->h_LookupError);
    dctx->h_AssertionError = DHPy_open_immortal(dctx, uctx->h_AssertionError);
    dctx->h_AttributeError = DHPy_open_immortal(dctx, uctx->h_AttributeError);
    dctx->h_BufferError = DHPy_open_immortal(dctx, uctx->h_BufferError);
    dctx->h_EOFError = DHPy_open_immortal(dctx, uctx->h_EOFError);
    dctx->h_FloatingPointError = DHPy_open_immortal(dctx, uctx->h_FloatingPointError);
    dctx->h_OSError = DHPy_open_immortal(dctx, uctx->h_OSError);
    dctx->h_ImportError = DHPy_open_immortal(dctx, uctx->h_ImportError);
    dctx->h_ModuleNotFoundError = DHPy_open_immortal(dctx, uctx->h_ModuleNotFoundError);
    dctx->h_IndexError = DHPy_open_immortal(dctx, uctx->h_IndexError);
    dctx->h_KeyError = DHPy_open_immortal(dctx, uctx->h_KeyError);
    dctx->h_KeyboardInterrupt = DHPy_open_immortal(dctx, uctx->h_KeyboardInterrupt);
    dctx->h_MemoryError = DHPy_open_immortal(dctx, uctx->h_MemoryError);
    dctx->h_NameError = DHPy_open_immortal(dctx, uctx->h_NameError);
    dctx->h_OverflowError = DHPy_open_immortal(dctx, uctx->h_OverflowError);
    dctx->h_RuntimeError = DHPy_open_immortal(dctx, uctx->h_RuntimeError);
    dctx->h_RecursionError = DHPy_open_immortal(dctx, uctx->h_RecursionError);
    dctx->h_NotImplementedError = DHPy_open_immortal(dctx, uctx->h_NotImplementedError);
    dctx->h_SyntaxError = DHPy_open_immortal(dctx, uctx->h_SyntaxError);
    dctx->h_IndentationError = DHPy_open_immortal(dctx, uctx->h_IndentationError);
    dctx->h_TabError = DHPy_open_immortal(dctx, uctx->h_TabError);
    dctx->h_ReferenceError = DHPy_open_immortal(dctx, uctx->h_ReferenceError);
    dctx->h_SystemError = DHPy_open_immortal(dctx, uctx->h_SystemError);
    dctx->h_SystemExit = DHPy_open_immortal(dctx, uctx->h_SystemExit);
    dctx->h_TypeError = DHPy_open_immortal(dctx, uctx->h_TypeError);
    dctx->h_UnboundLocalError = DHPy_open_immortal(dctx, uctx->h_UnboundLocalError);
    dctx->h_UnicodeError = DHPy_open_immortal(dctx, uctx->h_UnicodeError);
    dctx->h_UnicodeEncodeError = DHPy_open_immortal(dctx, uctx->h_UnicodeEncodeError);
    dctx->h_UnicodeDecodeError = DHPy_open_immortal(dctx, uctx->h_UnicodeDecodeError);
    dctx->h_UnicodeTranslateError = DHPy_open_immortal(dctx, uctx->h_UnicodeTranslateError);
    dctx->h_ValueError = DHPy_open_immortal(dctx, uctx->h_ValueError);
    dctx->h_ZeroDivisionError = DHPy_open_immortal(dctx, uctx->h_ZeroDivisionError);
    dctx->h_BlockingIOError = DHPy_open_immortal(dctx, uctx->h_BlockingIOError);
    dctx->h_BrokenPipeError = DHPy_open_immortal(dctx, uctx->h_BrokenPipeError);
    dctx->h_ChildProcessError = DHPy_open_immortal(dctx, uctx->h_ChildProcessError);
    dctx->h_ConnectionError = DHPy_open_immortal(dctx, uctx->h_ConnectionError);
    dctx->h_ConnectionAbortedError = DHPy_open_immortal(dctx, uctx->h_ConnectionAbortedError);
    dctx->h_ConnectionRefusedError = DHPy_open_immortal(dctx, uctx->h_ConnectionRefusedError);
    dctx->h_ConnectionResetError = DHPy_open_immortal(dctx, uctx->h_ConnectionResetError);
    dctx->h_FileExistsError = DHPy_open_immortal(dctx, uctx->h_FileExistsError);
    dctx->h_FileNotFoundError = DHPy_open_immortal(dctx, uctx->h_FileNotFoundError);
    dctx->h_InterruptedError = DHPy_open_immortal(dctx, uctx->h_InterruptedError);
    dctx->h_IsADirectoryError = DHPy_open_immortal(dctx, uctx->h_IsADirectoryError);
    dctx->h_NotADirectoryError = DHPy_open_immortal(dctx, uctx->h_NotADirectoryError);
    dctx->h_PermissionError = DHPy_open_immortal(dctx, uctx->h_PermissionError);
    dctx->h_ProcessLookupError = DHPy_open_immortal(dctx, uctx->h_ProcessLookupError);
    dctx->h_TimeoutError = DHPy_open_immortal(dctx, uctx->h_TimeoutError);
    dctx->h_Warning = DHPy_open_immortal(dctx, uctx->h_Warning);
    dctx->h_UserWarning = DHPy_open_immortal(dctx, uctx->h_UserWarning);
    dctx->h_DeprecationWarning = DHPy_open_immortal(dctx, uctx->h_DeprecationWarning);
    dctx->h_PendingDeprecationWarning = DHPy_open_immortal(dctx, uctx->h_PendingDeprecationWarning);
    dctx->h_SyntaxWarning = DHPy_open_immortal(dctx, uctx->h_SyntaxWarning);
    dctx->h_RuntimeWarning = DHPy_open_immortal(dctx, uctx->h_RuntimeWarning);
    dctx->h_FutureWarning = DHPy_open_immortal(dctx, uctx->h_FutureWarning);
    dctx->h_ImportWarning = DHPy_open_immortal(dctx, uctx->h_ImportWarning);
    dctx->h_UnicodeWarning = DHPy_open_immortal(dctx, uctx->h_UnicodeWarning);
    dctx->h_BytesWarning = DHPy_open_immortal(dctx, uctx->h_BytesWarning);
    dctx->h_ResourceWarning = DHPy_open_immortal(dctx, uctx->h_ResourceWarning);
    dctx->h_BaseObjectType = DHPy_open_immortal(dctx, uctx->h_BaseObjectType);
    dctx->h_TypeType = DHPy_open_immortal(dctx, uctx->h_TypeType);
    dctx->h_BoolType = DHPy_open_immortal(dctx, uctx->h_BoolType);
    dctx->h_LongType = DHPy_open_immortal(dctx, uctx->h_LongType);
    dctx->h_FloatType = DHPy_open_immortal(dctx, uctx->h_FloatType);
    dctx->h_UnicodeType = DHPy_open_immortal(dctx, uctx->h_UnicodeType);
    dctx->h_TupleType = DHPy_open_immortal(dctx, uctx->h_TupleType);
    dctx->h_ListType = DHPy_open_immortal(dctx, uctx->h_ListType);
    dctx->h_ComplexType = DHPy_open_immortal(dctx, uctx->h_ComplexType);
    dctx->h_BytesType = DHPy_open_immortal(dctx, uctx->h_BytesType);
    dctx->h_MemoryViewType = DHPy_open_immortal(dctx, uctx->h_MemoryViewType);
    dctx->h_CapsuleType = DHPy_open_immortal(dctx, uctx->h_CapsuleType);
    dctx->h_SliceType = DHPy_open_immortal(dctx, uctx->h_SliceType);
    dctx->h_Builtins = DHPy_open_immortal(dctx, uctx->h_Builtins);
    dctx->ctx_Dup = &debug_ctx_Dup;
    dctx->ctx_Close = &debug_ctx_Close;
    dctx->ctx_Long_FromInt32_t = &debug_ctx_Long_FromInt32_t;
    dctx->ctx_Long_FromUInt32_t = &debug_ctx_Long_FromUInt32_t;
    dctx->ctx_Long_FromInt64_t = &debug_ctx_Long_FromInt64_t;
    dctx->ctx_Long_FromUInt64_t = &debug_ctx_Long_FromUInt64_t;
    dctx->ctx_Long_FromSize_t = &debug_ctx_Long_FromSize_t;
    dctx->ctx_Long_FromSsize_t = &debug_ctx_Long_FromSsize_t;
    dctx->ctx_Long_AsInt32_t = &debug_ctx_Long_AsInt32_t;
    dctx->ctx_Long_AsUInt32_t = &debug_ctx_Long_AsUInt32_t;
    dctx->ctx_Long_AsUInt32_tMask = &debug_ctx_Long_AsUInt32_tMask;
    dctx->ctx_Long_AsInt64_t = &debug_ctx_Long_AsInt64_t;
    dctx->ctx_Long_AsUInt64_t = &debug_ctx_Long_AsUInt64_t;
    dctx->ctx_Long_AsUInt64_tMask = &debug_ctx_Long_AsUInt64_tMask;
    dctx->ctx_Long_AsSize_t = &debug_ctx_Long_AsSize_t;
    dctx->ctx_Long_AsSsize_t = &debug_ctx_Long_AsSsize_t;
    dctx->ctx_Long_AsVoidPtr = &debug_ctx_Long_AsVoidPtr;
    dctx->ctx_Long_AsDouble = &debug_ctx_Long_AsDouble;
    dctx->ctx_Float_FromDouble = &debug_ctx_Float_FromDouble;
    dctx->ctx_Float_AsDouble = &debug_ctx_Float_AsDouble;
    dctx->ctx_Bool_FromBool = &debug_ctx_Bool_FromBool;
    dctx->ctx_Length = &debug_ctx_Length;
    dctx->ctx_Number_Check = &debug_ctx_Number_Check;
    dctx->ctx_Add = &debug_ctx_Add;
    dctx->ctx_Subtract = &debug_ctx_Subtract;
    dctx->ctx_Multiply = &debug_ctx_Multiply;
    dctx->ctx_MatrixMultiply = &debug_ctx_MatrixMultiply;
    dctx->ctx_FloorDivide = &debug_ctx_FloorDivide;
    dctx->ctx_TrueDivide = &debug_ctx_TrueDivide;
    dctx->ctx_Remainder = &debug_ctx_Remainder;
    dctx->ctx_Divmod = &debug_ctx_Divmod;
    dctx->ctx_Power = &debug_ctx_Power;
    dctx->ctx_Negative = &debug_ctx_Negative;
    dctx->ctx_Positive = &debug_ctx_Positive;
    dctx->ctx_Absolute = &debug_ctx_Absolute;
    dctx->ctx_Invert = &debug_ctx_Invert;
    dctx->ctx_Lshift = &debug_ctx_Lshift;
    dctx->ctx_Rshift = &debug_ctx_Rshift;
    dctx->ctx_And = &debug_ctx_And;
    dctx->ctx_Xor = &debug_ctx_Xor;
    dctx->ctx_Or = &debug_ctx_Or;
    dctx->ctx_Index = &debug_ctx_Index;
    dctx->ctx_Long = &debug_ctx_Long;
    dctx->ctx_Float = &debug_ctx_Float;
    dctx->ctx_InPlaceAdd = &debug_ctx_InPlaceAdd;
    dctx->ctx_InPlaceSubtract = &debug_ctx_InPlaceSubtract;
    dctx->ctx_InPlaceMultiply = &debug_ctx_InPlaceMultiply;
    dctx->ctx_InPlaceMatrixMultiply = &debug_ctx_InPlaceMatrixMultiply;
    dctx->ctx_InPlaceFloorDivide = &debug_ctx_InPlaceFloorDivide;
    dctx->ctx_InPlaceTrueDivide = &debug_ctx_InPlaceTrueDivide;
    dctx->ctx_InPlaceRemainder = &debug_ctx_InPlaceRemainder;
    dctx->ctx_InPlacePower = &debug_ctx_InPlacePower;
    dctx->ctx_InPlaceLshift = &debug_ctx_InPlaceLshift;
    dctx->ctx_InPlaceRshift = &debug_ctx_InPlaceRshift;
    dctx->ctx_InPlaceAnd = &debug_ctx_InPlaceAnd;
    dctx->ctx_InPlaceXor = &debug_ctx_InPlaceXor;
    dctx->ctx_InPlaceOr = &debug_ctx_InPlaceOr;
    dctx->ctx_Callable_Check = &debug_ctx_Callable_Check;
    dctx->ctx_CallTupleDict = &debug_ctx_CallTupleDict;
    dctx->ctx_Call = &debug_ctx_Call;
    dctx->ctx_CallMethod = &debug_ctx_CallMethod;
    dctx->ctx_FatalError = &debug_ctx_FatalError;
    dctx->ctx_Err_SetString = &debug_ctx_Err_SetString;
    dctx->ctx_Err_SetObject = &debug_ctx_Err_SetObject;
    dctx->ctx_Err_SetFromErrnoWithFilename = &debug_ctx_Err_SetFromErrnoWithFilename;
    dctx->ctx_Err_SetFromErrnoWithFilenameObjects = &debug_ctx_Err_SetFromErrnoWithFilenameObjects;
    dctx->ctx_Err_Occurred = &debug_ctx_Err_Occurred;
    dctx->ctx_Err_ExceptionMatches = &debug_ctx_Err_ExceptionMatches;
    dctx->ctx_Err_NoMemory = &debug_ctx_Err_NoMemory;
    dctx->ctx_Err_Clear = &debug_ctx_Err_Clear;
    dctx->ctx_Err_NewException = &debug_ctx_Err_NewException;
    dctx->ctx_Err_NewExceptionWithDoc = &debug_ctx_Err_NewExceptionWithDoc;
    dctx->ctx_Err_WarnEx = &debug_ctx_Err_WarnEx;
    dctx->ctx_Err_WriteUnraisable = &debug_ctx_Err_WriteUnraisable;
    dctx->ctx_IsTrue = &debug_ctx_IsTrue;
    dctx->ctx_Type_FromSpec = &debug_ctx_Type_FromSpec;
    dctx->ctx_Type_GenericNew = &debug_ctx_Type_GenericNew;
    dctx->ctx_GetAttr = &debug_ctx_GetAttr;
    dctx->ctx_GetAttr_s = &debug_ctx_GetAttr_s;
    dctx->ctx_HasAttr = &debug_ctx_HasAttr;
    dctx->ctx_HasAttr_s = &debug_ctx_HasAttr_s;
    dctx->ctx_SetAttr = &debug_ctx_SetAttr;
    dctx->ctx_SetAttr_s = &debug_ctx_SetAttr_s;
    dctx->ctx_GetItem = &debug_ctx_GetItem;
    dctx->ctx_GetItem_i = &debug_ctx_GetItem_i;
    dctx->ctx_GetItem_s = &debug_ctx_GetItem_s;
    dctx->ctx_Contains = &debug_ctx_Contains;
    dctx->ctx_SetItem = &debug_ctx_SetItem;
    dctx->ctx_SetItem_i = &debug_ctx_SetItem_i;
    dctx->ctx_SetItem_s = &debug_ctx_SetItem_s;
    dctx->ctx_DelItem = &debug_ctx_DelItem;
    dctx->ctx_DelItem_i = &debug_ctx_DelItem_i;
    dctx->ctx_DelItem_s = &debug_ctx_DelItem_s;
    dctx->ctx_Type = &debug_ctx_Type;
    dctx->ctx_TypeCheck = &debug_ctx_TypeCheck;
    dctx->ctx_Type_GetName = &debug_ctx_Type_GetName;
    dctx->ctx_Type_IsSubtype = &debug_ctx_Type_IsSubtype;
    dctx->ctx_Is = &debug_ctx_Is;
    dctx->ctx_AsStruct_Object = &debug_ctx_AsStruct_Object;
    dctx->ctx_AsStruct_Legacy = &debug_ctx_AsStruct_Legacy;
    dctx->ctx_AsStruct_Type = &debug_ctx_AsStruct_Type;
    dctx->ctx_AsStruct_Long = &debug_ctx_AsStruct_Long;
    dctx->ctx_AsStruct_Float = &debug_ctx_AsStruct_Float;
    dctx->ctx_AsStruct_Unicode = &debug_ctx_AsStruct_Unicode;
    dctx->ctx_AsStruct_Tuple = &debug_ctx_AsStruct_Tuple;
    dctx->ctx_AsStruct_List = &debug_ctx_AsStruct_List;
    dctx->ctx_Type_GetBuiltinShape = &debug_ctx_Type_GetBuiltinShape;
    dctx->ctx_New = &debug_ctx_New;
    dctx->ctx_Repr = &debug_ctx_Repr;
    dctx->ctx_Str = &debug_ctx_Str;
    dctx->ctx_ASCII = &debug_ctx_ASCII;
    dctx->ctx_Bytes = &debug_ctx_Bytes;
    dctx->ctx_RichCompare = &debug_ctx_RichCompare;
    dctx->ctx_RichCompareBool = &debug_ctx_RichCompareBool;
    dctx->ctx_Hash = &debug_ctx_Hash;
    dctx->ctx_Bytes_Check = &debug_ctx_Bytes_Check;
    dctx->ctx_Bytes_Size = &debug_ctx_Bytes_Size;
    dctx->ctx_Bytes_GET_SIZE = &debug_ctx_Bytes_GET_SIZE;
    dctx->ctx_Bytes_AsString = &debug_ctx_Bytes_AsString;
    dctx->ctx_Bytes_AS_STRING = &debug_ctx_Bytes_AS_STRING;
    dctx->ctx_Bytes_FromString = &debug_ctx_Bytes_FromString;
    dctx->ctx_Bytes_FromStringAndSize = &debug_ctx_Bytes_FromStringAndSize;
    dctx->ctx_Unicode_FromString = &debug_ctx_Unicode_FromString;
    dctx->ctx_Unicode_Check = &debug_ctx_Unicode_Check;
    dctx->ctx_Unicode_AsASCIIString = &debug_ctx_Unicode_AsASCIIString;
    dctx->ctx_Unicode_AsLatin1String = &debug_ctx_Unicode_AsLatin1String;
    dctx->ctx_Unicode_AsUTF8String = &debug_ctx_Unicode_AsUTF8String;
    dctx->ctx_Unicode_AsUTF8AndSize = &debug_ctx_Unicode_AsUTF8AndSize;
    dctx->ctx_Unicode_FromWideChar = &debug_ctx_Unicode_FromWideChar;
    dctx->ctx_Unicode_DecodeFSDefault = &debug_ctx_Unicode_DecodeFSDefault;
    dctx->ctx_Unicode_DecodeFSDefaultAndSize = &debug_ctx_Unicode_DecodeFSDefaultAndSize;
    dctx->ctx_Unicode_EncodeFSDefault = &debug_ctx_Unicode_EncodeFSDefault;
    dctx->ctx_Unicode_ReadChar = &debug_ctx_Unicode_ReadChar;
    dctx->ctx_Unicode_DecodeASCII = &debug_ctx_Unicode_DecodeASCII;
    dctx->ctx_Unicode_DecodeLatin1 = &debug_ctx_Unicode_DecodeLatin1;
    dctx->ctx_Unicode_FromEncodedObject = &debug_ctx_Unicode_FromEncodedObject;
    dctx->ctx_Unicode_Substring = &debug_ctx_Unicode_Substring;
    dctx->ctx_List_Check = &debug_ctx_List_Check;
    dctx->ctx_List_New = &debug_ctx_List_New;
    dctx->ctx_List_Append = &debug_ctx_List_Append;
    dctx->ctx_Dict_Check = &debug_ctx_Dict_Check;
    dctx->ctx_Dict_New = &debug_ctx_Dict_New;
    dctx->ctx_Dict_Keys = &debug_ctx_Dict_Keys;
    dctx->ctx_Dict_Copy = &debug_ctx_Dict_Copy;
    dctx->ctx_Tuple_Check = &debug_ctx_Tuple_Check;
    dctx->ctx_Tuple_FromArray = &debug_ctx_Tuple_FromArray;
    dctx->ctx_Slice_Unpack = &debug_ctx_Slice_Unpack;
    dctx->ctx_Import_ImportModule = &debug_ctx_Import_ImportModule;
    dctx->ctx_Capsule_New = &debug_ctx_Capsule_New;
    dctx->ctx_Capsule_Get = &debug_ctx_Capsule_Get;
    dctx->ctx_Capsule_IsValid = &debug_ctx_Capsule_IsValid;
    dctx->ctx_Capsule_Set = &debug_ctx_Capsule_Set;
    dctx->ctx_FromPyObject = &debug_ctx_FromPyObject;
    dctx->ctx_AsPyObject = &debug_ctx_AsPyObject;
    dctx->ctx_CallRealFunctionFromTrampoline = &debug_ctx_CallRealFunctionFromTrampoline;
    dctx->ctx_ListBuilder_New = &debug_ctx_ListBuilder_New;
    dctx->ctx_ListBuilder_Set = &debug_ctx_ListBuilder_Set;
    dctx->ctx_ListBuilder_Build = &debug_ctx_ListBuilder_Build;
    dctx->ctx_ListBuilder_Cancel = &debug_ctx_ListBuilder_Cancel;
    dctx->ctx_TupleBuilder_New = &debug_ctx_TupleBuilder_New;
    dctx->ctx_TupleBuilder_Set = &debug_ctx_TupleBuilder_Set;
    dctx->ctx_TupleBuilder_Build = &debug_ctx_TupleBuilder_Build;
    dctx->ctx_TupleBuilder_Cancel = &debug_ctx_TupleBuilder_Cancel;
    dctx->ctx_Tracker_New = &debug_ctx_Tracker_New;
    dctx->ctx_Tracker_Add = &debug_ctx_Tracker_Add;
    dctx->ctx_Tracker_ForgetAll = &debug_ctx_Tracker_ForgetAll;
    dctx->ctx_Tracker_Close = &debug_ctx_Tracker_Close;
    dctx->ctx_Field_Store = &debug_ctx_Field_Store;
    dctx->ctx_Field_Load = &debug_ctx_Field_Load;
    dctx->ctx_ReenterPythonExecution = &debug_ctx_ReenterPythonExecution;
    dctx->ctx_LeavePythonExecution = &debug_ctx_LeavePythonExecution;
    dctx->ctx_Global_Store = &debug_ctx_Global_Store;
    dctx->ctx_Global_Load = &debug_ctx_Global_Load;
    dctx->ctx_Dump = &debug_ctx_Dump;
    dctx->ctx_Compile_s = &debug_ctx_Compile_s;
    dctx->ctx_EvalCode = &debug_ctx_EvalCode;
    dctx->ctx_ContextVar_New = &debug_ctx_ContextVar_New;
    dctx->ctx_ContextVar_Get = &debug_ctx_ContextVar_Get;
    dctx->ctx_ContextVar_Set = &debug_ctx_ContextVar_Set;
    dctx->ctx_SetCallFunction = &debug_ctx_SetCallFunction;
}
