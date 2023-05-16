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

   This file is automatically generated by hpy.tools.autogen.graalpy.autogen_ctx_init_jni_h
   See also hpy.tools.autogen and hpy/tools/public_api.h

   Run this to regenerate:
       make autogen

*/

_HPy_HIDDEN int init_autogen_jni_ctx(JNIEnv *env, jclass clazz, HPyContext *ctx);
HPy ctx_Module_Create_jni(HPyContext *ctx, HPyModuleDef *def);
HPy ctx_Dup_jni(HPyContext *ctx, HPy h);
void ctx_Close_jni(HPyContext *ctx, HPy h);
HPy ctx_Long_FromLong_jni(HPyContext *ctx, long value);
HPy ctx_Long_FromUnsignedLong_jni(HPyContext *ctx, unsigned long value);
HPy ctx_Long_FromLongLong_jni(HPyContext *ctx, long long v);
HPy ctx_Long_FromUnsignedLongLong_jni(HPyContext *ctx, unsigned long long v);
HPy ctx_Long_FromSize_t_jni(HPyContext *ctx, size_t value);
HPy ctx_Long_FromSsize_t_jni(HPyContext *ctx, HPy_ssize_t value);
long ctx_Long_AsLong_jni(HPyContext *ctx, HPy h);
unsigned long ctx_Long_AsUnsignedLong_jni(HPyContext *ctx, HPy h);
unsigned long ctx_Long_AsUnsignedLongMask_jni(HPyContext *ctx, HPy h);
long long ctx_Long_AsLongLong_jni(HPyContext *ctx, HPy h);
unsigned long long ctx_Long_AsUnsignedLongLong_jni(HPyContext *ctx, HPy h);
unsigned long long ctx_Long_AsUnsignedLongLongMask_jni(HPyContext *ctx, HPy h);
size_t ctx_Long_AsSize_t_jni(HPyContext *ctx, HPy h);
HPy_ssize_t ctx_Long_AsSsize_t_jni(HPyContext *ctx, HPy h);
void *ctx_Long_AsVoidPtr_jni(HPyContext *ctx, HPy h);
double ctx_Long_AsDouble_jni(HPyContext *ctx, HPy h);
HPy ctx_Float_FromDouble_jni(HPyContext *ctx, double v);
double ctx_Float_AsDouble_jni(HPyContext *ctx, HPy h);
HPy ctx_Bool_FromLong_jni(HPyContext *ctx, long v);
HPy_ssize_t ctx_Length_jni(HPyContext *ctx, HPy h);
int ctx_Sequence_Check_jni(HPyContext *ctx, HPy h);
int ctx_Number_Check_jni(HPyContext *ctx, HPy h);
HPy ctx_Add_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_Subtract_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_Multiply_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_MatrixMultiply_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_FloorDivide_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_TrueDivide_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_Remainder_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_Divmod_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_Power_jni(HPyContext *ctx, HPy h1, HPy h2, HPy h3);
HPy ctx_Negative_jni(HPyContext *ctx, HPy h1);
HPy ctx_Positive_jni(HPyContext *ctx, HPy h1);
HPy ctx_Absolute_jni(HPyContext *ctx, HPy h1);
HPy ctx_Invert_jni(HPyContext *ctx, HPy h1);
HPy ctx_Lshift_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_Rshift_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_And_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_Xor_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_Or_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_Index_jni(HPyContext *ctx, HPy h1);
HPy ctx_Long_jni(HPyContext *ctx, HPy h1);
HPy ctx_Float_jni(HPyContext *ctx, HPy h1);
HPy ctx_InPlaceAdd_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlaceSubtract_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlaceMultiply_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlaceMatrixMultiply_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlaceFloorDivide_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlaceTrueDivide_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlaceRemainder_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlacePower_jni(HPyContext *ctx, HPy h1, HPy h2, HPy h3);
HPy ctx_InPlaceLshift_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlaceRshift_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlaceAnd_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlaceXor_jni(HPyContext *ctx, HPy h1, HPy h2);
HPy ctx_InPlaceOr_jni(HPyContext *ctx, HPy h1, HPy h2);
int ctx_Callable_Check_jni(HPyContext *ctx, HPy h);
HPy ctx_CallTupleDict_jni(HPyContext *ctx, HPy callable, HPy args, HPy kw);
void ctx_FatalError_jni(HPyContext *ctx, const char *message);
void ctx_Err_SetString_jni(HPyContext *ctx, HPy h_type, const char *message);
void ctx_Err_SetObject_jni(HPyContext *ctx, HPy h_type, HPy h_value);
HPy ctx_Err_SetFromErrnoWithFilename_jni(HPyContext *ctx, HPy h_type, const char *filename_fsencoded);
void ctx_Err_SetFromErrnoWithFilenameObjects_jni(HPyContext *ctx, HPy h_type, HPy filename1, HPy filename2);
int ctx_Err_Occurred_jni(HPyContext *ctx);
int ctx_Err_ExceptionMatches_jni(HPyContext *ctx, HPy exc);
void ctx_Err_NoMemory_jni(HPyContext *ctx);
void ctx_Err_Clear_jni(HPyContext *ctx);
HPy ctx_Err_NewException_jni(HPyContext *ctx, const char *name, HPy base, HPy dict);
HPy ctx_Err_NewExceptionWithDoc_jni(HPyContext *ctx, const char *name, const char *doc, HPy base, HPy dict);
int ctx_Err_WarnEx_jni(HPyContext *ctx, HPy category, const char *message, HPy_ssize_t stack_level);
void ctx_Err_WriteUnraisable_jni(HPyContext *ctx, HPy obj);
int ctx_IsTrue_jni(HPyContext *ctx, HPy h);
HPy ctx_Type_FromSpec_jni(HPyContext *ctx, HPyType_Spec *spec, HPyType_SpecParam *params);
HPy ctx_Type_GenericNew_jni(HPyContext *ctx, HPy type, HPy *args, HPy_ssize_t nargs, HPy kw);
HPy ctx_GetAttr_jni(HPyContext *ctx, HPy obj, HPy name);
HPy ctx_GetAttr_s_jni(HPyContext *ctx, HPy obj, const char *name);
HPy ctx_MaybeGetAttr_s_jni(HPyContext *ctx, HPy obj, const char *name);
int ctx_HasAttr_jni(HPyContext *ctx, HPy obj, HPy name);
int ctx_HasAttr_s_jni(HPyContext *ctx, HPy obj, const char *name);
int ctx_SetAttr_jni(HPyContext *ctx, HPy obj, HPy name, HPy value);
int ctx_SetAttr_s_jni(HPyContext *ctx, HPy obj, const char *name, HPy value);
HPy ctx_GetItem_jni(HPyContext *ctx, HPy obj, HPy key);
HPy ctx_GetItem_i_jni(HPyContext *ctx, HPy obj, HPy_ssize_t idx);
HPy ctx_GetItem_s_jni(HPyContext *ctx, HPy obj, const char *key);
int ctx_Contains_jni(HPyContext *ctx, HPy container, HPy key);
int ctx_SetItem_jni(HPyContext *ctx, HPy obj, HPy key, HPy value);
int ctx_SetItem_i_jni(HPyContext *ctx, HPy obj, HPy_ssize_t idx, HPy value);
int ctx_SetItem_s_jni(HPyContext *ctx, HPy obj, const char *key, HPy value);
HPy ctx_Type_jni(HPyContext *ctx, HPy obj);
int ctx_TypeCheck_jni(HPyContext *ctx, HPy obj, HPy type);
int ctx_TypeCheck_g_jni(HPyContext *ctx, HPy obj, HPyGlobal type);
int ctx_SetType_jni(HPyContext *ctx, HPy obj, HPy type);
int ctx_Type_IsSubtype_jni(HPyContext *ctx, HPy sub, HPy type);
const char *ctx_Type_GetName_jni(HPyContext *ctx, HPy type);
int ctx_Is_jni(HPyContext *ctx, HPy obj, HPy other);
int ctx_Is_g_jni(HPyContext *ctx, HPy obj, HPyGlobal other);
void *ctx_AsStruct_jni(HPyContext *ctx, HPy h);
void *ctx_AsStructLegacy_jni(HPyContext *ctx, HPy h);
HPy ctx_New_jni(HPyContext *ctx, HPy h_type, void **data);
HPy ctx_Repr_jni(HPyContext *ctx, HPy obj);
HPy ctx_Str_jni(HPyContext *ctx, HPy obj);
HPy ctx_ASCII_jni(HPyContext *ctx, HPy obj);
HPy ctx_Bytes_jni(HPyContext *ctx, HPy obj);
HPy ctx_RichCompare_jni(HPyContext *ctx, HPy v, HPy w, int op);
int ctx_RichCompareBool_jni(HPyContext *ctx, HPy v, HPy w, int op);
HPy_hash_t ctx_Hash_jni(HPyContext *ctx, HPy obj);
HPy ctx_SeqIter_New_jni(HPyContext *ctx, HPy seq);
int ctx_Bytes_Check_jni(HPyContext *ctx, HPy h);
HPy_ssize_t ctx_Bytes_Size_jni(HPyContext *ctx, HPy h);
HPy_ssize_t ctx_Bytes_GET_SIZE_jni(HPyContext *ctx, HPy h);
char *ctx_Bytes_AsString_jni(HPyContext *ctx, HPy h);
char *ctx_Bytes_AS_STRING_jni(HPyContext *ctx, HPy h);
HPy ctx_Bytes_FromString_jni(HPyContext *ctx, const char *v);
HPy ctx_Bytes_FromStringAndSize_jni(HPyContext *ctx, const char *v, HPy_ssize_t len);
HPy ctx_Unicode_FromString_jni(HPyContext *ctx, const char *utf8);
int ctx_Unicode_Check_jni(HPyContext *ctx, HPy h);
HPy ctx_Unicode_AsASCIIString_jni(HPyContext *ctx, HPy h);
HPy ctx_Unicode_AsLatin1String_jni(HPyContext *ctx, HPy h);
HPy ctx_Unicode_AsUTF8String_jni(HPyContext *ctx, HPy h);
const char *ctx_Unicode_AsUTF8AndSize_jni(HPyContext *ctx, HPy h, HPy_ssize_t *size);
HPy ctx_Unicode_FromWideChar_jni(HPyContext *ctx, const wchar_t *w, HPy_ssize_t size);
HPy ctx_Unicode_DecodeFSDefault_jni(HPyContext *ctx, const char *v);
HPy ctx_Unicode_DecodeFSDefaultAndSize_jni(HPyContext *ctx, const char *v, HPy_ssize_t size);
HPy ctx_Unicode_EncodeFSDefault_jni(HPyContext *ctx, HPy h);
HPy_UCS4 ctx_Unicode_ReadChar_jni(HPyContext *ctx, HPy h, HPy_ssize_t index);
HPy ctx_Unicode_DecodeASCII_jni(HPyContext *ctx, const char *s, HPy_ssize_t size, const char *errors);
HPy ctx_Unicode_DecodeLatin1_jni(HPyContext *ctx, const char *s, HPy_ssize_t size, const char *errors);
HPy ctx_Unicode_FromEncodedObject_jni(HPyContext *ctx, HPy obj, const char *encoding, const char *errors);
HPy ctx_Unicode_InternFromString_jni(HPyContext *ctx, const char *str);
HPy ctx_Unicode_Substring_jni(HPyContext *ctx, HPy obj, HPy_ssize_t start, HPy_ssize_t end);
int ctx_List_Check_jni(HPyContext *ctx, HPy h);
HPy ctx_List_New_jni(HPyContext *ctx, HPy_ssize_t len);
int ctx_List_Append_jni(HPyContext *ctx, HPy h_list, HPy h_item);
int ctx_Dict_Check_jni(HPyContext *ctx, HPy h);
HPy ctx_Dict_New_jni(HPyContext *ctx);
HPy ctx_Dict_Keys_jni(HPyContext *ctx, HPy h);
HPy ctx_Dict_GetItem_jni(HPyContext *ctx, HPy op, HPy key);
int ctx_Tuple_Check_jni(HPyContext *ctx, HPy h);
HPy ctx_Tuple_FromArray_jni(HPyContext *ctx, HPy items[], HPy_ssize_t n);
int ctx_Slice_Unpack_jni(HPyContext *ctx, HPy slice, HPy_ssize_t *start, HPy_ssize_t *stop, HPy_ssize_t *step);
HPy ctx_ContextVar_New_jni(HPyContext *ctx, const char *name, HPy default_value);
int ctx_ContextVar_Get_jni(HPyContext *ctx, HPy context_var, HPy default_value, HPy *result);
HPy ctx_ContextVar_Set_jni(HPyContext *ctx, HPy context_var, HPy value);
HPy ctx_Import_ImportModule_jni(HPyContext *ctx, const char *name);
HPy ctx_Capsule_New_jni(HPyContext *ctx, void *pointer, const char *name, HPyCapsule_Destructor destructor);
void *ctx_Capsule_Get_jni(HPyContext *ctx, HPy capsule, _HPyCapsule_key key, const char *name);
int ctx_Capsule_IsValid_jni(HPyContext *ctx, HPy capsule, const char *name);
int ctx_Capsule_Set_jni(HPyContext *ctx, HPy capsule, _HPyCapsule_key key, void *value);
HPy ctx_FromPyObject_jni(HPyContext *ctx, cpy_PyObject *obj);
cpy_PyObject *ctx_AsPyObject_jni(HPyContext *ctx, HPy h);
HPyListBuilder ctx_ListBuilder_New_jni(HPyContext *ctx, HPy_ssize_t initial_size);
void ctx_ListBuilder_Set_jni(HPyContext *ctx, HPyListBuilder builder, HPy_ssize_t index, HPy h_item);
HPy ctx_ListBuilder_Build_jni(HPyContext *ctx, HPyListBuilder builder);
void ctx_ListBuilder_Cancel_jni(HPyContext *ctx, HPyListBuilder builder);
HPyTupleBuilder ctx_TupleBuilder_New_jni(HPyContext *ctx, HPy_ssize_t initial_size);
void ctx_TupleBuilder_Set_jni(HPyContext *ctx, HPyTupleBuilder builder, HPy_ssize_t index, HPy h_item);
HPy ctx_TupleBuilder_Build_jni(HPyContext *ctx, HPyTupleBuilder builder);
void ctx_TupleBuilder_Cancel_jni(HPyContext *ctx, HPyTupleBuilder builder);
HPyTracker ctx_Tracker_New_jni(HPyContext *ctx, HPy_ssize_t size);
int ctx_Tracker_Add_jni(HPyContext *ctx, HPyTracker ht, HPy h);
void ctx_Tracker_ForgetAll_jni(HPyContext *ctx, HPyTracker ht);
void ctx_Tracker_Close_jni(HPyContext *ctx, HPyTracker ht);
void ctx_Field_Store_jni(HPyContext *ctx, HPy target_object, HPyField *target_field, HPy h);
HPy ctx_Field_Load_jni(HPyContext *ctx, HPy source_object, HPyField source_field);
void ctx_ReenterPythonExecution_jni(HPyContext *ctx, HPyThreadState state);
HPyThreadState ctx_LeavePythonExecution_jni(HPyContext *ctx);
void ctx_Global_Store_jni(HPyContext *ctx, HPyGlobal *global, HPy h);
HPy ctx_Global_Load_jni(HPyContext *ctx, HPyGlobal global);
void ctx_Dump_jni(HPyContext *ctx, HPy h);
int ctx_Type_CheckSlot_jni(HPyContext *ctx, HPy type, HPyDef *value);

